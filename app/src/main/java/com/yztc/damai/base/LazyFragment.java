package com.yztc.damai.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wanggang on 2016/12/12.
 */

public abstract class LazyFragment extends BaseFragment {


    private boolean isCreated;
    private boolean isInited;
    private boolean  needInit;

    protected View root;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && !isInited){
            if(isCreated){
                init();
            }else {
                needInit=true;
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       root=  inflater.inflate(getLayoutId(),container,false);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        if(needInit){
            init();
        }
        isCreated=true;
    }

    private void init(){
        init(root);
        isInited=true;
    }



    protected abstract void init(View v);
    @LayoutRes
    protected abstract int getLayoutId();
}
