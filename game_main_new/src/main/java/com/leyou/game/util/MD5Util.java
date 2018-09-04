package com.leyou.game.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Description : MD5转化工具类
 *
 * @author : rocky
 * @Create Time : 2017/4/5 下午3:03
 * @Modified By: rocky
 * @Modified Time : 2017/4/5 下午3:03
 */

public class MD5Util {
    public static String md5(String str) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(str.getBytes());
        byte[] bs = md.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bs.length; i++) {
            int v = bs[i] & 0xff;
            if (v < 16) {
                sb.append(0);
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString();
    }
}
