package i.am.lucky.viewmodel.gank;

import android.arch.lifecycle.ViewModel;

import i.am.lucky.app.Constants;
import i.am.lucky.app.RootApplication;
import i.am.lucky.bean.AndroidBean;
import i.am.lucky.bean.FrontPageBean;
import i.am.lucky.data.model.EverydayModel;
import i.am.lucky.http.RequestImpl;
import i.am.lucky.http.cache.ACache;
import i.am.lucky.utils.SPUtils;
import i.am.lucky.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

/**
 * @author Cazaea
 * @data 2017/12/15
 */
public class EverydayViewModel extends ViewModel {

    private EverydayModel mEverydayModel;
    private ACache maCache;
    private EverydayNavigator everydayNavigator;
    private ArrayList<List<AndroidBean>> mLists;
    private ArrayList<String> mBannerImages;
    private String year;
    private String month;
    private String day;

    public void setEverydayNavigator(EverydayNavigator everydayNavigator) {
        this.everydayNavigator = everydayNavigator;
    }

    public EverydayViewModel() {
        maCache = ACache.get(RootApplication.getInstance());
        mEverydayModel = new EverydayModel();
        year = getTodayTime().get(0);
        month = getTodayTime().get(1);
        day = getTodayTime().get(2);
        mEverydayModel.setData(year, month, day);
    }

    private void showRecyclerViewData() {
        mEverydayModel.showRecyclerViewData(new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                if (mLists != null) {
                    mLists.clear();
                }
                mLists = (ArrayList<List<AndroidBean>>) object;
                if (mLists.size() > 0 && mLists.get(0).size() > 0) {
                    everydayNavigator.showListView(mLists);
                    maCache.remove(Constants.EVERYDAY_CONTENT);
                    maCache.put(Constants.EVERYDAY_CONTENT, mLists);
                } else {
                    mLists = (ArrayList<List<AndroidBean>>) maCache.getAsObject(Constants.EVERYDAY_CONTENT);
                    if (mLists != null && mLists.size() > 0) {
                        everydayNavigator.showListView(mLists);
                    } else {
                        // 一直请求，直到请求到数据为止
                        ArrayList<String> lastTime = TimeUtil.getLastTime(year, month, day);
                        mEverydayModel.setData(lastTime.get(0), lastTime.get(1), lastTime.get(2));
                        year = lastTime.get(0);
                        month = lastTime.get(1);
                        day = lastTime.get(2);
                        showRecyclerViewData();
                    }
                }
            }

            @Override
            public void loadFailed() {
                if (mLists != null && mLists.size() > 0) {
                    return;
                }
                handleNoData();
            }

            @Override
            public void addSubscription(Subscription subscription) {
                everydayNavigator.addRxSubscription(subscription);
            }
        });
    }

    private void handleNoData() {
        mLists = (ArrayList<List<AndroidBean>>) maCache.getAsObject(Constants.EVERYDAY_CONTENT);
        if (mLists != null && mLists.size() > 0) {
            everydayNavigator.showListView(mLists);
        } else {
            everydayNavigator.showErrorView();
        }
    }

    private void showBannerPage() {
        mEverydayModel.showBannerPage(new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                if (mBannerImages == null) {
                    mBannerImages = new ArrayList<String>();
                } else {
                    mBannerImages.clear();
                }
                FrontPageBean bean = (FrontPageBean) object;
                if (bean != null && bean.getResult() != null && bean.getResult().getFocus() != null && bean.getResult().getFocus().getResult() != null) {
                    final ArrayList<FrontPageBean.ResultBannerBean.FocusBean.ResultBeanX> result = (ArrayList<FrontPageBean.ResultBannerBean.FocusBean.ResultBeanX>) bean.getResult().getFocus().getResult();
                    if (result != null && result.size() > 0) {
                        for (int i = 0; i < result.size(); i++) {
                            //获取所有图片
                            mBannerImages.add(result.get(i).getRandpic());
                        }
                        everydayNavigator.showBannerView(mBannerImages, result);
                        maCache.remove(Constants.BANNER_PIC);
                        maCache.put(Constants.BANNER_PIC, mBannerImages);
                        maCache.remove(Constants.BANNER_PIC_DATA);
                        maCache.put(Constants.BANNER_PIC_DATA, result);
                    }
                }
            }

            @Override
            public void loadFailed() {

            }

            @Override
            public void addSubscription(Subscription subscription) {
                everydayNavigator.addRxSubscription(subscription);
            }
        });
    }

    public void handleCache() {
        ArrayList<FrontPageBean.ResultBannerBean.FocusBean.ResultBeanX> result = null;
        try {
            mBannerImages = (ArrayList<String>) maCache.getAsObject(Constants.BANNER_PIC);
            result = (ArrayList<FrontPageBean.ResultBannerBean.FocusBean.ResultBeanX>) maCache.getAsObject(Constants.BANNER_PIC_DATA);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mBannerImages != null && mBannerImages.size() > 0) {
            // 加上缓存使其可以点击
            everydayNavigator.showBannerView(mBannerImages, result);
        } else {
            showBannerPage();
        }
        mLists = (ArrayList<List<AndroidBean>>) maCache.getAsObject(Constants.EVERYDAY_CONTENT);
        if (mLists != null && mLists.size() > 0) {
            everydayNavigator.showListView(mLists);
        } else {
            everydayNavigator.showRotaLoading();
            showRecyclerViewData();
        }
    }


    public void loadData() {
        String oneData = SPUtils.getString("everyday_data", "2016-11-26");
        if (!oneData.equals(TimeUtil.getData())) {
            // 是第二天
            if (TimeUtil.isRightTime()) {
                //大于12：30,请求
                everydayNavigator.setIsOldDayRequest(false);
                mEverydayModel.setData(getTodayTime().get(0), getTodayTime().get(1), getTodayTime().get(2));
                everydayNavigator.showRotaLoading();
                showBannerPage();
                showRecyclerViewData();

            } else {
                // 小于，取缓存没有请求前一天
                ArrayList<String> lastTime = TimeUtil.getLastTime(getTodayTime().get(0), getTodayTime().get(1), getTodayTime().get(2));
                mEverydayModel.setData(lastTime.get(0), lastTime.get(1), lastTime.get(2));
                year = lastTime.get(0);
                month = lastTime.get(1);
                day = lastTime.get(2);
                // 是昨天
                everydayNavigator.setIsOldDayRequest(true);
                everydayNavigator.getACacheData();
            }
        } else {// 当天，取缓存没有请求当天
            // 是昨天
            everydayNavigator.setIsOldDayRequest(false);
            everydayNavigator.getACacheData();
        }
    }

    /**
     * 获取当天日期
     */
    public static ArrayList<String> getTodayTime() {
        String data = TimeUtil.getData();
        String[] split = data.split("-");
        String year = split[0];
        String month = split[1];
        String day = split[2];
        ArrayList<String> list = new ArrayList<>();
        list.add(year);
        list.add(month);
        list.add(day);
        return list;
    }

    public void onDestroy() {
        everydayNavigator = null;
        mEverydayModel = null;
        if (mLists != null) {
            mLists.clear();
            mLists = null;
        }
        if (mBannerImages != null) {
            mBannerImages.clear();
            mBannerImages = null;
        }
    }
}