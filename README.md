# 大麦网Demo
通过Demo总结下最近使用的技术


    使用原生手写

    1. 封装网络框架 HttpURLConnection
       RxJava+Retrofit+Okhttp
       动态缓存JSON数据 30分种，超时重新加载
    2. 自定义图片加载工具
       使用三级缓存+队列 下载、展示
    3. Fragment懒加载
    4. 自定义Banner
       可以显示少于5条Banner数据  甚至只有1条Banner数据
       自动滚动，触摸时不滚动Banner
       可设置点击事件
    5. 封装Adapter,减少代码量
        支持多布局
        支持头部、尾部视图、空视图
    6. 分模块开发，将项目框架与核心业务区分开
    7. 封装多个常用类
    ...

# 技术点一览

* BannerView 的[用法](https://github.com/Wan7451/damai/blob/master/core/src/main/java/com/yztc/core/views/banner/_sample.md)

* 封装网络框架 的[用法](https://github.com/Wan7451/damai/blob/master/core/src/main/java/com/yztc/core/net/_sample.md)

* 万能适配器(洋神) 的[用法](https://github.com/Wan7451/damai/blob/master/core/src/main/java/com/yztc/core/adapter/_sample.md)

* 流式布局(洋神) 的[用法](https://github.com/Wan7451/damai/blob/master/core/src/main/java/com/yztc/core/views/flowlayout/_sample.md)

* 懒加载Fragment的[用法](https://github.com/Wan7451/damai/blob/master/core/src/main/java/com/yztc/core/base/LazyFragment.java)

* 自定义 ImageLoader 的[用法](https://github.com/Wan7451/damai/blob/master/core/src/main/java/com/yztc/core/image/ImageLoader.java)

* 封装的多个工具类 [查看](https://github.com/Wan7451/damai/tree/master/core/src/main/java/com/yztc/core/utils)

* 限时本地缓存数据 [查看](https://github.com/Wan7451/damai/blob/master/core/src/main/java/com/yztc/core/utils/LimitDataCache.java)

* 自定Toast，解决 用户屏蔽App的通知权限后，Toast 不显示的问题
                [查看](https://github.com/Wan7451/damai/blob/master/core/src/main/java/com/yztc/core/views/Toast.java)