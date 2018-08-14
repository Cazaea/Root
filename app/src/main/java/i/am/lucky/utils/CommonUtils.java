package i.am.lucky.utils;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import i.am.lucky.app.RootApplication;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by Cazaea on 2016/11/22.
 * 获取原生资源常用工具
 */
public class CommonUtils {

    /**
     * 随机颜色
     */
    public static int randomColor() {
        Random random = new Random();
        int red = random.nextInt(150) + 50;     // 50-199
        int green = random.nextInt(150) + 50;   // 50-199
        int blue = random.nextInt(150) + 50;    // 50-199
        return Color.rgb(red, green, blue);
    }

    /**
     * 得到年月日的"日"
     */
    private String getDate() {
        Date date = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFm = new SimpleDateFormat("dd");
        return dateFm.format(date);
    }


    public static Drawable getDrawable(int resId) {
        return ContextCompat.getDrawable(RootApplication.getInstance(), resId);
//        return getResource().getDrawable(resId);
    }

    public static int getColor(int resId) {
        return getResource().getColor(resId);
    }

    public static Resources getResource() {
        return RootApplication.getInstance().getResources();
    }

    public static String[] getStringArray(int resId) {
        return getResource().getStringArray(resId);
    }

    public static String getString(int resId) {
        return getResource().getString(resId);
    }

    public static float getDimens(int resId) {
        return getResource().getDimension(resId);
    }

    public static void removeSelfFromParent(View child) {

        if (child != null) {
            ViewParent parent = child.getParent();
            if (parent instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) parent;
                group.removeView(child);
            }
        }
    }

}
