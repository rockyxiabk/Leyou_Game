package com.leyou.game.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Description : 文字倾斜45度 居中显示
 *
 * @author : rocky
 * @Create Time : 2017/11/29 下午12:20
 * @Modified Time : 2017/11/29 下午12:20
 */
public class TextViewRotate45 extends TextView {
    public TextViewRotate45(Context context) {
        super(context);
    }

    public TextViewRotate45(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextViewRotate45(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //倾斜度45,上下左右居中
        canvas.rotate(-45, getMeasuredWidth() / 2, getMeasuredHeight() / 2);
        super.onDraw(canvas);
    }
}
