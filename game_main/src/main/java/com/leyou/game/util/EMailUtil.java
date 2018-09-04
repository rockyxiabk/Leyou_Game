package com.leyou.game.util;

import android.text.TextUtils;

/**
 * Description : com.leyou.game.util
 *
 * @author : rocky
 * @Create Time : 2017/9/11 下午6:29
 * @Modified Time : 2017/9/11 下午6:29
 */
public class EMailUtil {
    //

    /**
     * 邮箱验证
     * [\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?
     */
    public static boolean isE_Mail(String e_mail) {

        String telRegex = "[\\\\w!#$%&'*+/=?^_`{|}~-]+(?:\\\\.[\\\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\\\w](?:[\\\\w-]*[\\\\w])?\\\\.)+[\\\\w](?:[\\\\w-]*[\\\\w])?";
        if (TextUtils.isEmpty(e_mail)) {
            return false;
        } else {
            return e_mail.matches(telRegex);
        }
    }
}
