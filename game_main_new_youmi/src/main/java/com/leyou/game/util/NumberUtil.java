package com.leyou.game.util;

import java.text.DecimalFormat;

/**
 * Description : 数字格式转化
 *
 * @author : rocky
 * @Create Time : 2017/5/12 下午5:19
 * @Modified Time : 2017/5/12 下午5:19
 */
public class NumberUtil {
    /**
     * double保留三位小数
     *
     * @param number
     * @return
     */
    public static String getTwoPointNumber(double number) {
        DecimalFormat df = new DecimalFormat("##0.###");
        String st = df.format(number);
        return st;
    }
}
