package i.am.lucky.ui.movie;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import i.am.lucky.MainActivity;
import i.am.lucky.R;
import i.am.lucky.adapter.OneAdapter;
import i.am.lucky.app.Constants;
import i.am.lucky.base.BaseFragment;
import i.am.lucky.bean.HotMovieBean;
import i.am.lucky.databinding.FragmentOneBinding;
import i.am.lucky.databinding.HeaderItemOneBinding;
import i.am.lucky.http.cache.ACache;
import i.am.lucky.utils.SPUtils;
import i.am.lucky.utils.TimeUtil;
import i.am.lucky.viewmodel.movie.OneViewModel;

/**
 * @author Cazaea
 */
public class OneFragment extends BaseFragment<FragmentOneBinding> {

    // 初始化完成后加载数据
    private boolean isPrepared = false;
    // 第一次显示时加载数据，第二次不显示
    private boolean isFirst = true;
    // 是否正在刷新（用于刷新数据时返回页面不再刷新）
    private boolean mIsLoading = false;
    private ACache aCache;
    private MainActivity activity;
    private HotMovieBean mHotMovieBean;
    private OneAdapter oneAdapter;
    private OneViewModel oneViewModel;

    @Override
    public int setContent() {
        return R.layout.fragment_one;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRecyclerView();
        aCache = ACache.get(getActivity());
        mHotMovieBean = (HotMovieBean) aCache.getAsObject(Constants.ONE_HOT_MOVIE);
        oneViewModel = ViewModelProviders.of(this).get(OneViewModel.class);
        isPrepared = true;
    }

    /**
     * 懒加载
     * 从此页面新开activity界面返回此页面 不会走这里
     */
    @Override
    protected void loadData() {
//        DebugUtil.error("------OneFragment---loadData: ");

        if (!isPrepared || !mIsVisible) {
            return;
        }

        // 显示，准备完毕，不是当天，则请求数据（正在请求时避免再次请求）
        String oneData = SPUtils.getString("one_data", "2016-11-26");

        if (!oneData.equals(TimeUtil.getData()) && !mIsLoading) {
            showLoading();
            /**延迟执行防止卡顿*/
            postDelayLoad();
        } else {
            // 为了正在刷新时不执行这部分
            if (mIsLoading) {
                return;
            }
            if (!isFirst) {
                return;
            }

            showLoading();
            if (mHotMovieBean == null && !mIsLoading) {
                postDelayLoad();
            } else {
                // TODO 强行使用Lambda表达式
                bindingView.listOne.postDelayed(() -> {
                    synchronized (this) {
                        setAdapter(mHotMovieBean);
                        showContentView();
                    }
                }, 150);
            }
        }

    }

    private void loadHotMovie() {
        oneViewModel.getHotMovie().observe(this, hotMovieBean -> {
            showContentView();
            if (hotMovieBean != null && hotMovieBean.getSubjects() != null) {
                setAdapter(hotMovieBean);

                aCache.remove(Constants.ONE_HOT_MOVIE);
                aCache.put(Constants.ONE_HOT_MOVIE, hotMovieBean);
                // 保存请求的日期
                SPUtils.putString("one_data", TimeUtil.getData());
                // 刷新结束
                mIsLoading = false;
            } else {
                if (mHotMovieBean != null) {
                    setAdapter(mHotMovieBean);
                } else {
                    if (oneAdapter.getItemCount() == 0) {
                        showError();
                    }
                }
            }
        });
    }

    private void setAdapter(HotMovieBean hotMovieBean) {
        oneAdapter.clear();
        oneAdapter.addAll(hotMovieBean.getSubjects());
        oneAdapter.notifyDataSetChanged();

        isFirst = false;
    }

    private void initRecyclerView() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        bindingView.listOne.setLayoutManager(mLayoutManager);
        // 加上这两行代码，下拉出提示才不会产生出现刷新头的bug，不加拉不下来
        bindingView.listOne.setPullRefreshEnabled(false);
        bindingView.listOne.clearHeader();
        bindingView.listOne.setLoadingMoreEnabled(false);
        // 需加，不然滑动不流畅
        bindingView.listOne.setNestedScrollingEnabled(false);
        bindingView.listOne.setHasFixedSize(false);
        HeaderItemOneBinding oneBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.header_item_one, null, false);
        oneBinding.setView(this);
        bindingView.listOne.addHeaderView(oneBinding.getRoot());
        oneAdapter = new OneAdapter(activity);
        bindingView.listOne.setAdapter(oneAdapter);
    }

    /**
     * 延迟执行，避免卡顿
     * 加同步锁，避免重复加载
     */
    private void postDelayLoad() {
        synchronized (this) {
            if (!mIsLoading) {
                mIsLoading = true;
                bindingView.listOne.postDelayed(this::loadHotMovie, 150);
            }
        }
    }

    public void headerClick() {
        DouBanTopActivity.start(activity);
    }

    @Override
    protected void onRefresh() {
        loadHotMovie();
    }

    /**
     * 从此页面新开activity界面返回此页面 走这里
     */
    @Override
    public void onResume() {
        super.onResume();
    }
}