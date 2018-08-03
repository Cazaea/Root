package i.am.lucky.data.model;

import i.am.lucky.base.BaseActivity;
import i.am.lucky.bean.wanandroid.HomeListBean;
import i.am.lucky.http.HttpClient;
import i.am.lucky.viewmodel.wan.WanNavigator;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Cazaea
 * @data 2018/5/10
 * @Description
 */

public class CollectModel {

    private BaseActivity activity;

    public CollectModel() {
    }

    public CollectModel(BaseActivity activity) {
        this.activity = activity;
    }

    /**
     * 收藏
     */
    public void collect(int id, WanNavigator.OnCollectNavigator navigator) {
        Subscription subscribe = HttpClient.Builder.getWanAndroidServer().collect(id)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HomeListBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        navigator.onFailure();
                    }

                    @Override
                    public void onNext(HomeListBean bean) {
                        if (bean != null && bean.getErrorCode() == 0) {
                            navigator.onSuccess();
                        } else {
                            navigator.onFailure();
                        }
                    }
                });
        if (activity != null) {
            activity.addSubscription(subscribe);
        }
    }

    /**
     * @param isCollectList 是否是收藏列表
     * @param originId      如果是收藏列表的话就是原始文章的id，如果是站外文章就是-1
     * @param id            bean里的id
     */
    public void unCollect(boolean isCollectList, int id, int originId, WanNavigator.OnCollectNavigator navigator) {
        if (isCollectList) {
            unCollect(id, originId, navigator);
        } else {
            unCollect(id, navigator);
        }
    }

    /**
     * 取消收藏
     */
    private void unCollect(int id, WanNavigator.OnCollectNavigator navigator) {
        Subscription subscribe = HttpClient.Builder.getWanAndroidServer().unCollectOrigin(id)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HomeListBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        navigator.onFailure();
                    }

                    @Override
                    public void onNext(HomeListBean bean) {
                        if (bean != null && bean.getErrorCode() == 0) {
                            navigator.onSuccess();
                        } else {
                            navigator.onFailure();
                        }
                    }
                });
        if (activity != null) {
            activity.addSubscription(subscribe);
        }
    }

    /**
     * 取消收藏，我的收藏页
     */
    private void unCollect(int id, int originId, WanNavigator.OnCollectNavigator navigator) {
        Subscription subscribe = HttpClient.Builder.getWanAndroidServer().unCollect(id, originId)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HomeListBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        navigator.onFailure();
                    }

                    @Override
                    public void onNext(HomeListBean bean) {
                        if (bean != null && bean.getErrorCode() == 0) {
                            navigator.onSuccess();
                        } else {
                            navigator.onFailure();
                        }
                    }
                });
        if (activity != null) {
            activity.addSubscription(subscribe);
        }
    }

}
