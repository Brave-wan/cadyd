package com.cadyd.app.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cadyd.app.R;
import com.cadyd.app.comm.ForPxTp;
import com.cadyd.app.widget.PercentLinearLayout;

import org.xmlpull.v1.XmlPullParser;


/**
 * 顶部标题栏
 *
 * @author ChaoyongWang
 */
@SuppressLint("ViewConstructor")
public class BaseLayout extends RelativeLayout {
    public Button leftButton;
    public Button rightButton;
    public TextView titleText;
    public int linearLayoutid = 10000;
    public int relativelayoutid = 1;
    public int leftButtonId = 2;
    public int textViewId = 3;
    public int rightButtonId = 4;
    public RelativeLayout title_relative;

    /**
     * @param context
     * @param layoutID 布局文件
     * @param resid    资源文件背景图片
     */
    public BaseLayout(Context context, int layoutID, int resid) {
        super(context);
        init(context, layoutID);
    }

    /**
     * @param context
     * @param resid   布局文件
     * @param resid   资源文件背景图片
     */
    public BaseLayout(Context context, View view, int resid) {
        super(context);
        init(context, view);
    }

    /**
     * @param context
     * @param layoutID 布局文件
     */
    public BaseLayout(Context context, int layoutID) {
        super(context);
        init(context, layoutID);
    }

    /**
     * @param context
     * @param view    布局文件
     */
    public BaseLayout(Context context, View view) {
        super(context);
        init(context, view);
    }

    /**
     * 初始化
     *
     * @param context
     * @param layoutID 布局文件id
     */
    public void init(Context context, int layoutID) {
        // 获取LayoutInflater对象，用于将xml转为视图对象
        LayoutInflater inflat = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // 获取对象
        View contentView = inflat.inflate(layoutID, null);
        init(context, contentView);
    }

    public void init(Context context, View contentView) {
        setBackgroundResource(R.color.transparent);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        // 设置RelativeLayout布局参数
        LayoutParams p = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        // 设置布局规则
        p.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        LinearLayout ly = layout(context);
        ly.setId(ly.getId());
        // 将实例化对象及布局参数装入RelativeLayout中
        addView(ly, p);
        /***************************** 根据传入LayoutID获取对象并创建内容区域并添加到RelativeLayout中 ******************************/
        // 创建参数
        LayoutParams p1 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        // 添加规则(将内容区域置于TitleBar之下)
        p1.addRule(RelativeLayout.BELOW, ly.getId());
        addView(contentView, p1);
        title_relative = (RelativeLayout) ly.findViewById(relativelayoutid);
        leftButton = (Button) ly.findViewById(leftButtonId);
        rightButton = (Button) ly.findViewById(rightButtonId);
        titleText = (TextView) ly.findViewById(textViewId);

        leftButton.setBackgroundResource(R.color.transparent);
        rightButton.setBackgroundResource(R.color.transparent);
        this.setFocusable(true);
        this.setClickable(true);
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        if (!TextUtils.isEmpty(title))
            titleText.setText(title);
    }

    /**
     * 创建顶部布局
     *
     * @param context
     * @return
     */
    public LinearLayout layout(Context context) {
        LinearLayout linearLayout = new LinearLayout(context);
        // 设定线性布局的方向为竖直方向
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        // 设定线性布局的填充方式为自适应
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.title_heigh));
        // 此处相当于布局文件中的Android:layout_gravity属性
        params.gravity = Gravity.TOP;
        linearLayout.setLayoutParams(params);
        linearLayout.setBackgroundColor(getResources().getColor(R.color.white));
        // 设定线性布局内的对齐方式为控件水平居中
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setId(linearLayoutid);

        RelativeLayout relativelayout = new RelativeLayout(context);
        relativelayout.setBackgroundResource(R.color.white);
        LinearLayout.LayoutParams relativelayout_params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, (int) getResources().getDimension(
                R.dimen.title_heigh));
        // 设定线性布局的填充方式为自适应
        relativelayout.setLayoutParams(relativelayout_params);
        relativelayout.setId(relativelayoutid);
        linearLayout.addView(relativelayout, relativelayout_params);

        // 左边按钮

        Button leftButton = new Button(context);
        LayoutParams leftButton_params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        leftButton_params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        leftButton_params.addRule(RelativeLayout.CENTER_VERTICAL);
        leftButton.setGravity(Gravity.CENTER);
        leftButton.setVisibility(View.VISIBLE);
        leftButton.setId(leftButtonId);
        leftButton.setTextSize(16);
        leftButton.setPadding(20, 20, 20, 20);
        leftButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.black_back, 0, 0, 0);
        relativelayout.addView(leftButton, leftButton_params);
        // 右边按钮
        Button rightButton = new Button(context);
        LayoutParams rightButton_params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT);
        rightButton_params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        rightButton_params.addRule(RelativeLayout.CENTER_VERTICAL);
        rightButton.setGravity(Gravity.CENTER);
       // rightButton.setPadding(20, 20, 20, 20);
        rightButton.setTextColor(getResources().getColor(R.color.text_primary_gray));
        rightButton.setMarqueeRepeatLimit(-1);
        rightButton.setId(rightButtonId);
        relativelayout.addView(rightButton, rightButton_params);
        // 中间文字
        TextView textView = new TextView(context);
        LayoutParams tv_params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        // 此处相当于布局文件中的Android:layout_gravity属性
        tv_params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(context.getResources().getColor(R.color.text_primary_gray));
        textView.setId(textViewId);
        textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textView.setSingleLine(true);
        textView.setFocusable(true);
        textView.setFocusableInTouchMode(true);
        textView.setMarqueeRepeatLimit(-1);
        textView.setVisibility(View.VISIBLE);
        textView.setTextSize(20);
        relativelayout.addView(textView, tv_params);

        //下划线
        TextView linsTextView = new TextView(context);
        LinearLayout.LayoutParams layoutParams = new PercentLinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
        layoutParams.setMargins(0, ForPxTp.dip2px(context, 5), 0, 0);
        linsTextView.setLayoutParams(layoutParams);
        linsTextView.setBackgroundColor(getResources().getColor(R.color.radio_nor));
        linearLayout.addView(linsTextView);

        return linearLayout;
    }
}
