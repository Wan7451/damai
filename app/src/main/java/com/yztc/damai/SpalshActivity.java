package com.yztc.damai;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ImageView;

import com.tbruyelle.rxpermissions.RxPermissions;
import com.yztc.core.utils.WelcomePicManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

public class SpalshActivity extends AppCompatActivity {

    @BindView(R.id.pic)
    ImageView pic;

    private AsyncTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_spalsh);
        ButterKnife.bind(this);

        getSupportActionBar().hide();


        //异步加载之前下载的图片
        task = new AsyncTask<Object, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Object... params) {
                return WelcomePicManager.getPic(SpalshActivity.this);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                    pic.setImageBitmap(bitmap);
                }
            }
        };
        checkPermission();
    }

    private void startMainDelay(final AsyncTask task) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                task.cancel(true);
                Intent i = new Intent();
                i.setClass(SpalshActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, 2000);
    }

    private void checkPermission() {

        new RxPermissions(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(
                        new Action1<Boolean>() {
                            @Override
                            public void call(Boolean aBoolean) {
                                if (aBoolean) {
                                    task.execute();
                                    startMainDelay(task);
                                }
                            }
                        }
                );


    }


}
