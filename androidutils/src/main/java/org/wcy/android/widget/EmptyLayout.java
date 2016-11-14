package org.wcy.android.widget;

import android.widget.Button;
import org.wcy.android.R;
import org.wcy.android.utils.LogUtil;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class EmptyLayout {
    View childView;
    private Context mContext;
    private ViewGroup mLoadingView;
    private ViewGroup mEmptyView;
    private ViewGroup mErrorView;
    private Animation mLoadingAnimation;
    private boolean mViewsAdded;
    private OnClickListener mErrorClickListener;
    ViewGroup mainview;
    View loadingAnimationView;
    private String message;
    // ---------------------------
    // static variables
    // ---------------------------
    /**
     * The empty state
     */
    private final int TYPE_EMPTY = 1;
    /**
     * The loading state
     */
    private final int TYPE_LOADING = 2;
    /**
     * The error state
     */
    private final int TYPE_ERROR = 3;
    private final int TYPE_VIEW = 4;
    private int mEmptyType = TYPE_LOADING;


    // ---------------------------
    // private methods
    // ---------------------------

    private void changeEmptyType() {
        setDefaultValues();
        // insert views in the root view
        if (!mViewsAdded) {
            LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
            lp.addRule(RelativeLayout.CENTER_VERTICAL);
            if (mEmptyView != null)
                mainview.addView(mEmptyView, lp);
            if (mLoadingView != null)
                mainview.addView(mLoadingView, lp);
            if (mErrorView != null)
                mainview.addView(mErrorView, lp);
            mViewsAdded = true;
        }
        View loadingAnimationView = mLoadingView.findViewById(R.id.imageViewLoading);
        TextView message_tv;
        switch (mEmptyType) {
            case TYPE_EMPTY:
                if (childView != null)
                    childView.setVisibility(View.GONE);
                if (mEmptyView != null) {
                    mEmptyView.setVisibility(View.VISIBLE);
                    message_tv = (TextView) mEmptyView.findViewById(R.id.textViewMessage);
                    Button button = (Button) mEmptyView.findViewById(R.id.found_the_goods);
                    if (message != null && !message.equals("")) {
                        message_tv.setText(message);
                    } else {
                        message_tv.setText(mContext.getString(R.string.empty_message));
                    }
                    if (Type == true) {
                        button.setVisibility(View.VISIBLE);
                        button.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //去发现的监听
                                if (foundClick != null) {
                                    foundClick.onFoundCilck();
                                }
                            }
                        });
                    }
                }
                if (mErrorView != null)
                    mErrorView.setVisibility(View.GONE);
                if (mLoadingView != null) {
                    mLoadingView.setVisibility(View.GONE);
                    if (loadingAnimationView != null && loadingAnimationView.getAnimation() != null)
                        loadingAnimationView.getAnimation().cancel();
                }
                break;
            case TYPE_ERROR:
                if (childView != null)
                    childView.setVisibility(View.GONE);
                if (mEmptyView != null)
                    mEmptyView.setVisibility(View.GONE);
                if (mErrorView != null) {
                    mErrorView.setVisibility(View.VISIBLE);
                    Button reload = (Button) mErrorView.findViewById(R.id.reload);
                    reload.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (reloadOnclick != null) {
                                reloadOnclick.reload();
                            }
                        }
                    });
                    message_tv = (TextView) mErrorView.findViewById(R.id.textViewMessage);
                    if (message != null && !message.equals("")) {
                        message_tv.setText(message);
                    } else {
                        message_tv.setText(mContext.getString(R.string.error_message));
                    }
                }
                if (mLoadingView != null) {
                    mLoadingView.setVisibility(View.GONE);
                    if (loadingAnimationView != null && loadingAnimationView.getAnimation() != null)
                        loadingAnimationView.getAnimation().cancel();
                }
                break;
            case TYPE_LOADING:
                if (childView != null)
                    childView.setVisibility(View.GONE);
                if (mEmptyView != null)
                    mEmptyView.setVisibility(View.GONE);
                if (mErrorView != null)
                    mErrorView.setVisibility(View.GONE);
                if (mLoadingView != null) {
                    mLoadingView.setVisibility(View.VISIBLE);
                    if (mLoadingAnimation != null && loadingAnimationView != null) {
                        loadingAnimationView.startAnimation(mLoadingAnimation);
                    } else if (loadingAnimationView != null) {
                        loadingAnimationView.startAnimation(getRotateAnimation());
                    }
                    message_tv = (TextView) mLoadingView.findViewById(R.id.textViewMessage);
                    if (message != null && !message.equals("")) {
                        message_tv.setText(message);
                    } else {
                        message_tv.setText(mContext.getString(R.string.loading_message));
                    }
                }
                break;
            case TYPE_VIEW:
                if (mEmptyView != null) {
                    mEmptyView.setVisibility(View.GONE);
                    LogUtil.i(EmptyLayout.class, "mEmptyView==GONE");
                }
                if (mErrorView != null)
                    mErrorView.setVisibility(View.GONE);
                if (mLoadingView != null) {
                    mLoadingView.setVisibility(View.GONE);
                    if (mLoadingAnimation != null && loadingAnimationView != null) {
                        loadingAnimationView.startAnimation(mLoadingAnimation);
                    } else if (loadingAnimationView != null) {
                        loadingAnimationView.startAnimation(getRotateAnimation());
                    }
                }
                if (childView != null)
                    childView.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }

    }

    private ReloadOnClick reloadOnclick;

    public void setReloadOnclick(ReloadOnClick reloadOnclick) {
        this.reloadOnclick = reloadOnclick;
    }

    public interface ReloadOnClick {
        public void reload();
    }

    private void setDefaultValues() {
        if (mEmptyView == null) {
            mEmptyView = (ViewGroup) View.inflate(mContext, R.layout.view_empty, null);
            mEmptyView.setFocusable(true);
            mEmptyView.setClickable(true);
            if (mErrorClickListener != null) {
                mEmptyView.setOnClickListener(mErrorClickListener);
            }
        }
        if (mLoadingView == null) {
            mLoadingView = (ViewGroup) View.inflate(mContext, R.layout.view_loading, null);
        }
        if (mErrorView == null) {
            mErrorView = (ViewGroup) View.inflate(mContext, R.layout.view_error, null);
            if (mErrorClickListener != null) {
                mErrorView.setFocusable(true);
                mErrorView.setClickable(true);
                mErrorView.setOnClickListener(mErrorClickListener);
            }
        }
    }

    private static Animation getRotateAnimation() {
        final RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, .5f,
                Animation.RELATIVE_TO_SELF, .5f);
        rotateAnimation.setDuration(1500);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        return rotateAnimation;
    }

    // ---------------------------
    // public methods
    // ---------------------------

    /**
     * Constructor
     *
     * @param context the context (preferred context is any activity)
     */
    public EmptyLayout(Context context) {
        mContext = context;
    }

    /**
     * Constructor
     *
     * @param context the context (preferred context is any activity)
     * @param view    the list view for which this library is being used
     */
    public EmptyLayout(Context context, View view) {
        mContext = context;
        mainview = (ViewGroup) view;
        childView = mainview.getChildAt(0);
    }

    /**
     * Shows the empty layout if the list is empty
     */
    public void showEmpty(String message) {
        this.message = message;
        this.mEmptyType = TYPE_EMPTY;
        changeEmptyType();
    }


    private OnEmptyFoundClick foundClick;
    private boolean Type = false;

    //zjh
    public void setShowEmptyClick(boolean Type, OnEmptyFoundClick foundClick) {
        this.foundClick = foundClick;
        this.Type = Type;
    }

    /**
     * Shows the empty layout if the list is empty
     */
    public void showEmpty() {
        showEmpty(null);
    }

    public void showView(String message) {
        this.message = message;
        this.mEmptyType = TYPE_VIEW;
        changeEmptyType();
    }

    public void showView() {
        showView(null);
    }

    /**
     * Shows loading layout if the list is empty
     */
    public void showLoading(String message) {
        this.message = message;
        this.mEmptyType = TYPE_LOADING;
        changeEmptyType();
    }

    public void showLoading() {
        showLoading(null);
    }

    /**
     * Shows error layout if the list is empty
     */
    public void showError(String message) {
        this.message = message;
        this.mEmptyType = TYPE_ERROR;
        changeEmptyType();
    }

    public void showError() {
        showError(null);
    }
}
