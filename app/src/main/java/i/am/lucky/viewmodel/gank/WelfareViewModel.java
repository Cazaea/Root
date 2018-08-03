package i.am.lucky.viewmodel.gank;

import android.arch.lifecycle.ViewModel;

import i.am.lucky.http.HttpUtils;
import i.am.lucky.base.BaseFragment;
import i.am.lucky.bean.GankIoDataBean;
import i.am.lucky.data.model.GankOtherModel;
import i.am.lucky.http.RequestImpl;

import java.util.ArrayList;

import rx.Subscription;

/**
 * @author Cazaea
 * @data 2018/1/17
 * @Description 福利页面ViewModel
 */

public class WelfareViewModel extends ViewModel {

    private final BaseFragment activity;
    private final GankOtherModel mModel;
    private WelfareNavigator navigator;
    private int mPage = 1;
    private ArrayList<String> imgList = new ArrayList<>();
    private ArrayList<String> imageTitleList = new ArrayList<>();

    public void setNavigator(WelfareNavigator navigator) {
        this.navigator = navigator;
    }

    public void onDestroy() {
        navigator = null;
    }

    public WelfareViewModel(BaseFragment activity) {
        this.activity = activity;
        mModel = new GankOtherModel();
    }

    public void loadWelfareData(){
        mModel.setData("福利", mPage, HttpUtils.per_page_more);
        mModel.getGankIoData(new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                navigator.showLoadSuccessView();

                GankIoDataBean gankIoDataBean = (GankIoDataBean) object;
                if (mPage == 1) {
                    if (gankIoDataBean != null
                            && gankIoDataBean.getResults() != null
                            && gankIoDataBean.getResults().size() > 0) {
                        imgList.clear();
                        for (int i = 0; i < gankIoDataBean.getResults().size(); i++) {
                            imgList.add(gankIoDataBean.getResults().get(i).getUrl());
                            imageTitleList.add(gankIoDataBean.getResults().get(i).getDesc());
                        }
                        navigator.setImageList(imgList,imageTitleList);
                        navigator.showAdapterView(gankIoDataBean);

                    } else {
                        navigator.showLoadFailedView();
                    }
                } else {
                    if (gankIoDataBean != null && gankIoDataBean.getResults() != null && gankIoDataBean.getResults().size() > 0) {
                        navigator.refreshAdapter(gankIoDataBean);
                        for (int i = 0; i < gankIoDataBean.getResults().size(); i++) {
                            imgList.add(gankIoDataBean.getResults().get(i).getUrl());
                            imageTitleList.add(gankIoDataBean.getResults().get(i).getDesc());
                        }
                        navigator.setImageList(imgList,imageTitleList);
                    } else {
                        navigator.showListNoMoreLoading();
                    }
                }
            }

            @Override
            public void loadFailed() {
                navigator.showLoadFailedView();
                if (mPage > 1) {
                    mPage--;
                }
            }

            @Override
            public void addSubscription(Subscription subscription) {
                activity.addSubscription(subscription);
            }
        });
    }

    public int getPage() {
        return mPage;
    }

    public void setPage(int mPage) {
        this.mPage = mPage;
    }
}
