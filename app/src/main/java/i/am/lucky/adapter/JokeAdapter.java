package i.am.lucky.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import i.am.lucky.R;
import i.am.lucky.base.baseadapter.BaseRecyclerViewAdapter;
import i.am.lucky.base.baseadapter.BaseRecyclerViewHolder;
import i.am.lucky.bean.wanandroid.DuanZiBean;
import i.am.lucky.databinding.ItemJokeBinding;
import i.am.lucky.utils.DialogBuild;
import i.am.lucky.utils.TimeUtil;

/**
 * Created by Cazaea on 2016/11/25.
 */

public class JokeAdapter extends BaseRecyclerViewAdapter<DuanZiBean> {

    private Activity activity;

    public JokeAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_joke);
    }

    private class ViewHolder extends BaseRecyclerViewHolder<DuanZiBean, ItemJokeBinding> {

        ViewHolder(ViewGroup context, int layoutId) {
            super(context, layoutId);
        }

        @Override
        public void onBindViewHolder(final DuanZiBean bean, final int position) {
            if (bean != null) {
                binding.setBean(bean);
                binding.executePendingBindings();
                String time = TimeUtil.formatDataTime(Long.valueOf(bean.getCreateTime() + "000"));
                binding.setTime(time);
                binding.llItemTop.setOnLongClickListener(v -> {
                    DialogBuild.showItems(v,bean.getContent());
                    return false;
                });
            }
        }
    }
}
