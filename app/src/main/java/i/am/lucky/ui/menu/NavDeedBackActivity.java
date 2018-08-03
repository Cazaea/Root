package i.am.lucky.ui.menu;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import i.am.lucky.R;
import i.am.lucky.base.BaseActivity;
import i.am.lucky.databinding.ActivityNavDeedBackBinding;
import i.am.lucky.utils.BaseTools;
import i.am.lucky.utils.CommonUtils;
import i.am.lucky.utils.PerfectClickListener;
import i.am.lucky.utils.ToastUtil;
import i.am.lucky.view.webview.WebViewActivity;

import java.util.List;

/**
 * @author Cazaea
 */
public class NavDeedBackActivity extends BaseActivity<ActivityNavDeedBackBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_deed_back);
        setTitle("问题反馈");
        showContentView();

        bindingView.tvIssues.setOnClickListener(listener);
        bindingView.tvJianshu.setOnClickListener(listener);
        bindingView.tvQq.setOnClickListener(listener);
        bindingView.tvEmail.setOnClickListener(listener);
        bindingView.tvFaq.setOnClickListener(listener);
    }

    private PerfectClickListener listener = new PerfectClickListener() {
        @Override
        protected void onNoDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.tv_issues:
                    WebViewActivity.loadUrl(v.getContext(), CommonUtils.getString(R.string.string_url_issues), "Issues");
                    break;
                case R.id.tv_qq:
                    if (BaseTools.isApplicationAvilible(NavDeedBackActivity.this, "com.tencent.mobileqq")) {
                        String url = "mqqwpa://im/chat?chat_type=wpa&uin=770413277";
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    } else {
                        ToastUtil.showToastLong("先安装一个QQ吧..");
                    }
                    break;
                case R.id.tv_email:
                    Intent data = new Intent(Intent.ACTION_SENDTO);
                    data.setData(Uri.parse("mailto:wistorm@163.com"));
                    startActivity(data);
                    break;
                case R.id.tv_jianshu:
                    WebViewActivity.loadUrl(v.getContext(), CommonUtils.getString(R.string.string_url_jianshu), "简书");
                    break;
                case R.id.tv_faq:
                    WebViewActivity.loadUrl(v.getContext(), CommonUtils.getString(R.string.string_url_faq), "常见问题归纳");
                    break;

                default:
                    break;
            }
        }
    };

    /**
     * 判断qq是否可用
     *
     * @param context
     * @return
     */
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void start(Context mContext) {
        Intent intent = new Intent(mContext, NavDeedBackActivity.class);
        mContext.startActivity(intent);
    }
}
