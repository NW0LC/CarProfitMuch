package com.exz.carprofitmuch.module;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.blankj.utilcode.util.ScreenUtils;
import com.exz.carprofitmuch.R;
import com.szw.framelibrary.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 引导页界面(viewpager)
 * */
public class IntroActivity extends Activity implements View.OnClickListener{


    //TODO 图片资源(测试用小绿机器人,正式后要替换)
    int[] image={R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher};

    List<View> images=new ArrayList<>();
    private ViewPager vp;
    private Button bt;

    private int currentPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.immersive(this);
//      根据版本号判断是不是第一次使用
        PackageInfo info=null;
        try {
            info=getPackageManager().getPackageInfo(getPackageName(),0);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        final int currentVersion = info != null ? info.versionCode : 0;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        Float lastVersion = sp.getFloat("VERSION_KEY", 0);

//        if (currentVersion>lastVersion){
        if (true){
//            第一次启动将当前版本进行存储
            sp.edit().putFloat("VERSION_KEY",currentVersion).apply();
            setContentView(R.layout.activity_intro);
            //将图片添加到list<view>集合中
            for (int anImage : image) {
                ImageView iv = new ImageView(this);
                iv.setImageResource(anImage);
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                images.add(iv);

            }

            vp =  findViewById(R.id.vp);
            bt =  findViewById(R.id.gointo_app_bt);

            GuildPagerAdapter adapter=new GuildPagerAdapter(images);
            vp.setAdapter(adapter);

            vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                //currentpage后面要用
                    currentPage=position;
                    if(position==2){
                        //设置可见,如果用按钮也可以实现activity的跳转,两种跳转功能都具备
                      bt.setVisibility(View.VISIBLE);
                        bt.setOnClickListener(IntroActivity.this);
                    }else {
                        bt.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            //设置ViewPager的滑动监听,为了滑动到最后一页,继续滑动实现页面的跳转
            vp.setOnTouchListener(new View.OnTouchListener() {
                float startX;

                float endX;


                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            startX = event.getX();

                            break;
                        case MotionEvent.ACTION_UP:
                            endX = event.getX();
//获取屏幕的宽度
                            int width = ScreenUtils.getScreenWidth();
                            //根据滑动的距离来切换界面
                            if (currentPage == 2 && startX - endX >= (width / 5)) {

                                startApp();//切换界面
                            }

                            break;
                    }
                    return false;
                }
            });

        }else {
//            非第一次启动直接跳转
            startApp();
        }




    }

    private void startApp() {
        Intent intent=new Intent(this,LogoActivity.class);
        startActivity(intent);
        finish();
    }


//button 点击事件
    public void onClick(View view) {
        startApp();

    }

    /**
     *@author Administrator
     *@date 2016/12/2 0002  上午 9:26
     *@Description viewpager 的适配器,内容不多直接写内部类了
     *@param
     *@retrun
    */
    public class GuildPagerAdapter extends PagerAdapter {
        List<View> list;

        public GuildPagerAdapter(List<View> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list.get(position));
            return list.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(list.get(position));

        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }
    }

}