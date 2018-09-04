package com.leyou.game.bean.friend;

/**
 * Description : com.leyou.game.bean.friend
 *
 * @author : rocky
 * @Create Time : 2017/10/30 下午4:38
 * @Modified Time : 2017/10/30 下午4:38
 */
public class FriendBean {

    /**
     * birthday : 0
     * comment : string
     * headImgUrl : string
     * nickname : string
     * phone : string
     * region : string
     * sex : 0
     * status : 0
     * userId : string
     */

    private long birthday;
    private String comment;
    private String headImgUrl;
    private String nickname;
    private String phone;
    private String region;
    private int sex;
    private int status;
    private String userId;

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
