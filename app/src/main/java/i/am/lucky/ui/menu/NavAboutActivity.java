package i.am.lucky.ui.menu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import i.am.lucky.R;
import i.am.lucky.base.BaseActivity;
import i.am.lucky.databinding.ActivityNavAboutBinding;
import i.am.lucky.utils.BaseTools;
import i.am.lucky.utils.CommonUtils;
import i.am.lucky.utils.PerfectClickListener;
import i.am.lucky.utils.UpdateUtil;
import i.am.lucky.view.webview.WebViewActivity;

/**
 * @author Cazaea
 */
public class NavAboutActivity extends BaseActivity<ActivityNavAboutBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_about);
        showContentView();
        setTitle("关于云阅");
        bindingView.tvVersionName.setText("当前版本 V" + BaseTools.getVersionName());


        // 直接写在布局文件里会很耗内存
        Glide.with(this).load(R.drawable.ic_cloudreader).into(bindingView.ivIcon);
        bindingView.tvGankio.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        bindingView.tvGankio.getPaint().setAntiAlias(true);//抗锯齿
        bindingView.tvDouban.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        bindingView.tvDouban.getPaint().setAntiAlias(true);//抗锯齿

        initListener();
    }

    private void initListener() {
        bindingView.tvGankio.setOnClickListener(listener);
        bindingView.tvDouban.setOnClickListener(listener);
        bindingView.tvAboutStar.setOnClickListener(listener);
        bindingView.tvFunction.setOnClickListener(listener);
//        bindingView.tvNewVersion.setOnClickListener(listener);
    }

    private PerfectClickListener listener = new PerfectClickListener() {
        @Override
        protected void onNoDoubleClick(View v) {
            String url = null;
            String title = "加载中...";
            switch (v.getId()) {
                case R.id.tv_gankio:
                    url = CommonUtils.getString(R.string.string_url_gankio);
                    title = "干货集中营";
                    break;
                case R.id.tv_douban:
                    url = CommonUtils.getString(R.string.string_url_douban);
                    title = "豆瓣开发者服务使用条款";
                    break;
                case R.id.tv_about_star:
                    url = CommonUtils.getString(R.string.string_url_cloudreader);
                    title = "CloudReader";
                    break;
                case R.id.tv_function:// 更新日志
                    url = CommonUtils.getString(R.string.string_url_update_log);
                    title = "更新日志";
                    break;
                case R.id.tv_new_version:// 检查更新
                    url = CommonUtils.getString(R.string.string_url_new_version);
                    title = "检查更新";
                    break;
                default:
                    break;
            }
            WebViewActivity.loadUrl(v.getContext(), url, title);
        }
    };

    public static void start(Context mContext) {
        Intent intent = new Intent(mContext, NavAboutActivity.class);
        mContext.startActivity(intent);
    }

    public void checkUpdate(View view) {
        UpdateUtil.check(this,true);
    }
}
