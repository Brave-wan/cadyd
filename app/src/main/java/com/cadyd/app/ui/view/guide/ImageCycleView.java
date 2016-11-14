package com.cadyd.app.ui.view.guide;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.interfaces.TowObjectParameterInterface;

/**
 * 广告图片自动轮播控件
 * 集合ViewPager和指示器的一个轮播控件，主要用于一般常见的广告图片轮播，具有自动轮播和手动轮播功能
 * 使用：只需在xml文件中使用{@code <com.cadyd.app.ui.view.guide.ImageCycleView/>} ，
 * 然后在页面中调用  {@link #setImageResources(List, ImageCycleViewListener) }即可!
 * 另外提供{@link #startImageCycle() } \ {@link #pushImageCycle() }两种方法，用于在Activity不可见之时节省资源；
 * 因为自动轮播需要进行控制，有利于内存管理
 *
 * @author wcy
 */
public class ImageCycleView extends LinearLayout {

    //TODO
    public ImageView.ScaleType scaleType = ImageView.ScaleType.FIT_XY;

    private TowObjectParameterInterface towObjectParameterInterface;

    /**
     * 上下文
     */
    private Context mContext;

    /**
     * 图片轮播视图
     */
    private ViewPager mAdvPager = null;

    /**
     * 滚动图片视图适配器
     */
    private ImageCycleAdapter mAdvAdapter;

    /**
     * 图片轮播指示器控件
     */
    private ViewGroup mGroup;

    /**
     * 图片轮播指示器-个图
     */
    private ImageView mImageView = null;

    /**
     * 滚动图片指示器-视图列表
     */
    private ImageView[] mImageViews = null;

    /**
     * 图片滚动当前图片下标
     */
    private int mImageIndex = 0;

    /**
     * 手机密度
     */
    private float mScale;
    /**
     * 是否下一张
     */
    private boolean isContinue = true;
    private AtomicInteger what = new AtomicInteger(4000);

    /**
     * @param context
     */
    public ImageCycleView(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public ImageCycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mScale = context.getResources().getDisplayMetrics().density;
        LayoutInflater.from(context).inflate(R.layout.ad_cycle_view, this);
        mAdvPager = (ViewPager) findViewById(R.id.adv_pager);
        mAdvPager.addOnPageChangeListener(new GuidePageChangeListener());
        mAdvPager.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        isContinue = true;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        isContinue = false;
                        break;
                    default:
                        isContinue = true;
                        break;
                }
                return false;
            }
        });
        // 滚动图片右下指示器视图
        mGroup = (ViewGroup) findViewById(R.id.viewGroup);
    }

    /**
     * 装填图片数据
     *
     * @param imageUrlList
     * @param imageCycleViewListener
     */
    public void setImageResources(List<String> imageUrlList, ImageCycleViewListener imageCycleViewListener, int resid) {
        if (mGroup.getChildCount() > 0) {
            // 清除所有子视图
            mGroup.removeAllViews();
        }
        // 图片广告数量
        final int imageCount = imageUrlList.size();
        mImageViews = new ImageView[imageCount];
        for (int i = 0; i < imageCount; i++) {
            mImageView = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(15, 15);
            params.setMargins(20, 0, 0, 0);
            mImageView.setLayoutParams(params);
            mImageViews[i] = mImageView;
            if (i == 0) {
                mImageViews[i].setBackgroundResource(R.drawable.bg_circle_sel);
            } else {
                mImageViews[i].setBackgroundResource(R.drawable.bg_circle_white);
            }
            mGroup.addView(mImageViews[i]);
        }
        mAdvAdapter = new ImageCycleAdapter(mContext, imageUrlList, imageCycleViewListener,resid);
        mAdvPager.setAdapter(mAdvAdapter);
        startImageCycle();
    }

    Thread thread = null;

    /**
     * 开始轮播(手动控制自动轮播与否，便于资源控制)
     */
    public void startImageCycle() {
//        startImageTimerTask();
    }

    /**
     * 暂停轮播——用于节省资源
     */
    public void pushImageCycle() {
        stopImageTimerTask();
    }

    /**
     * 开始图片滚动任务
     */
    private void startImageTimerTask() {
        isContinue = true;
        if (thread == null) {
            what.getAndSet(0);
            // 图片每5秒滚动一次
            thread = new Thread() {
                public void run() {
                    while (true) {
                        if (isContinue) {
                            try {
                                sleep(5000);
                                what.incrementAndGet();
                                viewHandler.sendEmptyMessage(what.get());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            };
            thread.start();
        }
    }

    /**
     * 停止图片滚动任务
     */
    private void stopImageTimerTask() {
        isContinue = false;
    }

    private final Handler viewHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mImageViews != null) {
                mAdvPager.setCurrentItem(msg.what);
            }

        }
    };

    /**
     * 轮播图片状态监听器
     *
     * @author minking
     */
    private final class GuidePageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int state) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int index) {
            what.getAndSet(index);
            viewHandler.sendEmptyMessage(index);
            // 设置当前显示的图片下标
            mImageIndex = index % mImageViews.length;
            // 设置图片滚动指示器背景
            mImageViews[mImageIndex].setBackgroundResource(R.drawable.bg_circle_sel);
            for (int i = 0; i < mImageViews.length; i++) {
                if (mImageIndex != i) {
                    mImageViews[i].setBackgroundResource(R.drawable.bg_circle_white);
                }
            }
            if (towObjectParameterInterface != null) {
                towObjectParameterInterface.Onchange(0, index, null);
            }
        }
    }

    private class ImageCycleAdapter extends PagerAdapter {

        /**
         * 图片视图缓存列表
         */
        private ArrayList<ImageView> mImageViewCacheList;

        /**
         * 图片资源列表
         */
        private List<String> mAdList = new ArrayList<String>();

        /**
         * 广告图片点击监听器
         */
        private ImageCycleViewListener mImageCycleViewListener;

        private Context mContext;
        private int resid;

        public ImageCycleAdapter(Context context, List<String> adList, ImageCycleViewListener imageCycleViewListener, int resid) {
            mContext = context;
            mAdList = adList;
            this.resid = resid;
            mImageCycleViewListener = imageCycleViewListener;
            mImageViewCacheList = new ArrayList<>();
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final int index = position % mAdList.size();// 计算当前显示的itemview
            String imageUrl = mAdList.get(index);
            ImageView imageView = null;
            if (mImageViewCacheList.isEmpty()) {
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

                imageView.setScaleType(scaleType);

            } else {
                imageView = mImageViewCacheList.remove(0);
            }
            // 设置图片点击监听
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mImageCycleViewListener.onImageClick(index, v);
                }
            });
            imageView.setTag(imageUrl);
            ApiClient.displayImage(imageUrl, imageView,resid);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ImageView view = (ImageView) object;
            container.removeView(view);
            mImageViewCacheList.add(view);
        }

    }

    /**
     * 轮播控件的监听事件
     *
     * @author minking
     */
    public static interface ImageCycleViewListener {
        /**
         * 单击图片事件
         *
         * @param position
         * @param imageView
         */
        public void onImageClick(int position, View imageView);

        public void carousel();
    }

    /**
     * 轮播控件的变化监听
     **/
    public void setPageChangeListener(TowObjectParameterInterface towObjectParameterInterface) {
        this.towObjectParameterInterface = towObjectParameterInterface;
    }

}
