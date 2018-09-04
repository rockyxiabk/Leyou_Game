package com.leyou.game.util;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Description : 自定义属性动画
 *
 * @author : rocky
 * @Create Time : 2017/10/23 上午1:03
 * @Modified Time : 2017/10/23 上午1:03
 */
public class AnimatorUtils {
    public static void translationXLeft(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", 0, -view.getWidth());
        animator.setDuration(200);
        animator.start();
    }

    public static void translationYRight(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", 0, view.getWidth());
        animator.setDuration(200);
        animator.start();
    }

    public static void translationYTop(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", 0, -view.getHeight());
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator, alpha);
        animatorSet.setDuration(500);
        animatorSet.start();
    }

    public static void translationYBottom(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", 0, view.getHeight());
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator, alpha);
        animatorSet.setDuration(500);
        animatorSet.start();
    }
}
