package i.am.lucky.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import i.am.lucky.R;
import i.am.lucky.base.baseadapter.BaseRecyclerViewAdapter;
import i.am.lucky.base.baseadapter.BaseRecyclerViewHolder;
import i.am.lucky.bean.wanandroid.HomeListBean;
import i.am.lucky.data.UserUtil;
import i.am.lucky.data.model.CollectModel;
import i.am.lucky.databinding.ItemWanAndroidBinding;
import i.am.lucky.ui.wan.child.ArticleListActivity;
import i.am.lucky.utils.DebugUtil;
import i.am.lucky.utils.PerfectClickListener;
import i.am.lucky.utils.ToastUtil;
import i.am.lucky.view.webview.WebViewActivity;
import i.am.lucky.viewmodel.wan.WanNavigator;

/**
 * Created by Cazaea on 2016/11/25.
 */

public class WanAndroidAdapter extends BaseRecyclerViewAdapter<HomeListBean.DataBean.DatasBean> {

    private Activity activity;
    private CollectModel model;
    /**
     * 是我的收藏页进来的，全部是收藏状态。bean里面没有返回isCollect信息
     */
    public boolean isCollectList = false;
    /**
     * 不显示类别信息
     */
    public boolean isNoShowChapterName = false;

    public WanAndroidAdapter(Activity activity) {
        this.activity = activity;
        model = new CollectModel();
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_wan_android);
    }

    public void setCollectList() {
        this.isCollectList = true;
    }

    public void setNoShowChapterName() {
        this.isNoShowChapterName = true;
    }

    private class ViewHolder extends BaseRecyclerViewHolder<HomeListBean.DataBean.DatasBean, ItemWanAndroidBinding> {

        ViewHolder(ViewGroup context, int layoutId) {
            super(context, layoutId);
        }

        @Override
        public void onBindViewHolder(final HomeListBean.DataBean.DatasBean bean, final int position) {
            if (bean != null) {
                binding.setBean(bean);
                binding.setAdapter(WanAndroidAdapter.this);
                binding.executePendingBindings();

                binding.vbCollect.setOnClickListener(new PerfectClickListener() {
                    @Override
                    protected void onNoDoubleClick(View v) {
                        if (UserUtil.isLogin(activity)) {
                            // 为什么状态值相反？因为点了之后控件已改变状态
                            DebugUtil.error("-----binding.vbCollect.isChecked():" + binding.vbCollect.isChecked());
                            if (!binding.vbCollect.isChecked()) {
                                model.unCollect(isCollectList, bean.getId(), bean.getOriginId(), new WanNavigator.OnCollectNavigator() {
                                    @Override
                                    public void onSuccess() {
                                        if (isCollectList) {

                                            int indexOf = getData().indexOf(bean);
                                            // 角标始终加一
                                            int adapterPosition = getAdapterPosition();

                                            DebugUtil.error("getAdapterPosition():" + getAdapterPosition());
                                            DebugUtil.error("indexOf:" + indexOf);
                                            // 移除数据增加删除动画
                                            getData().remove(indexOf);
                                            notifyItemRemoved(adapterPosition);
                                        } else {
                                            bean.setCollect(binding.vbCollect.isChecked());
                                        }
                                    }

                                    @Override
                                    public void onFailure() {
                                        bean.setCollect(true);
                                        notifyItemChanged(getAdapterPosition());
                                        ToastUtil.showToastLong("取消收藏失败");
                                    }
                                });
                            } else {
                                model.collect(bean.getId(), new WanNavigator.OnCollectNavigator() {
                                    @Override
                                    public void onSuccess() {
                                        bean.setCollect(true);
                                    }

                                    @Override
                                    public void onFailure() {
                                        ToastUtil.showToastLong("收藏失败");
                                        bean.setCollect(false);
                                        notifyItemChanged(getAdapterPosition());
                                    }
                                });
                            }
                        } else {
                            bean.setCollect(false);
                            notifyItemChanged(getAdapterPosition());
                        }
                    }
                });
            }
        }
    }

    public void openDetail(HomeListBean.DataBean.DatasBean bean) {
        WebViewActivity.loadUrl(activity, bean.getLink(), bean.getTitle());
    }

    public void openArticleList(HomeListBean.DataBean.DatasBean bean) {
        ArticleListActivity.start(activity, bean.getChapterId(), bean.getChapterName());
    }
}
