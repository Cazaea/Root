package i.am.lucky.utils;

import android.annotation.SuppressLint;
import android.widget.Toast;

import i.am.lucky.app.RootApplication;

/**
 * Created by Cazaea on 2016/12/14.
 * 单例Toast
 */

public class ToastUtil {

    private static Toast mToast;

    @SuppressLint("ShowToast")
    public static void showToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(RootApplication.getInstance(), text, Toast.LENGTH_SHORT);
        } else {
            mToast.cancel();
            mToast = Toast.makeText(RootApplication.getInstance(), text, Toast.LENGTH_SHORT);
        }
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.setText(text);
        mToast.show();
    }

    @SuppressLint("ShowToast")
    public static void showToastLong(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(RootApplication.getInstance(), text, Toast.LENGTH_LONG);
        } else {
            mToast.cancel();
            mToast = Toast.makeText(RootApplication.getInstance(), text, Toast.LENGTH_LONG);
        }
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.setText(text);
        mToast.show();
    }

}
