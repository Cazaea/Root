package i.am.lucky.ui.gank.child;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import i.am.lucky.MainActivity;
import i.am.lucky.R;
import i.am.lucky.adapter.GankAndroidAdapter;
import i.am.lucky.base.BaseFragment;
import i.am.lucky.bean.GankIoDataBean;
import i.am.lucky.databinding.FragmentAndroidBinding;
import i.am.lucky.viewmodel.gank.BigAndroidNavigator;
import i.am.lucky.viewmodel.gank.BigAndroidViewModel;
import i.am.lucky.recycler.XRecyclerView;

/**
 * 大安卓 fragment
 */
public class AndroidFragment extends BaseFragment<FragmentAndroidBinding> implements BigAndroidNavigator {

    private static final String TAG = "AndroidFragment";
    private static final String TYPE = "mType";
    private String mType = "Android";
    private boolean mIsPrepared;
    private boolean mIsFirst = true;
    private GankAndroidAdapter mGankAndroidAdapter;
    private MainActivity activity;
    private BigAndroidViewModel viewModel;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    public static AndroidFragment newInstance(String type) {
        AndroidFragment fragment = new AndroidFragment();
        Bundle args = new Bundle();
        args.putString(TYPE, type);
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
    public int setContent() {
        return R.layout.fragment_android;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = new BigAndroidViewModel(this, mType);
        viewModel.setBigAndroidNavigator(this);
        initRecyclerView();
        // 准备就绪
        mIsPrepared = true;
    }

    @Override
    protected void loadData() {
        if (!mIsPrepared || !mIsVisible || !mIsFirst) {
            return;
        }
        viewModel.loadAndroidData();
    }

    /**
     * 设置adapter
     */
    private void setAdapter(GankIoDataBean mAndroidBean) {
        mGankAndroidAdapter.clear();
        mGankAndroidAdapter.addAll(mAndroidBean.getResults());
        mGankAndroidAdapter.notifyDataSetChanged();
        bindingView.xrvAndroid.refreshComplete();

        mIsFirst = false;
    }

    private void initRecyclerView() {
        mGankAndroidAdapter = new GankAndroidAdapter(activity);
        bindingView.xrvAndroid.setLayoutManager(new LinearLayoutManager(getActivity()));
        bindingView.xrvAndroid.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                viewModel.setPage(1);
                viewModel.loadAndroidData();
            }

            @Override
            public void onLoadMore() {
                int page = viewModel.getPage();
                page++;
                viewModel.setPage(page);
                viewModel.loadAndroidData();
            }
        });
        bindingView.xrvAndroid.setAdapter(mGankAndroidAdapter);
    }

    /**
     * 加载失败后点击后的操作
     */
    @Override
    protected void onRefresh() {
        viewModel.loadAndroidData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void showLoadSuccessView() {
        showContentView();
    }

    @Override
    public void showAdapterView(GankIoDataBean gankIoDataBean) {
        setAdapter(gankIoDataBean);
    }

    @Override
    public void refreshAdapter(GankIoDataBean gankIoDataBean) {
        bindingView.xrvAndroid.refreshComplete();
        mGankAndroidAdapter.addAll(gankIoDataBean.getResults());
        mGankAndroidAdapter.notifyDataSetChanged();
    }

    @Override
    public void showListNoMoreLoading() {
        bindingView.xrvAndroid.noMoreLoading();
    }

    @Override
    public void showLoadFailedView() {
        bindingView.xrvAndroid.refreshComplete();
        // 注意：这里不能写成 mPage == 1，否则会一直显示错误页面
        if (mGankAndroidAdapter.getItemCount() == 0) {
            showError();
        }
    }
}
