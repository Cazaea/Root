package i.am.lucky.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import i.am.lucky.R;
import i.am.lucky.base.baseadapter.BaseRecyclerViewAdapter;
import i.am.lucky.base.baseadapter.BaseRecyclerViewHolder;
import i.am.lucky.bean.moviechild.SubjectsBean;
import i.am.lucky.databinding.ItemDoubanTopBinding;
import i.am.lucky.ui.movie.OneMovieDetailActivity;
import i.am.lucky.utils.DialogBuild;
import i.am.lucky.utils.PerfectClickListener;

/**
 * Created by Cazaea on 2016/12/10.
 */

public class DouBanTopAdapter extends BaseRecyclerViewAdapter<SubjectsBean> {


    private Activity activity;

    public DouBanTopAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_douban_top);
    }

    class ViewHolder extends BaseRecyclerViewHolder<SubjectsBean, ItemDoubanTopBinding> {

        ViewHolder(ViewGroup parent, int layout) {
            super(parent, layout);
        }

        @Override
        public void onBindViewHolder(final SubjectsBean bean, final int position) {
            binding.setBean(bean);
            /**
             * 当数据改变时，binding会在下一帧去改变数据，如果我们需要立即改变，就去调用executePendingBindings方法。
             */
            binding.executePendingBindings();
            binding.llItemTop.setOnClickListener(new PerfectClickListener() {
                @Override
                protected void onNoDoubleClick(View v) {
                    OneMovieDetailActivity.start(activity, bean, binding.ivTopPhoto);
                }
            });
            binding.llItemTop.setOnLongClickListener(v -> {
                String title = "Top" + (position + 1) + ": " + bean.getTitle();
                DialogBuild.show(v, title, (dialog, which) -> OneMovieDetailActivity.start(activity, bean, binding.ivTopPhoto));
                return false;
            });
        }
    }
}
