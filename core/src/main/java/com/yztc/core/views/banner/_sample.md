# BannerView 轮播条
封装的轮播组件

## 简单的使用

```
ArrayList<String> adBanners=new ArrayList();
adBanners.add("url");
adBanners.add("url");
adBanners.add("url");

BannerView bannerView=findViewById(R.id.banner);

bannerView.setOnBannerViewClick(new BannerViewPager.OnBannerViewClick() {
            @Override
            public void onBannerViewClick(int position) {
                ToastUtils.show(banners.get(position).getName());
            }
        });

 bannerView.setData(adBanners);
```

