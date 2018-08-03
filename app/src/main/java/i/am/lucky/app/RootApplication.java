package i.am.lucky.app;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.multidex.MultiDexApplication;

import i.am.lucky.http.HttpUtils;
import i.am.lucky.utils.DebugUtil;

import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.crashreport.CrashReport;

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
        HttpUtils.getInstance().init(this, DebugUtil.DEBUG);
        LeakCanary.install(this);
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
