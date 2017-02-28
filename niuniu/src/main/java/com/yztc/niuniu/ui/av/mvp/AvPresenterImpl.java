package com.yztc.niuniu.ui.av.mvp;


import com.yztc.niuniu.net.IAvApi;
import com.yztc.niuniu.ui.av.AvBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by wanggang on 2017/2/14.
 */

public class AvPresenterImpl implements IAvPresenter {

    private static int maxPage;
    private IAvView view;
    private IAvModel model;

    private int page;

    public AvPresenterImpl(IAvView view) {
        this.view = view;
        this.model = new AvModelImpl();
    }


    @Override
    public void loadData() {
        page = 1;
        model.loadList("1")
                .map(new ToBeanConvert())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<AvBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(ArrayList<AvBean> data) {
                        view.showContent(data, true);
                    }
                });
    }

    @Override
    public void addData() {
        if (page >= maxPage) {
            view.showContent(null, false);
            return;
        }
        model.loadList("1-" + ++page)
                .map(new ToBeanConvert())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<AvBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(ArrayList<AvBean> data) {
                        view.showContent(data, false);
                    }
                });

    }


    static class ToBeanConvert implements Func1<String, ArrayList<AvBean>> {

        @Override
        public ArrayList<AvBean> call(String s) {
            Document document = Jsoup.parse(s);
            Elements max = document.getElementsByAttributeValue("class", "a2");
            String text = max.first().text();
            String[] split = text.split("/");
            String[] split1 = split[1].split("页");
            AvPresenterImpl.maxPage = Integer.parseInt(split1[0]);
            Elements div = document.getElementsByAttributeValue("class", "mubanlist");
            Elements elements = div.toggleClass("post-thumbnail");
            Elements childrens = elements.first().children();
            ArrayList<AvBean> data = new ArrayList<>();
            for (Element c : childrens) {
                AvBean bean = new AvBean();
                Elements links = c.getElementsByTag("a");
                for (Element link : links) {
                    bean.setUrl(link.attr("href"));
                    bean.setTitle(link.attr("title"));
                    break;
                }
                Elements imgs = c.getElementsByTag("img");
                for (Element link : imgs) {
                    bean.setImg(link.attr("src"));
                    break;
                }
                data.add(bean);
            }
            return data;
        }
    }

    @Override
    public void loadData(String path) {
        path = path.replaceFirst("/", "");
        model.loadDetail(IAvApi.BASE_URL + path)
                .map(new AvPresenterImpl.ToStringConvert())
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
                    public void onNext(ArrayList<String> strings) {
                        view.startDetail(strings);
                    }
                });
    }


    static class ToStringConvert implements Func1<String, ArrayList<String>> {

        @Override
        public ArrayList<String> call(String s) {
            Document document = Jsoup.parse(s);
            Elements max = document.getElementsByAttributeValue("align", "center");
            Elements children = max.get(0).children();
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
