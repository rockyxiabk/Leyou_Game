package com.leyou.game.util;

/**
 * Description : com.leyou.game.util
 *
 * @author : rocky
 * @Create Time : 2017/7/22 下午1:57
 * @Modified Time : 2017/7/22 下午1:57
 */
public class RongYunUtil {
    private static RongYunUtil instance;

    public static RongYunUtil getInstance() {
        if (null == instance) {
            instance = new RongYunUtil();
        }
        return instance;
    }
}
