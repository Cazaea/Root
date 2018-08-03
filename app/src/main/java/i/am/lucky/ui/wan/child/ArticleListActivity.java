package i.am.lucky.ui.wan.child;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import i.am.lucky.R;
import i.am.lucky.adapter.WanAndroidAdapter;
import i.am.lucky.base.BaseActivity;
import i.am.lucky.base.BaseListViewModel;
import i.am.lucky.bean.wanandroid.HomeListBean;
import i.am.lucky.databinding.FragmentWanAndroidBinding;
import i.am.lucky.utils.CommonUtils;
import i.am.lucky.viewmodel.wan.ArticleListListViewModel;
import i.am.lucky.viewmodel.wan.WanAndroidListViewModel;
import i.am.lucky.viewmodel.wan.WanNavigator;
import i.am.lucky.recycler.XRecyclerView;

/**
 * 玩安卓文章列表
 *
 * @author Cazaea
 */
public class ArticleListActivity extends BaseActivity<FragmentWanAndroidBinding> implements WanNavigator.ArticleListNavigator {

    private ArticleListListViewModel viewModel;
    private WanAndroidListViewModel androidViewModel;
    private WanAndroidAdapter mAdapter;
    private int cid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_wan_android);
        initRefreshView();
        getIntentData();
        loadData();
    }

    private void getIntentData() {
        cid = getIntent().getIntExtra("cid", 0);
        String chapterName = getIntent().getStringExtra("chapterName");

        if (cid != 0) {
            setTitle(chapterName);
            androidViewModel = new WanAndroidListViewModel();
            androidViewModel.setArticleListNavigator(this);
            mAdapter.setNoShowChapterName();
        } else {
            setTitle("我的收藏");
            viewModel = new ArticleListListViewModel(this, this);
            mAdapter.setCollectList();
        }
    }

    private void loadData() {
        if (cid != 0) {
            androidViewModel.getHomeList(cid);
        } else {
            viewModel.getCollectList();
        }
    }

    private BaseListViewModel getViewModel() {
        if (viewModel != null) {
            return viewModel;
        } else {
            return androidViewModel;
        }
    }

    private void initRefreshView() {
        bindingView.srlBook.setColorSchemeColors(CommonUtils.getColor(R.color.colorDefaultTheme));
        bindingView.xrvBook.setLayoutManager(new LinearLayoutManager(this));
        bindingView.xrvBook.setPullRefreshEnabled(false);
        bindingView.xrvBook.clearHeader();
        mAdapter = new WanAndroidAdapter(this);
        bindingView.xrvBook.setAdapter(mAdapter);
        bindingView.srlBook.setOnRefreshListener(() -> bindingView.srlBook.postDelayed(() -> {
            getViewModel().setPage(0);
            loadData();
        }, 500));
        bindingView.xrvBook.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                int page = getViewModel().getPage();
                getViewModel().setPage(++page);
                loadData();
            }
        });
    }

    @Override
    public void loadHomeListFailure() {
        showContentView();
        if (bindingView.srlBook.isRefreshing()) {
            bindingView.srlBook.setRefreshing(false);
        }
        if (getViewModel().getPage() == 0) {
            showError();
        } else {
            bindingView.xrvBook.refreshComplete();
        }
    }

    @Override
    public void showAdapterView(HomeListBean bean) {
        if (getViewModel().getPage() == 0) {
            mAdapter.clear();
        }
        mAdapter.addAll(bean.getData().getDatas());
        mAdapter.notifyDataSetChanged();
        bindingView.xrvBook.refreshComplete();
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
    protected void onRefresh() {
        super.onRefresh();
        loadData();
    }

    public static void start(Context mContext) {
        Intent intent = new Intent(mContext, ArticleListActivity.class);
        mContext.startActivity(intent);
    }

    public static void start(Context mContext, int cid, String chapterName) {
        Intent intent = new Intent(mContext, ArticleListActivity.class);
        intent.putExtra("cid", cid);
        intent.putExtra("chapterName", chapterName);
        mContext.startActivity(intent);
    }
}
