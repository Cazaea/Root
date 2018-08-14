package i.am.lucky.ui.wan.child;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.view.inputmethod.EditorInfo;

import i.am.lucky.R;
import i.am.lucky.adapter.WanBookAdapter;
import i.am.lucky.base.BaseFragment;
import i.am.lucky.bean.book.BookBean;
import i.am.lucky.databinding.FragmentWanAndroidBinding;
import i.am.lucky.databinding.HeaderItemBookBinding;
import i.am.lucky.http.HttpClient;
import i.am.lucky.utils.CommonUtils;
import i.am.lucky.utils.DebugUtil;

import i.am.lucky.recycler.XRecyclerView;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Cazaea
 * Updated by Cazaea on 18/02/07.
 */
public class BookListFragment extends BaseFragment<FragmentWanAndroidBinding> {

    private static final String TYPE = "param1";
    private String mType = "综合";
    private boolean mIsPrepared;
    private boolean mIsFirst = true;
    // 开始请求的角标
    private int mStart = 0;
    // 一次请求的数量
    private int mCount = 18;
    private WanBookAdapter mBookAdapter;

    @Override
    public int setContent() {
        return R.layout.fragment_wan_android;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public static BookListFragment newInstance(String param1) {
        BookListFragment fragment = new BookListFragment();
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
        bindingView.srlBook.setOnRefreshListener(() -> bindingView.srlBook.postDelayed(() -> {
            mStart = 0;
            bindingView.xrvBook.reset();
            loadCustomData();
        }, 300));
        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
        bindingView.xrvBook.setLayoutManager(mLayoutManager);
        bindingView.xrvBook.setPullRefreshEnabled(false);
        bindingView.xrvBook.clearHeader();
        mBookAdapter = new WanBookAdapter(getActivity());
        bindingView.xrvBook.setAdapter(mBookAdapter);
        HeaderItemBookBinding oneBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.header_item_book, null, false);
        oneBinding.etTypeName.setText(mType);
        oneBinding.etTypeName.setSelection(mType.length());
        bindingView.xrvBook.addHeaderView(oneBinding.getRoot());
        /** 处理键盘搜索键 */
        oneBinding.etTypeName.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                mStart = 0;
                bindingView.xrvBook.reset();
                mType = oneBinding.etTypeName.getText().toString().trim();
                loadCustomData();
            }
            return false;
        });
        bindingView.xrvBook.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                mStart += mCount;
                loadCustomData();
            }
        });
    }

    @Override
    protected void loadData() {
        DebugUtil.error("-----loadData");
        if (!mIsPrepared || !mIsVisible || !mIsFirst) {
            return;
        }

        bindingView.srlBook.setRefreshing(true);
        bindingView.srlBook.postDelayed(this::loadCustomData, 500);
        DebugUtil.error("-----setRefreshing");
    }

    private void loadCustomData() {

        Subscription get = HttpClient.Builder.getDouBanService().getBook(mType, mStart, mCount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BookBean>() {
                    @Override
                    public void onCompleted() {
                        showContentView();
                        if (bindingView.srlBook.isRefreshing()) {
                            bindingView.srlBook.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showContentView();
                        if (bindingView.srlBook.isRefreshing()) {
                            bindingView.srlBook.setRefreshing(false);
                        }
                        if (mStart == 0) {
                            showError();
                        } else {
                            bindingView.xrvBook.refreshComplete();
                        }
                    }

                    @Override
                    public void onNext(BookBean bookBean) {
                        if (mStart == 0) {
                            if (bookBean != null && bookBean.getBooks() != null && bookBean.getBooks().size() > 0) {
                                mBookAdapter.clear();
                            } else {
                                showError();
                                return;
                            }
                            mIsFirst = false;
                        } else {
                            if (bookBean == null || bookBean.getBooks() == null || bookBean.getBooks().size() == 0) {
                                bindingView.xrvBook.noMoreLoading();
                                return;
                            }
                        }
                        mBookAdapter.addAll(bookBean.getBooks());
                        mBookAdapter.notifyDataSetChanged();
                        bindingView.xrvBook.refreshComplete();
                    }
                });
        addSubscription(get);
    }

    @Override
    protected void onRefresh() {
        bindingView.srlBook.setRefreshing(true);
        loadCustomData();
    }
}