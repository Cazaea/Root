package i.am.lucky.http;

import i.am.lucky.http.utils.BuildFactory;
import i.am.lucky.bean.FrontPageBean;
import i.am.lucky.bean.GankIoDataBean;
import i.am.lucky.bean.GankIoDayBean;
import i.am.lucky.bean.HotMovieBean;
import i.am.lucky.bean.MovieDetailBean;
import i.am.lucky.bean.UpdateBean;
import i.am.lucky.bean.book.BookBean;
import i.am.lucky.bean.book.BookDetailBean;
import i.am.lucky.bean.wanandroid.HomeListBean;
import i.am.lucky.bean.wanandroid.LoginBean;
import i.am.lucky.bean.wanandroid.NhdzListBean;
import i.am.lucky.bean.wanandroid.QsbkListBean;
import i.am.lucky.bean.wanandroid.WanAndroidBannerBean;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author Cazaea
 * @date 16/11/21
 * <p>
 * 网络请求类（一个接口一个方法）
 */
public interface HttpClient {

    class Builder {
        public static HttpClient getDouBanService() {
            return BuildFactory.getInstance().create(HttpClient.class, HttpUtils.API_DOUBAN);
        }

        public static HttpClient getTingServer() {
            return BuildFactory.getInstance().create(HttpClient.class, HttpUtils.API_TING);
        }

        public static HttpClient getGankIOServer() {
            return BuildFactory.getInstance().create(HttpClient.class, HttpUtils.API_GANKIO);
//            return HttpUtils.getInstance().getGankIOServer(HttpClient.class);
        }

        public static HttpClient getFirServer() {
            return BuildFactory.getInstance().create(HttpClient.class, HttpUtils.API_FIR);
        }

        public static HttpClient getWanAndroidServer() {
            return BuildFactory.getInstance().create(HttpClient.class, HttpUtils.API_WAN_ANDROID);
        }

        public static HttpClient getNHDZServer() {
            return BuildFactory.getInstance().create(HttpClient.class, HttpUtils.API_NHDZ);
        }

        public static HttpClient getQSBKServer() {
            return BuildFactory.getInstance().create(HttpClient.class, HttpUtils.API_QSBK);
        }
    }

    /**
     * 首页轮播图
     */
    @GET("ting?from=android&version=5.8.1.0&channel=ppzs&operator=3&method=baidu.ting.plaza.index&cuid=89CF1E1A06826F9AB95A34DC0F6AAA14")
    Observable<FrontPageBean> getFrontPage();

    /**
     * 玩安卓轮播图
     */
    @GET("banner/json")
    Observable<WanAndroidBannerBean> getWanAndroidBanner();

    /**
     * 分类数据: http://gank.io/api/data/数据类型/请求个数/第几页
     * 数据类型： 福利 | Android | iOS | 休息视频 | 拓展资源 | 前端 | all
     * 请求个数： 数字，大于0
     * 第几页：数字，大于0
     * eg: http://gank.io/api/data/Android/10/1
     */
    @GET("data/{type}/{pre_page}/{page}")
    Observable<GankIoDataBean> getGankIoData(@Path("type") String id, @Path("page") int page, @Path("pre_page") int pre_page);

    /**
     * 每日数据： http://gank.io/api/day/年/月/日
     * eg:http://gank.io/api/day/2015/08/06
     */
    @GET("day/{year}/{month}/{day}")
    Observable<GankIoDayBean> getGankIoDay(@Path("year") String year, @Path("month") String month, @Path("day") String day);

    /**
     * 豆瓣热映电影，每日更新
     */
    @GET("v2/movie/in_theaters")
    Observable<HotMovieBean> getHotMovie();

    /**
     * 获取电影详情
     *
     * @param id 电影bean里的id
     */
    @GET("v2/movie/subject/{id}")
    Observable<MovieDetailBean> getMovieDetail(@Path("id") String id);

    /**
     * 获取豆瓣电影top250
     *
     * @param start 从多少开始，如从"0"开始
     * @param count 一次请求的数目，如"10"条，最多100
     */
    @GET("v2/movie/top250")
    Observable<HotMovieBean> getMovieTop250(@Query("start") int start, @Query("count") int count);

    /**
     * 根据tag获取图书
     *
     * @param tag   搜索关键字
     * @param count 一次请求的数目 最多100
     */
    @GET("v2/book/search")
    Observable<BookBean> getBook(@Query("tag") String tag, @Query("start") int start, @Query("count") int count);

    @GET("v2/book/{id}")
    Observable<BookDetailBean> getBookDetail(@Path("id") String id);

    /**
     * @param id       应用id
     * @param apiToken token
     */
    @GET("latest/{id}")
    Observable<UpdateBean> checkUpdate(@Path("id") String id, @Query("api_token") String apiToken);

    /**
     * 糗事百科
     *
     * @param page 页码，从1开始
     */
    @GET("article/list/text")
    Observable<QsbkListBean> getQsbkList(@Query("page") int page);

    /**
     * 内涵段子
     *
     * @param page 页码，从0开始
     */
    @GET("neihan/stream/mix/v1/?mpic=2&essence=1&content_type=-102&message_cursor=-1&bd_Stringitude=113.369569&bd_latitude=23.149678&bd_city=广州市&am_Stringitude=113.367846&am_latitude=23.149878&am_city=广州市&am_loc_time=1465213692154&count=30&min_time=1465213700&screen_width=720&iid=4512422578&device_id=17215021497&ac=wifi&channel=NHSQH5AN&aid=7&app_name=joke_essay&version_code=431&device_platform=android&ssmix=a&device_type=6s+Plus&os_api=19&os_version=4.4.2&uuid=864394108025091&openudid=80FA5B208E050000&manifest_version_code=431")
    Observable<NhdzListBean> getNhdzList(@Query("page") int page);

    /**
     * 玩安卓，文章列表、知识体系下的文章
     *
     * @param page 页码，从0开始
     * @param cid  体系id
     */
    @GET("article/list/{page}/json")
    Observable<HomeListBean> getHomeList(@Path("page") int page, @Query("cid") Integer cid);

    /**
     * 玩安卓登录
     *
     * @param username 用户名
     * @param password 密码
     */
    @FormUrlEncoded
    @POST("user/login")
    Observable<LoginBean> login(@Field("username") String username, @Field("password") String password);

    /**
     * 玩安卓注册
     */
    @FormUrlEncoded
    @POST("user/register")
    Observable<LoginBean> register(@Field("username") String username, @Field("password") String password, @Field("repassword") String repassword);


    /**
     * 收藏文章列表
     *
     * @param page 页码
     */
    @GET("lg/collect/list/{page}/json")
    Observable<HomeListBean> getCollectList(@Path("page") int page);

    /**
     * 收藏本站文章，errorCode返回0为成功
     *
     * @param id 文章id
     */
    @POST("lg/collect/{id}/json")
    Observable<HomeListBean> collect(@Path("id") int id);

    /**
     * 取消收藏(首页文章列表)
     *
     * @param id 文章id
     */
    @POST("lg/uncollect_originId/{id}/json")
    Observable<HomeListBean> unCollectOrigin(@Path("id") int id);

    /**
     * 取消收藏，我的收藏页面（该页面包含自己录入的内容）
     *
     * @param id       文章id
     * @param originId 列表页下发，无则为-1
     *                 (代表的是你收藏之前的那篇文章本身的id；
     *                 但是收藏支持主动添加，这种情况下，没有originId则为-1)
     */
    @FormUrlEncoded
    @POST("lg/uncollect/{id}/json")
    Observable<HomeListBean> unCollect(@Path("id") int id, @Field("originId") int originId);

    /**
     * 根据tag获取music
     * @param tag
     * @return
     */

//    @GET("v2/music/search")
//    Observable<MusicRoot> searchMusicByTag(@Query("tag")String tag);

//    @GET("v2/music/{id}")
//    Observable<Musics> getMusicDetail(@Path("id") String id);
}