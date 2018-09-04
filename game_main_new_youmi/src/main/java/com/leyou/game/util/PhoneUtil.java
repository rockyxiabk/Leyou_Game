package com.leyou.game.util;

import android.text.TextUtils;

/**
 * Description : 手机常用工具类
 *
 * @author : rocky
 * @Create Time : 2017/4/19 上午9:44
 * @Modified Time : 2017/4/19 上午9:44
 */
public class PhoneUtil {
    /**
     * 手机号验证
     * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
     * 联通：130、131、132、152、155、156、185、186
     * 电信：133、153、180、189、（1349卫通
     * 虚拟号段
     * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
     */
    public static boolean isMobileNumber(String phone) {

        //"[1]"代表第1位为数字1，"[34578]"代表第二位可以为3、4、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        String telRegex = "[1][34587]\\d{9}";
        if (TextUtils.isEmpty(phone)) {
            return false;
        } else {
            return phone.matches(telRegex);
        }
    }
}
