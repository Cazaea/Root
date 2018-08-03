package i.am.lucky.http;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

import java.util.Objects;
import java.util.UUID;

/**
 * Created by Cazaea on 2017/2/14.
 */

class HttpHead {
    private static final String CLIENT_TYPE = "4";

    private static Context context;

    public static void init(Context context) {
        HttpHead.context = context;
    }

    public static String getHeader(String httpMethod) {
        long sen = System.currentTimeMillis() / 1000;
        String h = getUuid() + getDevice() + CLIENT_TYPE + httpMethod + "0" + getVersion() + sen;
        return h;
    }

    /**
     * * 获取版本号
     * * @return 当前应用的版本号
     */
    private static String getVersion() {
        PackageManager manager = context.getPackageManager();
        PackageInfo info;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
        return info.versionName;
    }

    @SuppressLint({"MissingPermission", "HardwareIds"})
    private static String getUuid() {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String tmDevice, tmSerial, androidId;
        tmDevice = Objects.requireNonNull(tm).getDeviceId();
        tmSerial = "ANDROID_ID";
        androidId = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        return deviceUuid.toString();

    }

    /**
     * 获取机型
     */
    private static String getDevice() {
        String s = android.os.Build.MODEL;
        String t = s.replaceAll(" ", "");
        return t;
    }
}
