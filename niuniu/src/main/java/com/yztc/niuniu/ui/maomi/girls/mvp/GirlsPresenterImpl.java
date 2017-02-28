package com.yztc.niuniu.ui.maomi.girls.mvp;

import android.text.TextUtils;

import com.yztc.niuniu.dickcache.DickCacheNoTimeOutFunc;
import com.yztc.niuniu.dickcache.DiskCache;
import com.yztc.niuniu.net.IMaomiApi;
import com.yztc.niuniu.ui.maomi.girls.GirlsBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import rx.Subscriber;
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

    private IGirlsView view;
    private IGirlsModel model;

    public GirlsPresenterImpl(IGirlsView view) {
        this.view = view;
        this.model = new GirlsModelImpl();
        diskCache = DiskCache.getInstance();
    }


    @Override
    public void loadData() {
        final String cache = diskCache.getString(KEY_LIST);
        if (!TextUtils.isEmpty(cache)) {
            ArrayList<GirlsBean> data = new ToBeanConvert().call(cache);
            view.showContent(data, true);
            return;
        }

        model.loadlist("")
                .map(new DickCacheNoTimeOutFunc(KEY_LIST))
                .map(new ToBeanConvert())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<GirlsBean>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(ArrayList<GirlsBean> strings) {
                        view.showContent(strings, true);
                    }
                });
    }

    @Override
    public void loadImagesList(String path) {
        String cache = diskCache.getString(KEY_IMAGESLIST + path);
        if (!TextUtils.isEmpty(cache)) {
            ArrayList<GirlsBean> data = new ToImagesListConvert().call(cache);
            view.showContent(data, true);
            return;
        }

        path = path.replaceFirst("/", "");
        model.imagesList(IMaomiApi.BASE_URL + path)
                .map(new DickCacheNoTimeOutFunc(KEY_IMAGESLIST))
                .map(new ToImagesListConvert())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<GirlsBean>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(ArrayList<GirlsBean> strings) {
                        view.showContent(strings, true);
                    }
                });
    }


    @Override
    public void loadImages(String path) {
        String cache = diskCache.getString(KEY_IMAGES + path);
        if (!TextUtils.isEmpty(cache)) {
            ArrayList<String> data = new ToImagesConvert().call(cache);
            view.startDetail(data);
            return;
        }

        path = path.replaceFirst("/", "");
        model.imagesList(IMaomiApi.BASE_URL + path)
                .map(new DickCacheNoTimeOutFunc(KEY_IMAGES))
                .map(new ToImagesConvert())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<String>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(ArrayList<String> data) {
                        view.startDetail(data);
                    }
                });

    }

    static class ToImagesConvert implements Func1<String, ArrayList<String>> {

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


    static class ToBeanConvert implements Func1<String, ArrayList<GirlsBean>> {

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

    static class ToImagesListConvert implements Func1<String, ArrayList<GirlsBean>> {

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
