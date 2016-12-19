package com.yztc.damai.ui.discover;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yztc.damai.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoverFragment extends Fragment {


    public DiscoverFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_discover, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        Observable loadData =
//                HttpRequest.getInstance().loadData("aaa");
//
//        loadData.subscribe(new BaseSubscriber<String>(getContext()) {
//            @Override
//            public void onNext(String s) {
//
//            }
//
//            @Override
//            public void onError(ExceptionHandle.ResponeThrowable e) {
//
//            }
//        });
    }
}
