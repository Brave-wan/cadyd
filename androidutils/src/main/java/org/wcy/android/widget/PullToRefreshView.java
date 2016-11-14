package org.wcy.android.widget;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import org.wcy.android.R;

/**
 * 上拉，下拉刷新
 *
 * @author wangchaoyong
 *
 */
public class PullToRefreshView extends LinearLayout {
	@SuppressWarnings("unused")
	private static final String TAG = "PullToRefreshView";
	// refresh states
	private static final int PULL_TO_REFRESH = 2;
	private static final int RELEASE_TO_REFRESH = 3;
	private static final int REFRESHING = 4;
	// pull state
	private static final int PULL_UP_STATE = 0;
	private static final int PULL_DOWN_STATE = 1;

	// id footer
	public final int pull_to_refresh_footer = 11;
	private final int pull_to_load_progress = 12;
	private final int pull_to_load_image = 13;
	private final int pull_to_load_text = 14;
	// id header
	public final int pull_to_refresh_header = 21;
	private final int pull_to_refresh_progress = 22;
	private final int pull_to_refresh_image = 23;
	private final int pull_to_refresh_text = 24;
	/**
	 * last y
	 */
	private int mLastMotionY;
	/**
	 * lock
	 */
	private boolean mLock;
	/**
	 * header view
	 */
	private View mHeaderView;
	/**
	 * footer view
	 */
	private View mFooterView;
	/**
	 * list or grid
	 */
	private AdapterView<?> mAdapterView;
	/**
	 * scrollview
	 */
	private ScrollView mScrollView;
	/**
	 * header view height
	 */
	private int mHeaderViewHeight;
	/**
	 * footer view height
	 */
	private int mFooterViewHeight;
	/**
	 * header view image
	 */
	private ImageView mHeaderImageView;
	/**
	 * footer view image
	 */
	private ImageView mFooterImageView;
	/**
	 * header tip text
	 */
	private TextView mHeaderTextView;
	/**
	 * footer tip text
	 */
	private TextView mFooterTextView;
	/**
	 * header refresh time
	 */
	// private TextView mHeaderUpdateTextView;
	/**
	 * footer refresh time
	 */
	// private TextView mFooterUpdateTextView;
	/**
	 * header progress bar
	 */
	private ProgressBar mHeaderProgressBar;
	/**
	 * footer progress bar
	 */
	private ProgressBar mFooterProgressBar;
	/**
	 * header view current state
	 */
	private int mHeaderState;
	/**
	 * footer view current state
	 */
	private int mFooterState;
	/**
	 * pull state,pull up or pull down;PULL_UP_STATE or PULL_DOWN_STATE
	 */
	private int mPullState;
	/**
	 * 变为向下的箭头,改变箭头方向
	 */
	private RotateAnimation mFlipAnimation;
	/**
	 * 变为逆向的箭头,旋转
	 */
	private RotateAnimation mReverseFlipAnimation;
	/**
	 * footer refresh listener
	 */
	private OnFooterRefreshListener mOnFooterRefreshListener;
	/**
	 * footer refresh listener
	 */
	private OnHeaderRefreshListener mOnHeaderRefreshListener;

	/**
	 * last update time
	 */

	// private String mLastUpdateTime;

	public PullToRefreshView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PullToRefreshView(Context context) {
		super(context);
		init();
	}

	/**
	 * init
	 *
	 * @description
	 * @param context
	 *            hylin 2012-7-26上午10:08:33
	 */
	private void init() {
		// Load all of the animations we need in code rather than through XML
		mFlipAnimation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF,
				0.5f);
		mFlipAnimation.setInterpolator(new LinearInterpolator());
		mFlipAnimation.setDuration(250);
		mFlipAnimation.setFillAfter(true);
		mReverseFlipAnimation = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mReverseFlipAnimation.setInterpolator(new LinearInterpolator());
		mReverseFlipAnimation.setDuration(250);
		mReverseFlipAnimation.setFillAfter(true);
		// header view 在此添加,保证是第一个添加到linearlayout的最上端
		addHeaderView();
	}

	private void addHeaderView() {
		// header view
		mHeaderView = relativelayout(0);

		mHeaderImageView = (ImageView) mHeaderView.findViewById(pull_to_refresh_image);
		mHeaderTextView = (TextView) mHeaderView.findViewById(pull_to_refresh_text);
		// mHeaderUpdateTextView = (TextView) mHeaderView
		// .findViewById(MResource.getByIdName(context, "id",
		// "pull_to_refresh_updated_at"));
		mHeaderProgressBar = (ProgressBar) mHeaderView.findViewById(pull_to_refresh_progress);
		// header layout
		measureView(mHeaderView);
		mHeaderViewHeight = mHeaderView.getMeasuredHeight();
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, mHeaderViewHeight);
		// 设置topMargin的值为负的header View高度,即将其隐藏在最上方
		params.topMargin = -(mHeaderViewHeight);
		// mHeaderView.setLayoutParams(params1);
		addView(mHeaderView, params);

	}

	private void addFooterView() {
		// footer view
		mFooterView = relativelayout(1);
		mFooterImageView = (ImageView) mFooterView.findViewById(pull_to_load_image);
		mFooterTextView = (TextView) mFooterView.findViewById(pull_to_load_text);
		mFooterProgressBar = (ProgressBar) mFooterView.findViewById(pull_to_load_progress);
		// footer layout
		measureView(mFooterView);
		mFooterViewHeight = mFooterView.getMeasuredHeight();
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, mFooterViewHeight);
		// int top = getHeight();
		// params.topMargin
		// =getHeight();//在这里getHeight()==0,但在onInterceptTouchEvent()方法里getHeight()已经有值了,不再是0;
		// getHeight()什么时候会赋值,稍候再研究一下
		// 由于是线性布局可以直接添加,只要AdapterView的高度是MATCH_PARENT,那么footer view就会被添加到最后,并隐藏
		addView(mFooterView, params);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		// footer view 在此添加保证添加到linearlayout中的最后
		addFooterView();
		initContentAdapterView();
	}

	/**
	 * init AdapterView like ListView,GridView and so on;or init ScrollView
	 *
	 * @description hylin 2012-7-30下午8:48:12
	 */
	private void initContentAdapterView() {
		int count = getChildCount();
		if (count < 3) {
			throw new IllegalArgumentException(
					"this layout must contain 3 child views,and AdapterView or ScrollView must in the second position!");
		}
		View view = null;
		for (int i = 0; i < count - 1; ++i) {
			view = getChildAt(i);
			if (view instanceof AdapterView<?>) {
				mAdapterView = (AdapterView<?>) view;
			}
			if (view instanceof ScrollView) {
				// finish later
				mScrollView = (ScrollView) view;
			}
		}
		if (mAdapterView == null && mScrollView == null) {
			throw new IllegalArgumentException("must contain a AdapterView or ScrollView in this layout!");
		}
	}

	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}

		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent e) {
		int y = (int) e.getRawY();
		switch (e.getAction()) {
			case MotionEvent.ACTION_DOWN:
				// 首先拦截down事件,记录y坐标
				mLastMotionY = y;
				break;
			case MotionEvent.ACTION_MOVE:
				// deltaY > 0 是向下运动,< 0是向上运动
				int deltaY = y - mLastMotionY;
				if (isRefreshViewScroll(deltaY)) {
					return true;
				}
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				break;
		}
		return false;
	}

	/*
	 * 如果在onInterceptTouchEvent()方法中没有拦截(即onInterceptTouchEvent()方法中 return
	 * false)则由PullToRefreshView 的子View来处理;否则由下面的方法来处理(即由PullToRefreshView自己来处理)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mLock) {
			return true;
		}
		int y = (int) event.getRawY();
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				// onInterceptTouchEvent已经记录
				// mLastMotionY = y;
				break;
			case MotionEvent.ACTION_MOVE:
				int deltaY = y - mLastMotionY;
				if (mPullState == PULL_DOWN_STATE) {
					if (mOnHeaderRefreshListener != null) {
						// PullToRefreshView执行下拉
						// Log.i(TAG, " pull down!parent view move!");
						headerPrepareToRefresh(deltaY);
					} else {
						return true;
					}
				} else if (mPullState == PULL_UP_STATE) {
					// PullToRefreshView执行上拉
					// Log.i(TAG, "pull up!parent view move!");
					if (mOnFooterRefreshListener != null) {
						footerPrepareToRefresh(deltaY);
					} else {
						return true;
					}

				}
				mLastMotionY = y;
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				int topMargin = getHeaderTopMargin();
				if (mPullState == PULL_DOWN_STATE) {
					if (topMargin >= 0) {
						if (mOnHeaderRefreshListener != null) {
							// 开始刷新
							headerRefreshing();
						} else {
							return true;
						}

					} else {
						// 还没有执行刷新，重新隐藏
						setHeaderTopMargin(-mHeaderViewHeight);
					}
				} else if (mPullState == PULL_UP_STATE) {
					if (Math.abs(topMargin) >= mHeaderViewHeight + mFooterViewHeight) {
						if (mOnFooterRefreshListener != null) {
							// 开始执行footer 刷新
							footerRefreshing();
						} else {
							return true;
						}
					} else {
						// 还没有执行刷新，重新隐藏
						setHeaderTopMargin(-mHeaderViewHeight);
					}
				}
				break;
		}
		return super.onTouchEvent(event);
	}

	/**
	 * 是否应该到了父View,即PullToRefreshView滑动
	 *
	 * @param deltaY
	 *            , deltaY > 0 是向下运动,< 0是向上运动
	 * @return
	 */
	private boolean isRefreshViewScroll(int deltaY) {
		if (mHeaderState == REFRESHING || mFooterState == REFRESHING) {
			return false;
		}
		// 对于ListView和GridView
		if (mAdapterView != null) {
			// 子view(ListView or GridView)滑动到最顶端
			if (deltaY > 0) {

				View child = mAdapterView.getChildAt(0);
				if (child == null) {
					mPullState = PULL_DOWN_STATE;
					return true;
				}
				if (mAdapterView.getFirstVisiblePosition() == 0 && child.getTop() == 0) {
					mPullState = PULL_DOWN_STATE;
					return true;
				}
				int top = child.getTop();
				int padding = mAdapterView.getPaddingTop();
				if (mAdapterView.getFirstVisiblePosition() == 0 && Math.abs(top - padding) <= 8) {// 这里之前用3可以判断,但现在不行,还没找到原因
					mPullState = PULL_DOWN_STATE;
					return true;
				}

			} else if (deltaY < 0) {
				View lastChild = mAdapterView.getChildAt(mAdapterView.getChildCount() - 1);
				if (lastChild == null) {
					mPullState = PULL_UP_STATE;
					return true;
				}
				// 最后一个子view的Bottom小于父View的高度说明mAdapterView的数据没有填满父view,
				// 等于父View的高度说明mAdapterView已经滑动到最后
				if (lastChild.getBottom() <= getHeight() && mAdapterView.getLastVisiblePosition() == mAdapterView.getCount() - 1) {
					mPullState = PULL_UP_STATE;
					return true;
				}
			}
		}
		// 对于ScrollView
		if (mScrollView != null) {
			// 子scroll view滑动到最顶端
			View child = mScrollView.getChildAt(0);
			if (deltaY > 0 && mScrollView.getScrollY() == 0) {
				mPullState = PULL_DOWN_STATE;
				return true;
			} else if (deltaY < 0 && child.getMeasuredHeight() <= getHeight() + mScrollView.getScrollY()) {
				mPullState = PULL_UP_STATE;
				return true;
			}
		}
		return false;
	}

	/**
	 * header 准备刷新,手指移动过程,还没有释放
	 *
	 * @param deltaY
	 *            ,手指滑动的距离
	 */
	private void headerPrepareToRefresh(int deltaY) {
		int newTopMargin = changingHeaderViewTopMargin(deltaY);
		// 当header view的topMargin>=0时，说明已经完全显示出来了,修改header view 的提示状态
		if (newTopMargin >= 0 && mHeaderState != RELEASE_TO_REFRESH) {
			mHeaderTextView.setText("松开后刷新");
			// mHeaderUpdateTextView.setVisibility(View.VISIBLE);
			mHeaderImageView.clearAnimation();
			mHeaderImageView.startAnimation(mFlipAnimation);
			mHeaderState = RELEASE_TO_REFRESH;
		} else if (newTopMargin < 0 && newTopMargin > -mHeaderViewHeight) {// 拖动时没有释放
			mHeaderImageView.clearAnimation();
			mHeaderImageView.startAnimation(mFlipAnimation);
			// mHeaderImageView.
			mHeaderTextView.setText("下拉刷新");
			mHeaderState = PULL_TO_REFRESH;
		}
	}

	/**
	 * footer 准备刷新,手指移动过程,还没有释放 移动footer view高度同样和移动header view
	 * 高度是一样，都是通过修改header view的topmargin的值来达到
	 *
	 * @param deltaY
	 *            ,手指滑动的距离
	 */
	private void footerPrepareToRefresh(int deltaY) {
		int newTopMargin = changingHeaderViewTopMargin(deltaY);
		// 如果header view topMargin 的绝对值大于或等于header + footer 的高度
		// 说明footer view 完全显示出来了，修改footer view 的提示状态
		if (Math.abs(newTopMargin) >= (mHeaderViewHeight + mFooterViewHeight) && mFooterState != RELEASE_TO_REFRESH) {
			mFooterView.setVisibility(View.VISIBLE);
			mHeaderImageView.setImageResource(R.drawable.ic_pulltorefresh_arrow);
			mFooterTextView.setText("松开后加载");
			mFooterImageView.clearAnimation();
			mFooterImageView.startAnimation(mFlipAnimation);
			mFooterState = RELEASE_TO_REFRESH;
		} else if (Math.abs(newTopMargin) < (mHeaderViewHeight + mFooterViewHeight)) {
			mFooterImageView.clearAnimation();
			mFooterImageView.startAnimation(mFlipAnimation);
			mFooterTextView.setText("上拉加载更多");
			mFooterState = PULL_TO_REFRESH;
		}
	}

	/**
	 * 修改Header view top margin的值
	 *
	 * @description
	 * @param deltaY
	 * @return hylin 2012-7-31下午1:14:31
	 */
	private int changingHeaderViewTopMargin(int deltaY) {
		LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
		float newTopMargin = params.topMargin + deltaY * 0.3f;
		// 这里对上拉做一下限制,因为当前上拉后然后不释放手指直接下拉,会把下拉刷新给触发了,感谢网友yufengzungzhe的指出
		// 表示如果是在上拉后一段距离,然后直接下拉
		if (deltaY > 0 && mPullState == PULL_UP_STATE && Math.abs(params.topMargin) <= mHeaderViewHeight) {
			return params.topMargin;
		}
		// 同样地,对下拉做一下限制,避免出现跟上拉操作时一样的bug
		if (deltaY < 0 && mPullState == PULL_DOWN_STATE && Math.abs(params.topMargin) >= mHeaderViewHeight) {
			return params.topMargin;
		}
		params.topMargin = (int) newTopMargin;
		mHeaderView.setLayoutParams(params);
		invalidate();
		return params.topMargin;
	}

	/**
	 * header refreshing
	 *
	 * @description hylin 2012-7-31上午9:10:12
	 */
	private void headerRefreshing() {
		mHeaderState = REFRESHING;
		setHeaderTopMargin(0);
		mHeaderImageView.setVisibility(View.GONE);
		mHeaderImageView.clearAnimation();
		mHeaderImageView.setImageDrawable(null);
		mHeaderProgressBar.setVisibility(View.VISIBLE);
		mHeaderTextView.setText("加载中......");
		if (mOnHeaderRefreshListener != null) {
			mOnHeaderRefreshListener.onHeaderRefresh(this);
		}
	}

	/**
	 * footer refreshing
	 *
	 * @description hylin 2012-7-31上午9:09:59
	 */
	private void footerRefreshing() {
		mFooterState = REFRESHING;
		int top = mHeaderViewHeight + mFooterViewHeight;
		setHeaderTopMargin(-top);
		mFooterView.setVisibility(View.VISIBLE);
		mFooterImageView.setVisibility(View.GONE);
		mFooterImageView.clearAnimation();
		mFooterImageView.setImageDrawable(null);
		mFooterProgressBar.setVisibility(View.VISIBLE);
		mFooterTextView.setText("加载中......");
		if (mOnFooterRefreshListener != null) {
			mOnFooterRefreshListener.onFooterRefresh(this);
		}
	}

	/**
	 * 设置header view 的topMargin的值
	 *
	 * @description
	 * @param topMargin
	 *            ，为0时，说明header view 刚好完全显示出来； 为-mHeaderViewHeight时，说明完全隐藏了
	 *            hylin 2012-7-31上午11:24:06
	 */
	private void setHeaderTopMargin(int topMargin) {
		LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
		params.topMargin = topMargin;
		mHeaderView.setLayoutParams(params);
		invalidate();
	}

	/**
	 * header view 完成更新后恢复初始状态
	 *
	 * @description hylin 2012-7-31上午11:54:23
	 */
	public void onHeaderRefreshComplete() {
		setHeaderTopMargin(-mHeaderViewHeight);
		mHeaderImageView.setVisibility(View.VISIBLE);
		mHeaderImageView.setImageResource(R.drawable.ic_pulltorefresh_arrow);
		mHeaderTextView.setText("下拉刷新");
		mHeaderProgressBar.setVisibility(View.GONE);
		// mHeaderUpdateTextView.setText("");
		mHeaderState = PULL_TO_REFRESH;
	}

	/**
	 * Resets the list to a normal state after a refresh.
	 *
	 * @param lastUpdated
	 *            Last updated at.
	 */
	public void onHeaderRefreshComplete(CharSequence lastUpdated) {
		// setLastUpdated(lastUpdated);
		onHeaderRefreshComplete();
	}

	/**
	 * footer view 完成更新后恢复初始状态
	 */
	public void onFooterRefreshComplete() {
		setHeaderTopMargin(-mHeaderViewHeight);
		mFooterImageView.setVisibility(View.VISIBLE);
		mFooterImageView.setImageResource(R.drawable.ic_pulltorefresh_arrow_up);
		mFooterTextView.setText("上拉加载更多");
		mFooterProgressBar.setVisibility(View.GONE);
		// mHeaderUpdateTextView.setText("");
		mFooterState = PULL_TO_REFRESH;
	}

	/**
	 * Set a text to represent when the list was last updated.
	 *
	 * @param lastUpdated
	 *            Last updated at.
	 */
	// public void setLastUpdated(CharSequence lastUpdated) {
	// if (lastUpdated != null) {
	// mHeaderUpdateTextView.setVisibility(View.VISIBLE);
	// mHeaderUpdateTextView.setText(lastUpdated);
	// } else {
	// mHeaderUpdateTextView.setVisibility(View.GONE);
	// }
	// }

	/**
	 * 获取当前header view 的topMargin
	 *
	 * @description
	 * @return hylin 2012-7-31上午11:22:50
	 */
	private int getHeaderTopMargin() {
		LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
		return params.topMargin;
	}

	/**
	 * lock
	 *
	 * @description hylin 2012-7-27下午6:52:25
	 */
	@SuppressWarnings("unused")
	private void lock() {
		mLock = true;
	}

	/**
	 * unlock
	 *
	 * @description hylin 2012-7-27下午6:53:18
	 */
	@SuppressWarnings("unused")
	private void unlock() {
		mLock = false;
	}

	/**
	 * set headerRefreshListener
	 *
	 * @description
	 * @param headerRefreshListener
	 *            hylin 2012-7-31上午11:43:58
	 */
	public void setOnHeaderRefreshListener(OnHeaderRefreshListener headerRefreshListener) {
		mOnHeaderRefreshListener = headerRefreshListener;
	}

	public void setOnFooterRefreshListener(OnFooterRefreshListener footerRefreshListener) {
		mOnFooterRefreshListener = footerRefreshListener;
	}

	/**
	 * Interface definition for a callback to be invoked when list/grid footer
	 * view should be refreshed.
	 */
	public interface OnFooterRefreshListener {
		public void onFooterRefresh(PullToRefreshView view);
	}

	/**
	 * Interface definition for a callback to be invoked when list/grid header
	 * view should be refreshed.
	 */
	public interface OnHeaderRefreshListener {
		public void onHeaderRefresh(PullToRefreshView view);
	}

	private RelativeLayout relativelayout(int location) {

		RelativeLayout relativelayout = new RelativeLayout(getContext());
		// 设定线性布局的填充方式为自适应
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		relativelayout.setBackgroundColor(Color.TRANSPARENT);
		// 设定线性布局内的对齐方式为控件水平居中
		relativelayout.setGravity(Gravity.CENTER);
		relativelayout.setLayoutParams(params);
		relativelayout.setBackgroundColor(Color.TRANSPARENT);

		LinearLayout linearLayout = new LinearLayout(getContext());
		RelativeLayout.LayoutParams linearLayout_params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		linearLayout_params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		// 设定线性布局的方向为竖直方向
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);
		linearLayout.setBackgroundColor(Color.TRANSPARENT);
		linearLayout.setGravity(Gravity.CENTER_VERTICAL);
		linearLayout.setLayoutParams(linearLayout_params);
		linearLayout.setBackgroundColor(Color.TRANSPARENT);

		ProgressBar pb = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleSmall);
		pb.setLayoutParams(linearLayout_params);

		pb.setVisibility(View.GONE);

		ImageView iv = new ImageView(getContext());
		iv.setLayoutParams(linearLayout_params);
		iv.setVisibility(View.VISIBLE);
		iv.setImageResource(R.drawable.ic_pulltorefresh_arrow_up);
		TextView tv = new TextView(getContext());
		linearLayout_params.leftMargin=30;
		tv.setLayoutParams(linearLayout_params);
		// // 字体加粗黑体
		// TextPaint tp = tv.getPaint();
		// tp.setFakeBoldText(true);

		tv.setTextColor(Color.BLACK);
		tv.setVisibility(View.VISIBLE);
		tv.setText("上拉加载更多");
		if (location == 0) {
			relativelayout.setId(pull_to_refresh_header);
			pb.setId(pull_to_refresh_progress);
			iv.setId(pull_to_refresh_image);
			tv.setId(pull_to_refresh_text);
		} else {
			relativelayout.setId(pull_to_refresh_footer);
			relativelayout.setVisibility(View.GONE);
			pb.setId(pull_to_load_progress);
			iv.setId(pull_to_load_image);
			tv.setId(pull_to_load_text);
		}

		linearLayout.addView(pb);
		linearLayout.addView(iv);
		linearLayout.addView(tv);

		relativelayout.addView(linearLayout, linearLayout_params);
		// 设定线性布局的方向为竖直方向
		return relativelayout;
	}
}