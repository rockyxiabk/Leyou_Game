package com.leyou.game.bean.pay;

/**
 * Description : com.leyou.game.bean.pay
 *
 * @author : rocky
 * @Create Time : 2017/10/27 下午3:12
 * @Modified Time : 2017/10/27 下午3:12
 */
public class PayBean {

    public String aliPayInfo;
    public String nonceStr;
    public String orderNo;
    public String wxAppId;
    public String wxPackage;
    public String wxPartnerId;
    public String wxPrepayId;
    public String wxSign;
    public String wxTimeStamp;

    public String getAliPayInfo() {
        return aliPayInfo;
    }

    public void setAliPayInfo(String aliPayInfo) {
        this.aliPayInfo = aliPayInfo;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getWxAppId() {
        return wxAppId;
    }

    public void setWxAppId(String wxAppId) {
        this.wxAppId = wxAppId;
    }

    public String getWxPackage() {
        return wxPackage;
    }

    public void setWxPackage(String wxPackage) {
        this.wxPackage = wxPackage;
    }

    public String getWxPartnerId() {
        return wxPartnerId;
    }

    public void setWxPartnerId(String wxPartnerId) {
        this.wxPartnerId = wxPartnerId;
    }

    public String getWxPrepayId() {
        return wxPrepayId;
    }

    public void setWxPrepayId(String wxPrepayId) {
        this.wxPrepayId = wxPrepayId;
    }

    public String getWxSign() {
        return wxSign;
    }

    public void setWxSign(String wxSign) {
        this.wxSign = wxSign;
    }

    public String getWxTimeStamp() {
        return wxTimeStamp;
    }

    public void setWxTimeStamp(String wxTimeStamp) {
        this.wxTimeStamp = wxTimeStamp;
    }
}
