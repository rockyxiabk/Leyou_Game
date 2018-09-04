package com.leyou.game.widget.treasury;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Description : 宝库模块 特有弹窗自定义字体
 *
 * @author : rocky
 * @Create Time : 2017/9/8 上午10:39
 * @Modified Time : 2017/9/8 上午10:39
 */
public class TreasureTextView extends TextView {
    public TreasureTextView(Context context) {
        super(context);
    }

    public TreasureTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        changeTypeFace(context, attrs);
    }

    private void changeTypeFace(Context context, AttributeSet attrs) {
        if (attrs != null) {
            Typeface mtf = Typeface.createFromAsset(context.getAssets(), "fonts/treasurytext.ttf");
            super.setTypeface(mtf);
        }
    }
}
