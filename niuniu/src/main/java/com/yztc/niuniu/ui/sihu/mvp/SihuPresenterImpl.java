package com.yztc.niuniu.ui.sihu.mvp;

import android.content.Context;
import android.text.TextUtils;

import com.yztc.niuniu.dickcache.DickCacheFunc;
import com.yztc.niuniu.dickcache.DiskCache;
import com.yztc.niuniu.net.ExceptionHandle;
import com.yztc.niuniu.net.ISihuApi;
import com.yztc.niuniu.net.NetSubscriber;
import com.yztc.niuniu.ui.sihu.SihuBean;

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

public class SihuPresenterImpl implements ISihuPresenter {

    private static final String KEY_LIST_PULL = SihuPresenterImpl.class.getName() + "LIST";
    private static final String KEY_LIST_PUSH = SihuPresenterImpl.class.getName() + "LIST";
    private static final String KEY_IMAGESLIST = SihuPresenterImpl.class.getName() + "IMAGES_LIST";
    private final DiskCache diskCache;


    private Context context;
    private ISihulView view;
    private ISihuModel model;

    private String page;
    private int count;


    public SihuPresenterImpl(Context context, ISihulView view) {
        this.context = context;
        this.view = view;
        diskCache = DiskCache.getInstance();
        model = new SihuModelImpl();
    }



    @Override
    public void loadData() {
        page = "index";
        count = 1;

        String key = KEY_LIST_PULL + page;

        final String cache = diskCache.getString(key);
        if (!TextUtils.isEmpty(cache)) {
            ArrayList<SihuBean> data = new SihuPresenterImpl.ToBeanConvert().call(cache);
            view.showContent(data, true);
        }

        if (!diskCache.isTimeOut(key)) {
            return;
        }

        model.loadlist(page)
                .map(new DickCacheFunc(key))
                .map(new ToBeanConvert())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetSubscriber<ArrayList<SihuBean>>(context) {


                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        view.showError(e.message);
                    }

                    @Override
                    public void onNext(ArrayList<SihuBean> strings) {
                        view.showContent(strings, true);
                    }
                });
    }

    @Override
    public void addData() {
        page = "index-" + ++count;

        String key = KEY_LIST_PUSH + page;

        final String cache = diskCache.getString(key);
        if (!TextUtils.isEmpty(cache)) {
            ArrayList<SihuBean> data = new SihuPresenterImpl.ToBeanConvert().call(cache);
            view.showContent(data, false);
        }

        if (!diskCache.isTimeOut(key)) {
            return;
        }

        model.loadlist(page)
                .map(new DickCacheFunc(key))
                .map(new ToBeanConvert())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetSubscriber<ArrayList<SihuBean>>(context) {

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        view.showError(e.message);
                    }

                    @Override
                    public void onNext(ArrayList<SihuBean> strings) {
                        view.showContent(strings, false);
                    }
                });
    }

    @Override
    public void loadImages(String path) {
        path = path.replaceFirst("/", "");
        String key = KEY_IMAGESLIST + path;

        final String cache = diskCache.getString(key);
        if (!TextUtils.isEmpty(cache)) {
            ArrayList<String> data = new SihuPresenterImpl.ToStringArrayConvert().call(cache);
            view.startDetail(data);
        }

        if (!diskCache.isTimeOut(key)) {
            return;
        }

        model.loadImages(ISihuApi.BASE_URL + path)
                .map(new DickCacheFunc(key))
                .map(new ToStringArrayConvert())
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
                    public void onNext(ArrayList<String> strings) {
                        view.startDetail(strings);
                    }
                });
    }


    static class ToBeanConvert implements Func1<String, ArrayList<SihuBean>> {

        @Override
        public ArrayList<SihuBean> call(String s) {

            Document document = Jsoup.parse(s);
            Elements max = document.getElementsByAttributeValue("class", "box list channel");

            Elements parent = max.get(0).children();
            Elements children = parent.get(0).children();

            ArrayList<SihuBean> data = new ArrayList<>();
            for (Element c : children) {
                Elements links = c.getElementsByTag("li");
                for (Element link : links) {
                    SihuBean bean = new SihuBean();
                    bean.setUrl(link.child(0).attr("href"));
                    String t = link.text();
                    String title = t.substring(5, t.length());
                    bean.setTitle(title);
                    data.add(bean);
                    break;
                }
            }
            return data;
        }
    }

    static class ToStringArrayConvert implements Func1<String, ArrayList<String>> {

        @Override
        public ArrayList<String> call(String s) {
            Document document = Jsoup.parse(s);
            Elements max = document.getElementsByAttributeValue("class", "content");

            Elements parent = max.get(0).children();
            Elements children = parent.get(0).children();

            ArrayList<String> data = new ArrayList<>();
            for (Element c : children) {
                Elements links = c.getElementsByTag("img");
                for (Element link : links) {
                    data.add(link.attr("src"));
                    break;
                }
            }
            return data;
        }
    }
}
