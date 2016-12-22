package com.yztc.core.base;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.yztc.core.App;
import com.yztc.core.R;
import com.yztc.core.utils.LogUtils;

/**
 * Created by wanggang on 2016/12/21.
 */

public class LoadResActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        overridePendingTransition(R.anim.null_anim, R.anim.null_anim);
        setContentView(R.layout.activity_spalsh);
        new LoadDexTask().execute();
    }

    class LoadDexTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            try {
                MultiDex.install(getApplication());
                LogUtils.d("loadDex", "install finish");
                ((App) getApplication()).installFinish(getApplication());
            } catch (Exception e) {
                LogUtils.e("loadDex", e.getLocalizedMessage() + "");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            LogUtils.d("loadDex", "get install finish");
            finish();
            System.exit(0);
        }
    }

    @Override
    public void onBackPressed() {
        //cannot backpress
    }
}