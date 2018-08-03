package i.am.lucky.viewmodel.wan;

import i.am.lucky.base.BaseActivity;
import i.am.lucky.base.BaseListViewModel;
import i.am.lucky.bean.wanandroid.HomeListBean;
import i.am.lucky.http.HttpClient;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Cazaea
 * @data 2018/5/9
 * @Description 文章列表ViewModel
 */

public class ArticleListListViewModel extends BaseListViewModel {

    private final BaseActivity activity;
    private WanNavigator.ArticleListNavigator navigator;

    public ArticleListListViewModel(BaseActivity activity, WanNavigator.ArticleListNavigator navigator) {
        this.activity = activity;
        this.navigator = navigator;
    }

    /**
     * 我的收藏
     */
    public void getCollectList() {
        Subscription subscribe = HttpClient.Builder.getWanAndroidServer().getCollectList(mPage)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HomeListBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        navigator.loadHomeListFailure();
                    }

                    @Override
                    public void onNext(HomeListBean bean) {
                        navigator.showLoadSuccessView();
                        if (mPage == 0) {
                            if (bean == null || bean.getData() == null || bean.getData().getDatas() == null || bean.getData().getDatas().size() <= 0) {
                                navigator.loadHomeListFailure();
                                return;
                            }
                        } else {
                            if (bean == null || bean.getData() == null || bean.getData().getDatas() == null || bean.getData().getDatas().size() <= 0) {
                                navigator.showListNoMoreLoading();
                                return;
                            }
                        }
                        navigator.showAdapterView(bean);
                    }
                });
        activity.addSubscription(subscribe);
    }

    public void onDestroy() {
        navigator = null;
    }
}
