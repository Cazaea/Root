package i.am.lucky.data;

import android.content.Context;

import i.am.lucky.app.Constants;
import i.am.lucky.data.room.Injection;
import i.am.lucky.data.room.User;
import i.am.lucky.data.room.UserDataCallback;
import i.am.lucky.ui.menu.LoginActivity;
import i.am.lucky.utils.SPUtils;
import i.am.lucky.utils.ToastUtil;

/**
 * @author Cazaea
 * @data 2018/5/7
 * @Description 处理用户登录问题
 */
public class UserUtil {

    /**
     * 初始化登录状态
     */
    public static void getLoginStatus() {
        Injection.get().getSingleBean(new UserDataCallback() {
            @Override
            public void onDataNotAvailable() {
                SPUtils.putBoolean(Constants.IS_LOGIN, false);
            }

            @Override
            public void getData(User bean) {
                SPUtils.putBoolean(Constants.IS_LOGIN, true);
            }
        });
    }

    public static void handleLoginSuccess() {
        SPUtils.putBoolean(Constants.IS_LOGIN, true);
    }

    public static void handleLoginFailure() {
        SPUtils.putBoolean(Constants.IS_LOGIN, false);
        SPUtils.remove("cookie");
    }

    /**
     * 是否登录，没有进入登录页面
     */
    public static boolean isLogin(Context context) {
        boolean isLogin = SPUtils.getBoolean(Constants.IS_LOGIN, false);
        if (!isLogin) {
            ToastUtil.showToastLong("请先登录~");
            LoginActivity.start(context);
            return false;
        } else {
            return true;
        }
    }
}
