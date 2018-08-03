package i.am.lucky.viewmodel.menu;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;
import android.text.TextUtils;

import i.am.lucky.base.BaseActivity;
import i.am.lucky.bean.wanandroid.LoginBean;
import i.am.lucky.data.UserUtil;
import i.am.lucky.data.room.Injection;
import i.am.lucky.http.HttpClient;
import i.am.lucky.utils.ToastUtil;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Cazaea
 * @data 2018/5/7
 * @Description
 */

public class LoginViewModel extends ViewModel {

    public final ObservableField<String> username = new ObservableField<>();

    public final ObservableField<String> password = new ObservableField<>();

    @SuppressLint("StaticFieldLeak")
    private BaseActivity activity;
    private LoginNavigator navigator;

    public LoginViewModel(BaseActivity activity) {
        this.activity = activity;
    }

    public void setNavigator(LoginNavigator navigator) {
        this.navigator = navigator;
    }

    public void register() {
        if (!verifyData()) {
            return;
        }
        Subscription subscribe = HttpClient.Builder.getWanAndroidServer().register(username.get(), password.get(), password.get())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(LoginBean bean) {
                        if (bean != null && bean.getData() != null) {
                            Injection.get().addData(bean.getData());
                            UserUtil.handleLoginSuccess();
                            navigator.loadSuccess();
                        } else {
                            if (bean != null) {
                                ToastUtil.showToastLong(bean.getErrorMsg());
                            }
                        }
                    }
                });
        activity.addSubscription(subscribe);
    }

    public void login() {
        if (!verifyData()) {
            return;
        }
        Subscription subscribe = HttpClient.Builder.getWanAndroidServer().login(username.get(), password.get())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(LoginBean bean) {
                        if (bean != null && bean.getData() != null) {
                            Injection.get().addData(bean.getData());
                            UserUtil.handleLoginSuccess();
                            navigator.loadSuccess();
                        } else {
                            if (bean != null) {
                                ToastUtil.showToastLong(bean.getErrorMsg());
                            }
                        }
                    }
                });
        activity.addSubscription(subscribe);
    }

    private boolean verifyData() {
        if (TextUtils.isEmpty(username.get())) {
            ToastUtil.showToast("请输入用户名");
            return false;
        }
        if (TextUtils.isEmpty(password.get())) {
            ToastUtil.showToast("请输入密码");
            return false;
        }
        return true;
    }

    public void onDestroy() {
        navigator = null;
    }
}
