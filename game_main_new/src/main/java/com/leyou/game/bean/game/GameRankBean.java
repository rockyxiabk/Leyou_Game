package com.leyou.game.bean.game;

/**
 * Description : com.leyou.game.bean.game
 *
 * @author : rocky
 * @Create Time : 2017/10/27 下午2:04
 * @Modified Time : 2017/10/27 下午2:04
 */
public class GameRankBean {

    public long birthday;
    public String headImgUrl;
    public String nickname;
    public boolean myself;
    public int rank;
    public int score;
    public int sex;

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
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

    public boolean isMyself() {
        return myself;
    }

    public void setMyself(boolean myself) {
        this.myself = myself;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
}
