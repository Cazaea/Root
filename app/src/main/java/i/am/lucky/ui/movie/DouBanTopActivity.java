package i.am.lucky.ui.movie;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import i.am.lucky.R;
import i.am.lucky.adapter.DouBanTopAdapter;
import i.am.lucky.base.BaseActivity;
import i.am.lucky.bean.HotMovieBean;
import i.am.lucky.databinding.ActivityDoubanTopBinding;
import i.am.lucky.viewmodel.movie.DouBanTopViewModel;
import i.am.lucky.viewmodel.movie.OnMovieLoadListener;

import i.am.lucky.recycler.XRecyclerView;

/**
 * Created by Cazaea on 16/12/10.
 * Updated by Cazaea on 17/12/26.
 */
public class DouBanTopActivity extends BaseActivity<ActivityDoubanTopBinding> implements OnMovieLoadListener {

    private int mStart = 0;
    private int mCount = 21;
    private DouBanTopAdapter mDouBanTopAdapter;
    private DouBanTopViewModel topViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_douban_top);
        setTitle("豆瓣电影Top250");
        topViewModel = ViewModelProviders.of(this).get(DouBanTopViewModel.class);
        topViewModel.setOnMovieLoadListener(this);
        initRecyclerView();
        loadDouBanTop250();
    }

    private void loadDouBanTop250() {
        topViewModel.getHotMovie(mStart, mCount);
    }

    @Override
    public void onSuccess(HotMovieBean hotMovieBean) {
        showContentView();
        if (mStart == 0) {
            if (hotMovieBean != null
                    && hotMovieBean.getSubjects() != null
                    && hotMovieBean.getSubjects().size() > 0) {

                mDouBanTopAdapter.clear();
                mDouBanTopAdapter.addAll(hotMovieBean.getSubjects());
                mDouBanTopAdapter.notifyDataSetChanged();
            } else {
                bindingView.xrvTop.setVisibility(View.GONE);
                bindingView.xrvTop.refreshComplete();
                if (mDouBanTopAdapter.getItemCount() == 0) {
                    showError();
                }
            }
        } else {
            if (hotMovieBean != null
                    && hotMovieBean.getSubjects() != null
                    && hotMovieBean.getSubjects().size() > 0) {
                bindingView.xrvTop.refreshComplete();
                mDouBanTopAdapter.addAll(hotMovieBean.getSubjects());
                mDouBanTopAdapter.notifyDataSetChanged();
            } else {
                bindingView.xrvTop.noMoreLoading();
            }
        }
    }

    @Override
    public void onFailure() {
        bindingView.xrvTop.refreshComplete();
        if (mDouBanTopAdapter.getItemCount() == 0) {
            showError();
        }
    }

    private void initRecyclerView() {
        mDouBanTopAdapter = new DouBanTopAdapter(DouBanTopActivity.this);
        bindingView.xrvTop.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        bindingView.xrvTop.setPullRefreshEnabled(false);
        bindingView.xrvTop.clearHeader();
        bindingView.xrvTop.setLoadingMoreEnabled(true);
        bindingView.xrvTop.setAdapter(mDouBanTopAdapter);
        bindingView.xrvTop.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
            }

            @Override
            public void onLoadMore() {
                mStart += mCount;
                loadDouBanTop250();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        topViewModel.onDestroy();
    }

    @Override
    protected void onRefresh() {
        loadDouBanTop250();
    }

    public static void start(Context mContext) {
        Intent intent = new Intent(mContext, DouBanTopActivity.class);
        mContext.startActivity(intent);
    }
}