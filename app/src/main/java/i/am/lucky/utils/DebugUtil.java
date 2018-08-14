package i.am.lucky.utils;

import android.content.Context;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

/**
 * @author Cazaea
 * @Description 在代码中要打印log, 就直接DebugUtil.debug(....).
 * 然后如果发布的时候,就直接把这个类的DEBUG 改成false,这样所有的log就不会再打印在控制台.
 */
public class DebugUtil {

    public static final String TAG = "Cazaea";
    public static final boolean DEBUG = true;

    public static void toast(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

    public static void debug(String msg) {
        if (DEBUG) {
            Logger.d(TAG, msg);
        }
    }

    public static void debug(String tag, String msg) {
        if (DEBUG) {
            Logger.t(tag);
            Logger.d(msg);
        }
    }

    public static void error(String error) {
        if (DEBUG) {
            Logger.e(TAG, error);
        }
    }

    public static void error(String tag, String error) {
        if (DEBUG) {
            Logger.t(tag);
            Logger.e(error);
        }
    }
}