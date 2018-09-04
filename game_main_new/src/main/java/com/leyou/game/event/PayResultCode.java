package com.leyou.game.event;

/**
 * Description : 支付结果
 *
 * @author : rocky
 * @Create Time : 2017/5/16 下午3:34
 * @Modified By: rocky
 * @Modified Time : 2017/5/16 下午3:34
 */
public class PayResultCode {
    private int resultCode;//支付返回码，0 支付失败，1支付成功
    private String errorStr;//错误信息

    public PayResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public PayResultCode(int resultCode, String errorStr) {
        this.resultCode = resultCode;
        this.errorStr = errorStr;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getErrorStr() {
        return errorStr;
    }

    public void setErrorStr(String errorStr) {
        this.errorStr = errorStr;
    }
}
