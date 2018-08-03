package i.am.lucky.ui.wan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import i.am.lucky.R;
import i.am.lucky.base.BaseFragment;
import i.am.lucky.databinding.FragmentBookBinding;
import i.am.lucky.ui.wan.child.BannerFragment;
import i.am.lucky.ui.wan.child.BookListFragment;
import i.am.lucky.ui.wan.child.JokeFragment;
import i.am.lucky.view.MyFragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Cazaea on 16/12/14.
 * 展示玩安卓的页面
 */
public class WanFragment extends BaseFragment<FragmentBookBinding> {

    private ArrayList<String> mTitleList = new ArrayList<>(3);
    private ArrayList<Fragment> mFragments = new ArrayList<>(3);

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        showLoading();
        initFragmentList();
        /**
         * 注意使用的是：getChildFragmentManager，
         * 这样setOffscreenPageLimit()就可以添加上，保留相邻2个实例，切换时不会卡
         * 但会内存溢出，在显示时加载数据
         */
        MyFragmentPagerAdapter myAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(), mFragments, mTitleList);
        bindingView.vpBook.setAdapter(myAdapter);
        // 左右预加载页面的个数
        bindingView.vpBook.setOffscreenPageLimit(2);
        myAdapter.notifyDataSetChanged();
        bindingView.tabBook.setTabMode(TabLayout.MODE_FIXED);
        bindingView.tabBook.setupWithViewPager(bindingView.vpBook);
        showContentView();
    }

    @Override
    public int setContent() {
        return R.layout.fragment_book;
    }

    private void initFragmentList() {
        mTitleList.clear();
        mTitleList.add("玩安卓");
        mTitleList.add("书籍");
        mTitleList.add("段子");
        mFragments.add(BannerFragment.newInstance("玩安卓"));
        mFragments.add(BookListFragment.newInstance("心理学"));
        mFragments.add(JokeFragment.newInstance("段子"));
    }
}
