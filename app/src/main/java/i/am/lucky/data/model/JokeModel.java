package i.am.lucky.data.model;

import android.text.TextUtils;

import i.am.lucky.bean.wanandroid.DuanZiBean;
import i.am.lucky.bean.wanandroid.NhdzListBean;
import i.am.lucky.bean.wanandroid.QsbkListBean;
import i.am.lucky.http.HttpClient;
import i.am.lucky.viewmodel.wan.WanNavigator;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author Cazaea
 * @data 2018/2/9
 * @Description
 */

public class JokeModel {


    public void showNhdzList(final WanNavigator.JokeModelNavigator listener, int page) {
        Func1<NhdzListBean, Observable<List<DuanZiBean>>> func1 = bean -> {
            List<DuanZiBean> lists = new ArrayList<>();

            if (bean != null && bean.getData() != null && bean.getData().getData() != null
                    && bean.getData().getData().size() > 0) {
                List<NhdzListBean.DataBeanX.DataBean> data = bean.getData().getData();
                for (NhdzListBean.DataBeanX.DataBean bean1 : data) {
                    NhdzListBean.DataBeanX.DataBean.GroupBean group = bean1.getGroup();
                    if (group != null) {
                        DuanZiBean duanZiBean = new DuanZiBean();
                        duanZiBean.setCategoryName(group.getCategory_name());
                        duanZiBean.setContent(group.getContent());
                        duanZiBean.setCreateTime(group.getCreate_time());
                        NhdzListBean.DataBeanX.DataBean.GroupBean.UserBean user = group.getUser();
                        if (user != null) {
                            duanZiBean.setAvatarUrl(user.getAvatar_url());
                            duanZiBean.setName(user.getName());
                        }
                        lists.add(duanZiBean);
                    }
                }
            }
            return Observable.just(lists);
        };

        Observer<List<DuanZiBean>> observer = new Observer<List<DuanZiBean>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                listener.loadFailed();
            }

            @Override
            public void onNext(List<DuanZiBean> lists) {
                listener.loadSuccess(lists);
            }
        };

        Subscription subscription = HttpClient.Builder.getNHDZServer().getNhdzList(page)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .flatMap(func1)
                .subscribe(observer);
        listener.addSubscription(subscription);
    }


    public void showQSBKList(final WanNavigator.JokeModelNavigator listener, int page) {
        Func1<QsbkListBean, Observable<List<DuanZiBean>>> func1 = bean -> {

            List<DuanZiBean> lists = new ArrayList<>();
            if (bean != null && bean.getItems() != null && bean.getItems().size() > 0) {
                List<QsbkListBean.ItemsBean> items = bean.getItems();
                for (QsbkListBean.ItemsBean bean1 : items) {
                    DuanZiBean duanZiBean = new DuanZiBean();
                    duanZiBean.setContent(bean1.getContent());
                    duanZiBean.setCreateTime(bean1.getPublished_at());
                    QsbkListBean.ItemsBean.TopicBean topic = bean1.getTopic();
                    QsbkListBean.ItemsBean.UserBean user = bean1.getUser();
                    if (topic != null) {
                        duanZiBean.setCategoryName(topic.getContent());
                    }
                    if (user != null) {
                        duanZiBean.setName(user.getLogin());
                        String thumb = user.getThumb();
                        if (!TextUtils.isEmpty(thumb)) {
                            String string = "http:" + thumb;
//                                String s = thumb.replaceAll("\\\\\\\\", "\\");
                            duanZiBean.setAvatarUrl(string);
                        }
                    }
//                        bean1.getHot_comment();
                    lists.add(duanZiBean);
                }
            }

            return Observable.just(lists);
        };

        Observer<List<DuanZiBean>> observer = new Observer<List<DuanZiBean>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                listener.loadFailed();
            }

            @Override
            public void onNext(List<DuanZiBean> lists) {
                listener.loadSuccess(lists);
            }
        };

        Subscription subscription = HttpClient.Builder.getQSBKServer().getQsbkList(page)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .flatMap(func1)
                .subscribe(observer);
        listener.addSubscription(subscription);
    }

}
