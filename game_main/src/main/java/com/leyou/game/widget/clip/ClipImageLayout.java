package com.leyou.game.widget.clip;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.leyou.game.R;

/**
 * Description : 图片裁剪
 *
 * @author : rocky
 * @Create Time : 2017/5/27 下午2:08
 * @Modified By: rocky
 * @Modified Time : 2017/5/27 下午2:08
 */
public class ClipImageLayout extends RelativeLayout {
    public static final int REQUEST_CODE = 456;

    private Context context;
    private ClipZoomImageView mZoomImageView;
    private ClipImageBorderView mClipImageView;

    /**
     * 这里测试，直接写死了大小，真正使用过程中，可以提取为自定义属性
     */
    private int mHorizontalPadding = 0;

    public ClipImageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mZoomImageView = new ClipZoomImageView(context);
        mClipImageView = new ClipImageBorderView(context);
        android.view.ViewGroup.LayoutParams lp = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);

        this.addView(mZoomImageView, lp);
        this.addView(mClipImageView, lp);

        // 计算padding的px
        mHorizontalPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mHorizontalPadding, getResources().getDisplayMetrics());
        mZoomImageView.setHorizontalPadding(mHorizontalPadding);
        mClipImageView.setHorizontalPadding(mHorizontalPadding);
    }

    /**
     * 对外公布设置边距的方法,单位为dp
     *
     * @param mHorizontalPadding
     */
    public void setHorizontalPadding(int mHorizontalPadding) {
        this.mHorizontalPadding = mHorizontalPadding;
    }

    /**
     * 这里测试，直接写死了图片，真正使用过程中，可以提取为自定义属性
     */
    public void setImageSource(String imagePath) {
        Glide.with(context).load(imagePath).error(getResources().getDrawable(R.mipmap.icon_header_bg)).into(mZoomImageView);
    }

    /**
     * 裁切图片
     *
     * @return
     */
    public Bitmap clip() {
        return mZoomImageView.clip();
    }

}
