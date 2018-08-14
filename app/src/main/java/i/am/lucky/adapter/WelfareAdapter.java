package i.am.lucky.adapter;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import i.am.lucky.R;
import i.am.lucky.base.baseadapter.BaseRecyclerViewAdapter;
import i.am.lucky.base.baseadapter.BaseRecyclerViewHolder;
import i.am.lucky.bean.GankIoDataBean;
import i.am.lucky.databinding.ItemWelfareBinding;
import i.am.lucky.utils.DensityUtil;

/**
 * Created by Cazaea on 2016/12/1.
 */

public class WelfareAdapter extends BaseRecyclerViewAdapter<GankIoDataBean.ResultBean> {

    @NonNull
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_welfare);
    }

    private class ViewHolder extends BaseRecyclerViewHolder<GankIoDataBean.ResultBean, ItemWelfareBinding> {


        ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @Override
        public void onBindViewHolder(final GankIoDataBean.ResultBean resultsBean, final int position) {
            /**
             * 注意：DensityUtil.setViewMargin(itemView,true,5,3,5,0);
             * 如果这样使用，则每个item的左右边距是不一样的，
             * 这样item不能复用，所以下拉刷新成功后显示会闪一下
             * 换成每个item设置上下左右边距是一样的话，系统就会复用，就消除了图片不能复用 闪跳的情况
             */
            if (position % 2 == 0) {
                DensityUtil.setViewMargin(itemView, false, 12, 6, 12, 0);
            } else {
                DensityUtil.setViewMargin(itemView, false, 6, 12, 12, 0);
            }
            binding.setBean(resultsBean);
            // TODO 仿抖动
            binding.executePendingBindings();
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onClick(resultsBean, position);
                }
            });
        }
    }
}