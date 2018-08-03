package i.am.lucky.data.model;

import android.arch.lifecycle.MutableLiveData;

import i.am.lucky.bean.HotMovieBean;
import i.am.lucky.http.HttpClient;
import i.am.lucky.viewmodel.movie.OnMovieLoadListener;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Cazaea
 * @data 2017/12/14
 * @Description 干货订制页面
 * @Singleton // informs Dagger that this class should be constructed once
 */
public class OneRepository {

    public MutableLiveData<HotMovieBean> getHotMovie() {
        final MutableLiveData<HotMovieBean> data = new MutableLiveData<>();
        HttpClient.Builder.getDouBanService().getHotMovie().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<HotMovieBean>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                data.setValue(null);
            }

            @Override
            public void onNext(HotMovieBean hotMovieBean) {
                if (hotMovieBean != null) {
                    data.setValue(hotMovieBean);
                }
            }
        });
        return data;
    }

    public void getMovieTop250(int start, int count, OnMovieLoadListener loadListener) {
        HttpClient.Builder.getDouBanService().getMovieTop250(start, count).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<HotMovieBean>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                if (loadListener != null) {
                    loadListener.onFailure();
                }
            }

            @Override
            public void onNext(HotMovieBean hotMovieBean) {
                if (hotMovieBean != null) {
                    if (loadListener != null) {
                        loadListener.onSuccess(hotMovieBean);
                    }
                }
            }
        });
    }

}
