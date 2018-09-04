package com.leyou.game.bean.user;

/**
 * Description : com.leyou.game.bean.user
 *
 * @author : rocky
 * @Create Time : 2017/11/29 下午2:15
 * @Modified Time : 2017/11/29 下午2:15
 */
public class WithCashNoteBean {

    public String bank;//银行
    public String cardNum;//卡号
    public long createTime;//申请时间
    public double money;//提现金额
    public long payTime;//打款时间
    public String realName;//真实姓名
    public int status; //提现状态1初始申请状态2审核通过3已打款4审核未通过

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public long getPayTime() {
        return payTime;
    }

    public void setPayTime(long payTime) {
        this.payTime = payTime;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
