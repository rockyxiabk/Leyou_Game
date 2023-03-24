package com.leyou.game.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Scroller;

import com.leyou.game.R;
import com.leyou.game.util.ScreenUtil;

/**
 * Description : 圆形进度
 *
 * @author : rocky
 * @Create Time : 2017/10/28 下午5:03
 * @Modified Time : 2017/10/28 下午5:03
 */
public class CircleProgressView extends View {
    public static final String PROGRESS_PROPERTY = "progress";
    /**
     * 画笔对象的引用
     */
    private Paint[] paints;
    RectF oval;
    /**
     * 圆环的颜色
     */
    private int roundColor;
    /**
     * 圆环的宽度
     */
    private float roundWidth;

    /**
     * 圆环进度的颜色
     */
    private int roundProgressColor;

    /**
     * 移动
     */
    Scroller scroller;

    protected float progress;
    protected float progressText;
    private String msg = "签到";
    private Context context;
    private boolean isSign = false;

    public CircleProgressView(Context context) {
        this(context, null);
        this.context = context;
    }

    public CircleProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;
    }

    public CircleProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs);
        paints = new Paint[4];
        paints[0] = new Paint();
        paints[1]=new TextPaint();
        oval = new RectF();

        TypedArray mTypedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.circleProgressStyle, defStyleAttr, 0);

        roundColor = mTypedArray.getColor(R.styleable.circleProgressStyle_roundColor, Color.GRAY);
        roundWidth = mTypedArray.getDimension(R.styleable.circleProgressStyle_roundWidth, 3);
        roundProgressColor = mTypedArray.getColor(R.styleable.circleProgressStyle_roundProgressColor, Color.RED);
        mTypedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // super.onDraw(canvas);
        float centre = getWidth() / 2; // 获取圆心的x坐标
        float radius = (centre - roundWidth / 2); // 圆环的半径

        paints[0].setColor(roundColor); // 设置圆环的颜色
        paints[0].setStyle(Paint.Style.STROKE); // 设置空心
        paints[0].setStrokeWidth(roundWidth); // 设置圆环的宽度
        paints[0].setAntiAlias(true); // 消除锯齿
        paints[0].setStrokeCap(Paint.Cap.ROUND);// 圆角

        canvas.drawCircle(centre, centre, radius, paints[0]); // 画出圆环

        paints[0].setColor(roundProgressColor);
        // 用于定义的圆弧的形状和大小的界限.指定圆弧的外轮廓矩形区域
        // 椭圆的上下左右四个点(View 左上角为0)
        oval.set(centre - radius, centre - radius, centre + radius, centre + radius);

        //画圆弧
        canvas.drawArc(oval, -90, progress, false, paints[0]);

        /**
         * 画进度百分比的text
         */
        paints[1].setTextAlign(Paint.Align.CENTER);
        paints[1].setColor(roundColor);
        paints[1].setTextSize(ScreenUtil.sp2px(context, 20));
        paints[1].setTypeface(Typeface.DEFAULT_BOLD); // 设置字体
//        float textWidth = paints[1].measureText(msg);//设置TextPaint 不需要在计算文字的坐标
        canvas.drawText(msg, centre ,centre+ScreenUtil.sp2px(context, 10) , paints[1]); // 画出进度百分比
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress * 360 / 100;
        invalidate();// UI thread
        //postInvalidate();//非UI线程
    }

    public void dodo(float progressText, float progress, String msg, boolean isSign) {
        this.progressText = progressText;
        this.progress = progress;
        this.msg = msg;
        this.isSign = isSign;

        //也可使用3.0的AnimationSet实现
//      AnimationSet set = new AnimationSet(true);
//      set.setRepeatCount(AnimationSet.INFINITE);
//      set.setInterpolator(new AccelerateDecelerateInterpolator());
//      set.start();
//      this.setAnimation(set);

        //4.0以上，在AnimationSet基础上封装的，遗憾的是没有Repeat
        AnimatorSet animation = new AnimatorSet();

        ObjectAnimator progressAnimation = ObjectAnimator.ofFloat(this, "progress", 0f, progress);
        progressAnimation.setDuration(1500);
        // 动画执行时间

        /*
         * AccelerateInterpolator　　　　　                 加速，开始时慢中间加速
         * DecelerateInterpolator　　　 　　                减速，开始时快然后减速
         * AccelerateDecelerateInterolator　               先加速后减速，开始结束时慢，中间加速
         * AnticipateInterpolator　　　　　　                反向,先向相反方向改变一段再加速播放
         * AnticipateOvershootInterpolator　                反向加超越，先向相反方向改变，再加速播放，会超出目的值然后缓慢移动至目的值
         * BounceInterpolator　　　　　　　                    跳跃，快到目的值时值会跳跃，如目的值100，后面的值可能依次为85，77，70，80，90，100
         * CycleIinterpolator　　　　　　　　                   循环，动画循环一定次数，值的改变为一正弦函数：Math.sin(2 *
         * mCycles * Math.PI * input) LinearInterpolator　　　 线性，线性均匀改变
         * OvershottInterpolator　　　　　　                  超越，最后超出目的值然后缓慢改变到目的值
         * TimeInterpolator　　　　　　　　　                        一个接口，允许你自定义interpolator，以上几个都是实现了这个接口
         */
        progressAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.playTogether(progressAnimation);//动画同时执行,可以做多个动画
        animation.start();
    }
}