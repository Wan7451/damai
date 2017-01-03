# 对用户上传头像的简单封装

* 可在Activity中使用
* 可在Fragment中使用
* 默认上传到7牛云，实际使用需要修改地址
* 头像裁剪 实际使用需要根据需求修改裁剪图片的大小
  支持裁剪分辨率高的图片

使用：

+ 1、初始化

```
userIconManager = UserIconManager.getInstance(this);
userIconManager.setIsAutoUp2Server(true);
userIconManager.setUserIconHandlerListener(new UserIconHandlerListener() {
      @Override
      public void onCropOk(File f) {
          Bitmap bitmap = BitmapFactory.decodeFile(f.getPath());
          binding.userIcon.setImageBitmap(bitmap);
      }

      @Override
      public void onUp2ServerOk(String url) {
          Log.i("==========>>", url);
          ImageLoader.getInstance().loadImages(binding.userIcon, url, true);
      }

      @Override
      public void onError(String error) {

      }
   });
```


+ 2 显示选择 选择图片的 对话框
```
userIconManager.showChoiceDialog();
```


+ 3 重写方法
```
 @Override
 public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    userIconManager.onActivityResult(requestCode, resultCode, data);
 }
```
