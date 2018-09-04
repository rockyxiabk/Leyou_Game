package com.leyou.game.bean.treasure;

/**
 * Description : com.leyou.game.bean.treasure
 *
 * @author : rocky
 * @Create Time : 2017/12/11 下午5:45
 * @Modified Time : 2017/12/11 下午5:45
 */
public class TreasureGainBean {

    /**
     * userId : 77b647e413a848a6904744c5e91cc30e
     * gainNum : 166199
     * nickname : 秦芳
     * headImgUrl : https://file.igamestorm.com//userHeadImg/default.png
     * freeGaiNum : 162494
     * occupyNum : 3705
     */

    public String userId;
    public int gainNum;//总共收益
    public String nickname;//当前用户昵称
    public String headImgUrl;//当前用户头像
    public int freeGaiNum;//自然收益所得
    public int occupyNum;//抢占宝库所得

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getGainNum() {
        return gainNum;
    }

    public void setGainNum(int gainNum) {
        this.gainNum = gainNum;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public int getFreeGaiNum() {
        return freeGaiNum;
    }

    public void setFreeGaiNum(int freeGaiNum) {
        this.freeGaiNum = freeGaiNum;
    }

    public int getOccupyNum() {
        return occupyNum;
    }

    public void setOccupyNum(int occupyNum) {
        this.occupyNum = occupyNum;
    }
}
