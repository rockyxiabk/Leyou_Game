package com.leyou.game.bean;

/**
 * Description : 微信支付
 *
 * @author : rocky
 * @Create Time : 2017/5/16 下午3:32
 * @Modified By: rocky
 * @Modified Time : 2017/5/16 下午3:32
 */

public class WXOrderInfoBean {
    public String outTraderNo;//订单编号
    public PayInfo payInfo;//微信的一些相关信息

    public static class PayInfo {
        public String appid;//wx appId
        public String partnerid;//商户号id
        public String noncestr;//微信返回的随机字符串
        public String packageValue;
        public String prepayid;//微信生成的预支付回话标识，用于后续接口调用中使用，该值有效期为2小时
        public String timestamp;//时间戳
        public String sign;//微信返回的签名

        @Override
        public String toString() {
            return "PayInfo{" +
                    "appid='" + appid + '\'' +
                    ", partnerid='" + partnerid + '\'' +
                    ", noncestr='" + noncestr + '\'' +
                    ", packageValue='" + packageValue + '\'' +
                    ", prepayid='" + prepayid + '\'' +
                    ", timestamp='" + timestamp + '\'' +
                    ", sign='" + sign + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "WXOrderInfo{" +
                "outTradeNo='" + outTraderNo + '\'' +
                ", payInfo=" + payInfo +
                '}';
    }
}
