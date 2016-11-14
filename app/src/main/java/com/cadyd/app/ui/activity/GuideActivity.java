package com.cadyd.app.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cadyd.app.AppManager;
import com.cadyd.app.R;
import com.cadyd.app.adapter.GuidePagerAdapter;
import com.cadyd.app.ui.base.BaseActivity;

import org.wcy.android.utils.ActivityUtil;

import java.util.ArrayList;
import java.util.List;

//import com.tencent.bugly.crashreport.CrashReport;


 /**
  * @Description: ${todo}(这里用一句话描述这个方法的作用)}$
  * 作者：Jerry
  * 邮箱：185214487@qq.com
  * @time 16-10-12 下午1:40
  */
public class GuideActivity extends BaseActivity {
    private final long TWO_SECOND = 1000 * 2;
    private long mPreTime;
    /**
     * 日志标记
     */
    public final String TAG = "GuideActivity";
    /**
     * viewPgaer控件，显示各种图片
     */
    private ViewPager viewPager_guide;
    /**
     * 引导页小点
     */
    private LinearLayout linear_guide_dots;
    /**
     * viewPager适配器
     */
    private GuidePagerAdapter guidePagerAdapter;
    /**
     * 适配器数据源
     */
    private List<View> views;
    /**
     * 保存每次引导上一种状态变量
     */
    private int currentIndex;
    /**
     * 最开始的生命周期，用来初始化
     */
    int[] images = new int[]{R.mipmap.slide1, R.mipmap.slide2, R.mipmap.slide3};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUtil.setFullScreen(this, true);
        setContentView(R.layout.activity_guide);
        // 判断是否首次加载
        if (application.getInitial()) {
            startActivity(MainActivity.class, true);
        } else {
            initLayout();
        }
    }

    /**
     * 初始化布局
     */
    private void initLayout() {
        currentIndex = 0;
        /* 设置viewpager数据源 */
        views = new ArrayList<View>();
        /* viewPager设置 */
        viewPager_guide = (ViewPager) findViewById(R.id.viewPager_guide);
        LayoutInflater inflater = LayoutInflater.from(this);
        /* 引导小点设置 */
        linear_guide_dots = (LinearLayout) findViewById(R.id.linear_guide_dots);
        for (int i = 0; i < images.length; i++) {
            View view = inflater.inflate(R.layout.guide_content, null);
            view.setBackgroundResource(images[i]);
            if (i == images.length - 1) {
                TextView tv = (TextView) view.findViewById(R.id.start);
                tv.setVisibility(View.VISIBLE);
                tv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        application.setInitial();// 设置为不是首次加载
                        finishActivity();
                        startActivity(MainActivity.class, true);
                    }
                });
            }
            views.add(view);
            ImageView iv = new ImageView(GuideActivity.this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
            params.setMargins(20, 0, 0, 0);
            iv.setLayoutParams(params);
            iv.setBackgroundResource(R.drawable.slide_image_dot_c);
            if (i == 0) {
                iv.setEnabled(false);// 设置为白色，即选中状态
            } else {
                iv.setEnabled(true);// 都设为灰色
            }
//            linear_guide_dots.addView(iv);
        }
        guidePagerAdapter = new GuidePagerAdapter(views, this);
        viewPager_guide.setAdapter(guidePagerAdapter);
        viewPager_guide.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                currentIndex = arg0;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - mPreTime) > TWO_SECOND) {
            Toast.makeText(GuideActivity.this, getString(R.string.main_press_again_exit),
                    Toast.LENGTH_SHORT).show();
            mPreTime = currentTime;
        } else {
            AppManager.getAppManager().AppExit();
        }
    }


}
