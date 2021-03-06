package com.leyou.game.util;

import com.leyou.game.dao.PhoneContact;

import java.util.Comparator;

/**
 * Description : com.leyou.game.util
 *
 * @author : rocky
 * @Create Time : 2017/7/15 下午3:44
 * @Modified Time : 2017/7/15 下午3:44
 */
public class PhoneContactComparator implements Comparator<PhoneContact> {
    private static final String TAG = "PinyinComparator";

    public int compare(PhoneContact friend1, PhoneContact friend2) {
        //这里主要是用来对ListView里面的数据根据*ABCDEFG...Z#来排序
        if (friend1.getPhoneNameLetter().equals("@") || friend2.getPhoneNameLetter().equals("#")) {
            return -1;
        } else if (friend1.getPhoneNameLetter().equals("#") || friend2.getPhoneNameLetter().equals("@")) {
            return 1;
        } else {
            return friend1.getPhoneNameLetter().compareTo(friend2.getPhoneNameLetter());
        }
    }
}
