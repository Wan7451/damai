package com.yztc.damai.inject;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * Created by wanggang on 2017/1/6.
 */

public class InjectActivity extends AppCompatActivity {

    private int mLayoutId = -1;


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        injectLayout();
        injectView();
    }

    private void injectView() {
        if (mLayoutId <= 0) {
            return;
        }
        Class<?> clazz = this.getClass();
        Field[] fields = clazz.getDeclaredFields();//获得声明的成员变量
        for (Field field : fields) {
            //判断是否有注解
            try {
                if (field.getAnnotations() != null) {
                    if (field.isAnnotationPresent(ViewInject.class)) {//如果属于这个注解
                        //为这个控件设置属性
                        field.setAccessible(true);//允许修改反射属性
                        ViewInject inject = field.getAnnotation(ViewInject.class);
                        field.set(this, this.findViewById(inject.value()));
                    }
                }
            } catch (Exception e) {
//                throw new InterruptedException("not found view id!");
                Log.e("wusy", "not found view id!");
            }
        }
    }

    private void injectLayout() {
        Class<?> clazz = this.getClass();
        if (clazz.getAnnotations() != null) {
            if (clazz.isAnnotationPresent(LayouyInject.class)) {
                LayouyInject inject = clazz.getAnnotation(LayouyInject.class);
                mLayoutId = inject.value();
                setContentView(mLayoutId);
            }
        }

    }
}
