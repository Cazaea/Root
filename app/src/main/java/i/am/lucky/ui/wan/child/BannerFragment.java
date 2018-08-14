package i.am.lucky.ui.wan.child;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import i.am.lucky.MainActivity;
import i.am.lucky.R;
import i.am.lucky.adapter.WanAndroidAdapter;
import i.am.lucky.base.BaseFragment;
import i.am.lucky.bean.wanandroid.HomeListBean;
import i.am.lucky.bean.wanandroid.WanAndroidBannerBean;
import i.am.lucky.databinding.FragmentWanAndroidBinding;
import i.am.lucky.databinding.HeaderWanAndroidBinding;
import i.am.lucky.utils.CommonUtils;
import i.am.lucky.utils.GlideImageLoader;
import i.am.lucky.view.webview.WebViewActivity;
import i.am.lucky.viewmodel.wan.WanAndroidListViewModel;
import i.am.lucky.viewmodel.wan.WanNavigator;

import java.util.ArrayList;
import java.util.List;

import i.am.lucky.recycler.XRecyclerView;

/**
 * @author Cazaea
 *         Updated by Cazaea on 18/02/07.
 */
public class BannerFragment extends BaseFragment<FragmentWanAndroidBinding> implements WanNavigator.BannerNavigator, WanNavigator.ArticleListNavigator {

    private static final String TYPE = "param1";
    private String mType = "综合";
    private boolean mIsPrepared;
    private boolean mIsFirst = true;
    private MainActivity activity;
    private WanAndroidAdapter mAdapter;
    private HeaderWanAndroidBinding androidBinding;
    private WanAndroidListViewModel viewModel;

    @Override
    public int setContent() {
        return R.layout.fragment_wan_android;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    public static BannerFragment newInstance(String param1) {
        BannerFragment fragment = new BannerFragment();
        Bundle args = new Bundle();
        args.putString(TYPE, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getString(TYPE);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showContentView();
        viewModel = new WanAndroidListViewModel(this);
        viewModel.setNavigator(this);
        viewModel.setArticleListNavigator(this);
        initRefreshView();

        // 准备就绪
        mIsPrepared = true;
        /**
         * 因为启动时先走loadData()再走onActivityCreated，
         * 所以此处要额外调用load(),不然最初不会加载内容
         */
        loadData();
    }

    private void initRefreshView() {
        bindingView.srlBook.setColorSchemeColors(CommonUtils.getColor(R.color.colorDefaultTheme));
        bindingView.srlBook.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                bindingView.srlBook.postDelayed(() -> {
                    viewModel.setPage(0);
                    loadCustomData();
                }, 1000);

            }
        });
        bindingView.xrvBook.setLayoutManager(new LinearLayoutManager(getActivity()));
        bindingView.xrvBook.setPullRefreshEnabled(false);
        bindingView.xrvBook.clearHeader();
        mAdapter = new WanAndroidAdapter(getActivity());
        bindingView.xrvBook.setAdapter(mAdapter);
        androidBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.header_wan_android, null, false);
        viewModel.getWanAndroidBanner();
        bindingView.xrvBook.addHeaderView(androidBinding.getRoot());

        bindingView.xrvBook.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                int page = viewModel.getPage();
                viewModel.setPage(++page);
                loadCustomData();
            }
        });
    }

    /**
     * 设置banner图
     */
    @Override
    public void showBannerView(ArrayList<String> bannerImages, ArrayList<String> mBannerTitle, List<WanAndroidBannerBean.DataBean> result) {
        androidBinding.banner.setVisibility(View.VISIBLE);
        androidBinding.banner.setBannerTitles(mBannerTitle);
        androidBinding.banner.setImages(bannerImages).setImageLoader(new GlideImageLoader()).start();
        androidBinding.banner.setOnBannerListener(position -> {
            if (result.get(position) != null && !TextUtils.isEmpty(result.get(position).getUrl())) {
                WebViewActivity.loadUrl(getContext(), result.get(position).getUrl(), result.get(position).getTitle());
            }
        });
    }

    @Override
    public void loadBannerFailure() {
        androidBinding.banner.setVisibility(View.GONE);
    }

    @Override
    public void loadHomeListFailure() {
        showContentView();
        if (bindingView.srlBook.isRefreshing()) {
            bindingView.srlBook.setRefreshing(false);
        }
        if (viewModel.getPage() == 0) {
            showError();
        } else {
            bindingView.xrvBook.refreshComplete();
        }
    }

    @Override
    public void showAdapterView(HomeListBean bean) {
        if (viewModel.getPage() == 0) {
            mAdapter.clear();
        }
        mAdapter.addAll(bean.getData().getDatas());
        mAdapter.notifyDataSetChanged();
        bindingView.xrvBook.refreshComplete();

        if (viewModel.getPage() == 0) {
            mIsFirst = false;
        }
    }

    @Override
    public void showListNoMoreLoading() {
        bindingView.xrvBook.noMoreLoading();
    }

    @Override
    public void showLoadSuccessView() {
        showContentView();
        bindingView.srlBook.setRefreshing(false);
    }

    @Override
    protected void loadData() {
        if (!mIsPrepared || !mIsVisible || !mIsFirst) {
            return;
        }

        bindingView.srlBook.setRefreshing(true);
        bindingView.srlBook.postDelayed(this::loadCustomData, 500);
    }

    @Override
    protected void onInvisible() {
        // 不可见时轮播图停止滚动
        if (androidBinding != null && androidBinding.banner != null) {
            androidBinding.banner.stopAutoPlay();
        }
    }

    private void loadCustomData() {
        viewModel.getHomeList(null);
    }

    @Override
    protected void onRefresh() {
        bindingView.srlBook.setRefreshing(true);
        loadCustomData();
    }
}
