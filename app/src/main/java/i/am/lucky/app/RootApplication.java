package i.am.lucky.app;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.multidex.MultiDexApplication;

import i.am.lucky.utils.DebugUtil;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.crashreport.CrashReport;

import i.am.lucky.http.HttpUtils;

/**
 * Created by Cazaea on 2016/11/22.
 *
 * @description Inherit MultiDexApplication to implement subcontract processing
 */

public class RootApplication extends MultiDexApplication {

    private static RootApplication sRootApplication;

    public static RootApplication getInstance() {
        return sRootApplication;
    }

    @SuppressWarnings("unused")
    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        sRootApplication = this;
        // Logger工具初始化
        Logger.addLogAdapter(new AndroidLogAdapter());
        // 初始化网络工具
        HttpUtils.getInstance().init(this, DebugUtil.DEBUG);
        // 加载内存优化检查工具
        LeakCanary.install(this);
        // 初始化Bugly强制更新
        CrashReport.initCrashReport(getApplicationContext(), "3977b2d86f", DebugUtil.DEBUG);

        initTextSize();
    }

    /**
     * Make its system change the font size invalid
     */
    private void initTextSize() {
        Resources res = getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
    }

}
