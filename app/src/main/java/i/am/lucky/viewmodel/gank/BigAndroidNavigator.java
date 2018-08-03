package i.am.lucky.viewmodel.gank;

import i.am.lucky.bean.GankIoDataBean;

/**
 * @author Cazaea
 * @data 2018/1/16
 * @Description
 */

public interface BigAndroidNavigator {

    void showLoadSuccessView();

    /**
     * 显示adapter数据
     */
    void showAdapterView(GankIoDataBean gankIoDataBean);

    /**
     * 刷新adapter数据
     */
    void refreshAdapter(GankIoDataBean gankIoDataBean);

    /**
     * 显示列表没有更多数据布局
     */
    void showListNoMoreLoading();

    void showLoadFailedView();
}
