package com.yztc.niuniu.ui.maomi.girls.mvp;

import android.content.Context;
import android.text.TextUtils;

import com.yztc.niuniu.dickcache.DickCacheFunc;
import com.yztc.niuniu.dickcache.DiskCache;
import com.yztc.niuniu.net.ExceptionHandle;
import com.yztc.niuniu.net.IMaomiApi;
import com.yztc.niuniu.net.NetSubscriber;
import com.yztc.niuniu.ui.maomi.girls.GirlsBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by wanggang on 2017/2/14.
 */

public class GirlsPresenterImpl implements IGirlsPresenter {


    private static final String KEY_LIST = GirlsPresenterImpl.class.getName() + "LIST";
    private static final String KEY_IMAGESLIST = GirlsPresenterImpl.class.getName() + "IMAGES_LIST";
    private static final String KEY_IMAGES = GirlsPresenterImpl.class.getName() + "IMAGES";

    private final DiskCache diskCache;

    private Context context;
    private IGirlsView view;
    private IGirlsModel model;

    public GirlsPresenterImpl(Context context, IGirlsView view) {
        this.context = context;
        this.view = view;
        this.model = new GirlsModelImpl();
        diskCache = DiskCache.getInstance();
    }



    @Override
    public void loadData() {

        String key = KEY_LIST;

        final String cache = diskCache.getString(key);
        if (!TextUtils.isEmpty(cache)) {
            ArrayList<GirlsBean> data = new ToBeanConvert().call(cache);
            view.showContent(data, true);
        }

        if (!diskCache.isTimeOut(key, System.currentTimeMillis())) {
            return;
        }

        model.loadlist("")
                .map(new DickCacheFunc(key))
                .map(new ToBeanConvert())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetSubscriber<ArrayList<GirlsBean>>(context) {

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        view.showError(e.message);
                    }

                    @Override
                    public void onNext(ArrayList<GirlsBean> strings) {
                        view.showContent(strings, true);
                    }
                });
    }

    @Override
    public void loadImagesList(String path) {
        path = path.replaceFirst("/", "");
        String key = KEY_IMAGESLIST + path;

        String cache = diskCache.getString(key);
        if (!TextUtils.isEmpty(cache)) {
            ArrayList<GirlsBean> data = new ToImagesListConvert().call(cache);
            view.showContent(data, true);
        }

        if (!diskCache.isTimeOut(key, System.currentTimeMillis())) {
            return;
        }

        model.imagesList(IMaomiApi.BASE_URL + path)
                .map(new DickCacheFunc(key))
                .map(new ToImagesListConvert())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetSubscriber<ArrayList<GirlsBean>>(context) {

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        view.showError(e.message);
                    }

                    @Override
                    public void onNext(ArrayList<GirlsBean> strings) {
                        view.showContent(strings, true);
                    }
                });
    }


    @Override
    public void loadImages(String path) {
        path = path.replaceFirst("/", "");
        String key = KEY_IMAGES + path;

        String cache = diskCache.getString(key);
        if (!TextUtils.isEmpty(cache)) {
            ArrayList<String> data = new ToImagesConvert().call(cache);
            view.startDetail(data);
        }

        if (!diskCache.isTimeOut(key, System.currentTimeMillis())) {
            return;
        }


        model.imagesList(IMaomiApi.BASE_URL + path)
                .map(new DickCacheFunc(key))
                .map(new ToImagesConvert())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetSubscriber<ArrayList<String>>(context) {

                    @Override
                    protected boolean ishowLoading() {
                        return true;
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        view.showError(e.message);
                    }

                    @Override
                    public void onNext(ArrayList<String> data) {
                        view.startDetail(data);
                    }
                });

    }

    private static class ToImagesConvert implements Func1<String, ArrayList<String>> {

        @Override
        public ArrayList<String> call(String s) {

            Document document = Jsoup.parse(s);
            Elements max = document.getElementsByAttributeValue("class", "picContent");

            Elements e1 = max.get(0).children();
            ArrayList<String> data = new ArrayList<>();
            for (Element e4 : e1) {
                Elements e5 = e4.getElementsByTag("img");
                String src = e5.attr("src");
                if (!TextUtils.isEmpty(src)) {
                    data.add(e5.attr("src"));
                }
            }
            return data;
        }
    }


    private static class ToBeanConvert implements Func1<String, ArrayList<GirlsBean>> {

        @Override
        public ArrayList<GirlsBean> call(String s) {

            Document document = Jsoup.parse(s);
            Elements max = document.getElementsByAttributeValue("class", "mainArea px17");

            Elements e1 = max.get(0).children();
            Elements e2 = e1.get(0).children();
            Elements e3 = e2.get(0).children();
            ArrayList<GirlsBean> data = new ArrayList<>();
            for (Element e4 : e3) {
                Elements e5 = e4.getElementsByTag("a");
                for (Element e6 : e5) {
                    GirlsBean bean = new GirlsBean();
                    bean.setUrl(e6.attr("href"));
                    Elements e7 = e6.getElementsByTag("img");
                    for (Element e8 : e7) {
                        bean.setImage(e8.attr("src"));
                    }
                    data.add(bean);
                }
            }
            return data;
        }
    }

    private static class ToImagesListConvert implements Func1<String, ArrayList<GirlsBean>> {

        @Override
        public ArrayList<GirlsBean> call(String s) {
            Document document = Jsoup.parse(s);
            Elements max = document.getElementsByAttributeValue("class", "movieList");

            Elements parent = max.get(0).children();

            ArrayList<GirlsBean> data = new ArrayList<>();
            for (Element c : parent) {
                Elements a = c.getElementsByTag("a");
                String href = a.attr("href");

                Elements b = c.getElementsByTag("img");
                String src = b.attr("src");

                GirlsBean bean = new GirlsBean();
                bean.setUrl(href);
                bean.setImage(src);
                data.add(bean);
            }
            return data;
        }
    }
}
