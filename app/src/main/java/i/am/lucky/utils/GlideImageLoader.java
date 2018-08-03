package i.am.lucky.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import i.am.lucky.R;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by Cazaea on 2016/11/30.
 * 首页轮播图
 */

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object url, ImageView imageView) {
        Glide.with(context).load(url)
                .placeholder(R.drawable.img_two_bi_one)
                .error(R.drawable.img_two_bi_one)
                .crossFade(1000)
                .into(imageView);
    }
}
