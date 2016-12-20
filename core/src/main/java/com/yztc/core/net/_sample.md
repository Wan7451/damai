# 网络框架封装
使用 RxJava、Retrofit、Okhttp 进行封装


##特色
* 支持GET请求自动缓存
* 可动态设置接口缓存数据时长
* 支持添加公共参数
* 支持显示网络日志
* 处理自动处理网络问题
* 显示数据加载对话框
* BaseResponse 处理




### 使用
Retrofit的自定义

```
 BasicParamsInterceptor interceptor=new
                 BasicParamsInterceptor.Builder()
                 .addParam("key1","v1")
                 .build();

 RetrofitProvider hepler = new RetrofitProvider.Builder()
                 .baseUrl(NetConfig.BASE_URL)
                 .isGetAutoCache(true)
                 .isShowLog(true)
                 .basicParamsInterceptor(interceptor)
                 .build();
 retorfit = hepler.getRetorfit();
```


### 网络访问封装

```
public class HttpRequest extends HttpBase {

    private Retrofit retorfit;
    private DaMaiApi daMaiApi;

    static HttpRequest instance = null;

    private HttpRequest() {

        BasicParamsInterceptor interceptor=new
                BasicParamsInterceptor.Builder()
                .addParam("key1","v1")
                .build();

        RetrofitProvider hepler = new RetrofitProvider.Builder()
                .baseUrl(NetConfig.BASE_URL)
                .isGetAutoCache(true)
                .isShowLog(true)
                .basicParamsInterceptor(interceptor)
                .build();
        retorfit = hepler.getRetorfit();
    }

    public static HttpRequest getInstance() {
        if (instance == null) {
            synchronized (HttpRequest.class) {
                if (instance == null) {
                    instance = new HttpRequest();
                }
            }
        }
        return instance;
    }


    public DaMaiApi getApi() {
        if (daMaiApi == null)
            daMaiApi = retorfit.create(DaMaiApi.class);
        return daMaiApi;
    }


    public Observable loadData(String userId) {
        return daMaiApi.loadData("userId")
                .delay(1, TimeUnit.SECONDS)//延迟1S执行网络操作
                .compose(schedulersTransformer())//线程调度
                .compose(transformer());//处理 baseResponse
    }

}
```


### 调用接口



```
    Observable loadData =
        HttpRequest.getInstance().loadData("aaa");

    loadData.subscribe(new BaseSubscriber<String>(getContext()) {
        @Override
        public void onNext(String s) {

        }

        @Override
        public void onError(ExceptionHandle.ResponeThrowable e) {

        }
     });

```

# Response 基类处理 （可以不管）

```
public class Response<T> implements BaseResponse {
    @Override
    public boolean isOk() {
        return false;
    }

    @Override
    public T getData() {
        return null;
    }

    @Override
    public String erroeMsg() {
        return null;
    }
}
```
