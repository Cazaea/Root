package i.am.lucky.base;

import android.arch.lifecycle.ViewModel;

/**
 * @author Cazaea
 * @data 2018/5/12
 * @Description 有列表的页面
 */

public class BaseListViewModel extends ViewModel {

    public int mPage = 0;

    public int getPage() {
        return mPage;
    }

    public void setPage(int mPage) {
        this.mPage = mPage;
    }
}
