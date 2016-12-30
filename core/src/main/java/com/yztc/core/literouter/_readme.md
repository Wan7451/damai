# 轻量级 RouteLite

轻量级路由，主要用与解决 多模块开发 中界面跳转的问题，降低模块之间的耦合性


## 使用
## 1. 创建接口


使用方式与Retrofit类似
* @ClassName  指定要跳转组件类
* @RequestCode  Activity跳转的Request,Service设置无效
* @Key       Intent传值 值的key
* 返回值  只能 null  或者  IntentWrapper

```
public interface IntentService {
    @ClassName("android.router.literouter.ActivityDemo2")
    @RequestCode(100)
    void intent2ActivityDemo2(@Key("platform") String platform, @Key("year") int year);

    @ClassName("android.router.literouter.ActivityDemo2")
    IntentWrapper intent2ActivityDemo2Raw(@Key("platform") String platform, @Key("year") int year);
}
```

## 2. 初始化LiteRoute

```
LiteRouter liteRouter = new LiteRouter.Builder().interceptor(new Interceptor() {
            @Override
            public boolean intercept(IntentWrapper intentWrapper) {
                return false;
            }
        }).build();
final IntentService intentService = liteRouter.create(IntentService.class, this);

```

## 3. 执行跳转

```
intentService.intent2ActivityDemo2("android", 2016);
```

## 4. 其他用法

```
IntentWrapper intentWrapper = intentService.intent2ActivityDemo2Raw("android", 2016);
// intent
Intent intent = intentWrapper.getIntent();
// add your flags
intentWrapper.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
// start
intentWrapper.start();
```