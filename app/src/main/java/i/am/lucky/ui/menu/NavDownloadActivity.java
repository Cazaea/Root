package i.am.lucky.ui.menu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import i.am.lucky.R;
import i.am.lucky.base.BaseActivity;
import i.am.lucky.databinding.ActivityNavDownloadBinding;
import i.am.lucky.utils.PerfectClickListener;
import i.am.lucky.utils.QRCodeUtil;
import i.am.lucky.utils.ShareUtils;

public class NavDownloadActivity extends BaseActivity<ActivityNavDownloadBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_download);
        showContentView();

        setTitle("扫码下载");
        String url = "https://fir.im/cloudreader";
        QRCodeUtil.showThreadImage(this, url, bindingView.ivErweima, R.drawable.ic_cloudreader_mip);
        bindingView.tvShare.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                ShareUtils.share(v.getContext(), R.string.string_share_text);
            }
        });
    }

    public static void start(Context mContext) {
        Intent intent = new Intent(mContext, NavDownloadActivity.class);
        mContext.startActivity(intent);
    }
}
