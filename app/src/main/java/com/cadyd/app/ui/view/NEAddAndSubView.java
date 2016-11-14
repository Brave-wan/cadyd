package com.cadyd.app.ui.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.cadyd.app.R;

/**
 * 数量加减
 *
 * @author wcy
 */
public class NEAddAndSubView extends LinearLayout {
    AlertAddSubConfirmation alert;
    Context context;
    OnNumChangeListener onNumChangeListener;
    TextView addButton;
    TextView subButton;
   public   TextView editText;
    int num; // editText中的数值
    int maxNum = -1;

    public NEAddAndSubView(Context context) {
        super(context);
        this.context = context;
        num = 0;
        control();
    }

    /**
     * 带初始数据实例化
     *
     * @param context
     */
    public NEAddAndSubView(Context context, int num) {
        super(context);
        this.context = context;
        this.num = num;
        control();
    }

    /**
     * 从XML中实例化
     */
    public NEAddAndSubView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        num = 0;
        control();
    }

    /**
     *
     */
    private void control() {
        initialise(); // 实例化内部view
        setViewListener();
    }


    /**
     * 实例化内部View
     */
    private void initialise() {
        View view = LayoutInflater.from(context).inflate(R.layout.ne_add_and_sub_layout, null);
        addButton = (TextView) view.findViewById(R.id.add);
        subButton = (TextView) view.findViewById(R.id.sub);
        editText = (TextView) view.findViewById(R.id.number);
        addButton.setTag("+");
        subButton.setTag("-");
        view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(view);
    }


    /**
     * 设置editText中的值
     *
     * @param num
     */
    public void setNum(int num) {
        this.num = num;
        String s = String.valueOf(num);
        editText.setText(s);
    }

    /**
     * 获取editText中的值
     *
     * @return
     */
    public int getNum() {
        if (editText.getText().toString() != null) {
            return Integer.parseInt(editText.getText().toString());
        } else {
            return 0;
        }
    }

    /**
     * 设置输入框中的字体大小
     *
     * @param spValue 字体大小SP
     */
    public void setTextSize(int spValue) {
        editText.setTextSize(spValue);
    }


    /**
     * 设置按钮背景色
     *
     * @param addBtnColor 加号背景色
     * @param subBtnColor 减号背景色
     */
    public void setButtonBgColor(int addBtnColor, int subBtnColor) {
        addButton.setBackgroundColor(addBtnColor);
        subButton.setBackgroundColor(subBtnColor);
    }

    /**
     * 设置EditText文本变化监听
     *
     * @param onNumChangeListener
     */
    public void setOnNumChangeListener(OnNumChangeListener onNumChangeListener) {
        this.onNumChangeListener = onNumChangeListener;
    }

    /**
     * 设置文本变化相关监听事件
     */
    private void setViewListener() {
        addButton.setOnClickListener(new OnButtonClickListener());
        subButton.setOnClickListener(new OnButtonClickListener());
        editText.addTextChangedListener(new OnTextChangeListener());
    }

    /**
     * 加减按钮事件监听器
     *
     * @author wcy
     */
    class OnButtonClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            String numString = editText.getText().toString();
            if (numString == null || numString.equals("")) {
                num = 1;
                editText.setText("1");
            } else {
                if (v.getTag().equals("+")) {
                    if (Integer.parseInt(editText.getText().toString()) == maxNum && maxNum != -1) {
                        Toast.makeText(context, "亲，数量最大为" + maxNum + "哦~",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        if (++num < 1) // 先加，再判断
                        {
                            num--;
                            Toast.makeText(context, "亲，数量至少为1哦~",
                                    Toast.LENGTH_SHORT).show();
                            editText.setText("1");
                        } else {
                            editText.setText(String.valueOf(num));
                            if (onNumChangeListener != null) {
                                onNumChangeListener.onNumChange(NEAddAndSubView.this,
                                        num - 1, num);
                            }
                        }
                    }
                } else if (v.getTag().equals("-")) {
                    if (--num < 1) // 先减，再判断
                    {
                        num++;
                        Toast.makeText(context, "亲，数量至少为1哦~",
                                Toast.LENGTH_SHORT).show();
                        editText.setText("1");
                    } else {
                        editText.setText(String.valueOf(num));
                        if (onNumChangeListener != null) {
                            onNumChangeListener.onNumChange(NEAddAndSubView.this, num + 1,
                                    num);
                        }
                    }
                }
            }
        }
    }

    public void sub() {
        editText.setText(String.valueOf(num--));
    }

    public void add() {
        editText.setText(String.valueOf(num++));
    }

    /**
     * EditText输入变化事件监听器
     *
     * @author wcy
     */
    class OnTextChangeListener implements TextWatcher {

        @Override
        public void afterTextChanged(Editable s) {
            String numString = s.toString();
            if (numString == null || numString.equals("")) {
                num = 1;
            } else {
                int numInt = Integer.parseInt(numString);
                if (numInt < 1) {
                    Toast.makeText(context, "亲，数量至少为1哦~",
                            Toast.LENGTH_SHORT).show();
                    editText.setText("1");
                } else if (maxNum != -1) {
//                    if (numInt > maxNum) {
//                        Toast.makeText(context, "亲，数量最大为" + maxNum + "哦~",
//                                Toast.LENGTH_SHORT).show();
//                    }
//                    editText.setText(String.valueOf(maxNum));
                } else {
                    // 设置EditText光标位置 为文本末端
//                    editText.setSelection(editText.getText().toString()
//                            .length());
                    num = numInt;
                }
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

        }

    }

    public void setIsEditable(boolean isEditable) {
        editText.setFocusable(isEditable);
        editText.setClickable(isEditable);
        editText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alert == null) {
                    alert = new AlertAddSubConfirmation(context);
                }
                alert.show(num, new AlertAddSubConfirmation.OnClickListeners() {
                    @Override
                    public void ConfirmOnClickListener(int number) {
                        alert.hide();
                        if (onNumChangeListener != null) {
                            int bnum = num;
                            setNum(number);
                            onNumChangeListener.onNumChange(NEAddAndSubView.this,
                                    bnum, number);

                        }
                    }

                    @Override
                    public void CancelOnClickListener() {
                        alert.hide();
                    }
                });
            }
        });
    }

    public int getMaxNum() {
        return maxNum;
    }

    public static final int add_type = 1;
    public static final int sub_type = 2;

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public interface OnNumChangeListener {
        /**
         * 输入框中的数值改变事件
         *
         * @param view 整个AddAndSubView
         * @param num  输入框的数值
         */
        public void onNumChange(View view, int bnum, int num);
    }

    public class TextChangedListener implements TextWatcher {
        private boolean isChanged = false;
        EditText et;

        public TextChangedListener(EditText et) {
            this.et = et;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (isChanged) {// ----->如果字符未改变则返回
                return;
            }
            String str = s.toString();
            StringBuffer cuttedStr = new StringBuffer();
            isChanged = true;
            if (str.equals("") || str.equals("0")) {
                cuttedStr.append("1");
            } else {
                if (Integer.parseInt(str) > maxNum && maxNum != -1) {
                    cuttedStr.append(maxNum);
                } else {
                    cuttedStr.append(str);
                }
            }
            et.setText(cuttedStr.toString());
            et.setSelection(et.length());
            isChanged = false;
        }
    }
}
