package i.am.lucky.viewmodel.movie;

import i.am.lucky.bean.HotMovieBean;

/**
 * @author Cazaea
 * @data 2017/12/26
 * @Description
 */

public interface OnMovieLoadListener {

    void onSuccess(HotMovieBean bean);

    void onFailure();
}
