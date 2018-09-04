package com.leyou.game.bean.user;

/**
 * Description : 用户签到
 *
 * @author : rocky
 * @Create Time : 2017/11/1 下午2:54
 * @Modified By: rocky
 * @Modified Time : 2017/11/1 下午2:54
 */

public class SignBean {

    public long lastSignDate;
    public int continuityDay;
    public int maxDay;
    public int signDiamondsNum;

    public int getContinuityDay() {
        return continuityDay;
    }

    public void setContinuityDay(int continuityDay) {
        this.continuityDay = continuityDay;
    }

    public long getLastSignDate() {
        return lastSignDate;
    }

    public void setLastSignDate(long lastSignDateX) {
        this.lastSignDate = lastSignDateX;
    }

    public int getSignDiamondsNum() {
        return signDiamondsNum;
    }

    public void setSignDiamondsNum(int signDiamondsNum) {
        this.signDiamondsNum = signDiamondsNum;
    }
}
