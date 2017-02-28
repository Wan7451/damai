package com.yztc.niuniu.ui.detail;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yztc.niuniu.R;
import com.yztc.niuniu.util.ImageLoager;

import java.util.ArrayList;

public class PagerActivity extends AppCompatActivity {

    private TextView textView;
    private ArrayList<String> imgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);

        getSupportActionBar().hide();

        ViewPager banner = (ViewPager) findViewById(R.id.pager_view);
        textView = (TextView) findViewById(R.id.pager_text);

        imgs = getIntent().getStringArrayListExtra("imgs");
        int curr = getIntent().getIntExtra("curr", 0);

        ImgPagerAdapter adapter = new ImgPagerAdapter(this, imgs);
        banner.setAdapter(adapter);
        textView.setText(curr + 1 + "/" + imgs.size());

        banner.setCurrentItem(curr);

        banner.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                textView.setText(position + 1 + "/" + imgs.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    static class ImgPagerAdapter extends PagerAdapter {

        private Context context;
        private ArrayList<String> imgs;
        private ArrayList<ImageView> imgViews;

        public ImgPagerAdapter(Context context, ArrayList<String> imgs) {
            this.context = context;
            this.imgs = imgs;
            init();
        }


        private void init() {
            if (imgs == null)
                return;
            imgViews = new ArrayList<>();
            for (String url : imgs) {
                ImageView imgView = new ImageView(context);
                ViewGroup.LayoutParams lp = new ViewPager.LayoutParams();
                lp.width = context.getResources().getDisplayMetrics().widthPixels;
                lp.height = context.getResources().getDisplayMetrics().heightPixels;
                imgView.setLayoutParams(lp);
                ImageLoager.loadZoom(context, url, imgView);
                imgViews.add(imgView);
            }
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = imgViews.get(position);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imgViews.get(position));
        }

        @Override
        public int getCount() {
            return imgs != null ? imgs.size() : 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
