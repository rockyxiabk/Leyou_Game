package com.leyou.game.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Description : 禁止做切换时闪屏效果
 *
 * @author : rocky
 * @Create Time : 2017/4/12 下午5:08
 * @Modified Time : 2017/4/12 下午5:08
 */
public class IndexViewPager extends ViewPager {
    private boolean isCanScroll = true;

    public IndexViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IndexViewPager(Context context) {
        super(context);
    }

    public void setCanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (isCanScroll) {
            return super.onTouchEvent(arg0);
        } else {
            return false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (isCanScroll) {
            return super.onInterceptTouchEvent(arg0);
        } else {
            return false;
        }

    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item, false);
    }
}