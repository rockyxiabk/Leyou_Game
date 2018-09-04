package com.leyou.game.widget;


import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Description : com.rocky.pullview
 * 模拟下拉组件
 *
 * @author : rocky
 * @Create Time : 2017/10/16 下午1:54
 * @Modified Time : 2017/10/16 下午1:54
 */

public final class ScrollPullView extends ViewGroup {

    private int mLastY;
    private Context mContext;
    private Scroller mScroller;
    private int mChildCount;//子View的个数

    public ScrollPullView(Context context) {
        this(context, null);
    }

    public ScrollPullView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        mContext = context;
        initView();
    }

    private void initView() {
        mScroller = new Scroller(mContext);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int y = (int) event.getY();
        switch (event.getAction()) {
            //手指按下时,初始化按下位置的X,Y位置值
            case MotionEvent.ACTION_DOWN:
                mLastY = y;
                break;
            //计算滑动的偏移量,产生滑动效果
            case MotionEvent.ACTION_MOVE:
                //手指向下滑动delayY>0，向上滑动delayY<0
                int delayY = y - mLastY;
                delayY = delayY * -1;
                scrollBy(0, delayY);
                break;
            case MotionEvent.ACTION_UP:
                /**
                 * scrollY是指:View的上边缘和View内容的上边缘(其实就是第一个ChildView的上边缘)的距离
                 * scrollY=上边缘-View内容上边缘，scrollTo/By方法滑动的知识View的内容
                 * 往下滑动scrollY是负值
                 */
                int scrollY = getScrollY();
                smoothScrollByScroller(scrollY);
//                smoothScrollByAnim(scrollY);
//                smoothScrollByHandler(scrollY);
                break;
        }
        mLastY = y;
        return true;
    }

    /**
     * 执行滑动效果
     * 使用scroller实现
     *
     * @param dy
     */
    private void smoothScrollByScroller(int dy) {
        mScroller.startScroll(0, dy, 0, dy * -1, 1000);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    /**
     * 使用动画来实现
     *
     * @param dy
     */
    private void smoothScrollByAnim(int dy) {
        final float delayY = dy;
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 1).setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //计算动画完成的百分比
                float percent = animation.getAnimatedFraction();
                float dy = (1.0f - percent) * delayY;
                scrollTo(0, (int) dy);
            }
        });
        valueAnimator.start();
    }

    private int count;
    private int delayY;

    /**
     * 使用Handler来实现
     *
     * @param dy
     */
    private void smoothScrollByHandler(int dy) {
        delayY = dy;
        count = 0;
        scrollHandler.sendEmptyMessageDelayed(0, 20);
    }

    private Handler scrollHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    count++;
                    if (count <= 50) {
                        float percent = count / 50.0f;
                        int scrollY = (int) (delayY * (1.0f - percent));
                        Log.d("scrollY:", String.valueOf(scrollY));
                        scrollTo(0, scrollY);
                        scrollHandler.sendEmptyMessageDelayed(0, 20);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 重新计算子View的高度和宽度
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth;
        int measureHeight;
        mChildCount = getChildCount();
        //测量子View
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int widthSpaceSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthSpaceMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpaceSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightSpaceMode = MeasureSpec.getMode(heightMeasureSpec);

        //获取横向的padding值
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        final View childView = getChildAt(0);
        /**
         * 如果子View的数量是0,就读取LayoutParams中数据
         * 否则就对子View进行测量
         * 此处主要是针对wrap_content这种模式进行处理，因为默认情况下
         * wrap_content等于match_parent
         */
        if (mChildCount == 0) {
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            if (layoutParams != null) {
                setMeasuredDimension(layoutParams.width, layoutParams.height);
            } else {
                setMeasuredDimension(0, 0);
            }
        } else if (heightSpaceMode == MeasureSpec.AT_MOST && widthSpaceMode == MeasureSpec.AT_MOST) {
            measuredWidth = childView.getMeasuredWidth() * mChildCount;
            measureHeight = getChildMaxHeight();
            //将两侧的padding值加上去
            measuredWidth = paddingLeft + measuredWidth + paddingRight;
            setMeasuredDimension(measuredWidth, measureHeight);
        } else if (heightSpaceMode == MeasureSpec.AT_MOST) {
            measureHeight = getChildMaxHeight();
            setMeasuredDimension(widthSpaceSize, measureHeight);
        } else if (widthSpaceMode == MeasureSpec.AT_MOST) {
            measuredWidth = childView.getMeasuredWidth() * mChildCount;
            measuredWidth = paddingLeft + measuredWidth + paddingRight;
            setMeasuredDimension(measuredWidth, heightSpaceSize);
        }
    }


    /**
     * 获取子View中最大高度
     *
     * @return
     */
    private int getChildMaxHeight() {
        int maxHeight = 0;
        for (int i = 0; i < mChildCount; i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() != View.GONE) {
                int height = childView.getMeasuredHeight();
                if (height > maxHeight) {
                    maxHeight = height;
                }
            }
        }
        return maxHeight;
    }


    /**
     * 设置子View的布局
     *
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childLeft = 0;
        for (int i = 0; i < mChildCount; i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() != View.GONE) {
                int childWidth = childView.getMeasuredWidth();
                childView.layout(childLeft, 0, childLeft + childWidth, childView.getMeasuredHeight());
                childLeft += childWidth;
            }
        }
    }
}