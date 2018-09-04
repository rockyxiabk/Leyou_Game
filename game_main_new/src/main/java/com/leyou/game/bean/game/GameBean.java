package com.leyou.game.bean.game;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Description : 游戏相关属性
 *
 * @author : rocky
 * @Create Time : 2017/10/27 下午12:01
 * @Modified Time : 2017/10/27 下午12:01
 */
public class GameBean implements Parcelable {
    public static final int TYPE_WIN_PRIZE = 1;//1赢大奖
    public static final int TYPE_H5 = 2;//2H5对接游戏
    public static final int TYPE_WIN_DIAMOND = 3;//3侧边栏游戏
    public String bannerUrl;//游戏banner图
    public String iconUrl;//游戏icon图
    public String iconDynamicUrl;//游戏动态icon图
    public String name;//游戏名称
    public String propaganda;//宣传语
    public String property;//属性以逗号分隔
    public String readme;//详情说明
    public String recommend;//小编推荐
    public String uniqueMark;//游戏唯一标识
    public String url;//游戏地址
    @SerializedName("imgList")
    public List<String> imgList;//图片列表集合

    public String imgName;//？
    public int isAlone;//0单机游戏，1不是单机游戏
    public int isHot;//是否最热
    public int playNum;//多少人在玩儿
    public int screenDirection;// 1横屏，2竖屏
    public int type;//游戏类型：1赢大奖，2H5对接大型游戏,3侧边栏游戏;轮播图片：1打开url直接开始游戏，2打开详情页

    public String gameImgUrl;//侧边栏对应图片的url
    public String gameUrl;//游戏url链接
    public int isShow;//是否展示侧边栏：1展示0不展示
    public int score;//赢大奖得分

    public int category;//游戏分类
    public String imgUrl;//游戏类别图片
    public String title;//类标标题


    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPropaganda() {
        return propaganda;
    }

    public void setPropaganda(String propaganda) {
        this.propaganda = propaganda;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getReadme() {
        return readme;
    }

    public void setReadme(String readme) {
        this.readme = readme;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    public String getUniqueMark() {
        return uniqueMark;
    }

    public void setUniqueMark(String uniqueMark) {
        this.uniqueMark = uniqueMark;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getImgList() {
        return imgList;
    }

    public void setImgList(List<String> imgList) {
        this.imgList = imgList;
    }

    public String getIconDynamicUrl() {
        return iconDynamicUrl;
    }

    public void setIconDynamicUrl(String iconDynamicUrl) {
        this.iconDynamicUrl = iconDynamicUrl;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public int getIsAlone() {
        return isAlone;
    }

    public void setIsAlone(int isAlone) {
        this.isAlone = isAlone;
    }

    public int getIsHot() {
        return isHot;
    }

    public void setIsHot(int isHot) {
        this.isHot = isHot;
    }

    public int getPlayNum() {
        return playNum;
    }

    public void setPlayNum(int playNum) {
        this.playNum = playNum;
    }

    public int getScreenDirection() {
        return screenDirection;
    }

    public void setScreenDirection(int screenDirection) {
        this.screenDirection = screenDirection;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getGameImgUrl() {
        return gameImgUrl;
    }

    public void setGameImgUrl(String gameImgUrl) {
        this.gameImgUrl = gameImgUrl;
    }

    public String getGameUrl() {
        return gameUrl;
    }

    public void setGameUrl(String gameUrl) {
        this.gameUrl = gameUrl;
    }

    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.bannerUrl);
        dest.writeString(this.iconUrl);
        dest.writeString(this.iconDynamicUrl);
        dest.writeString(this.name);
        dest.writeString(this.propaganda);
        dest.writeString(this.property);
        dest.writeString(this.readme);
        dest.writeString(this.recommend);
        dest.writeString(this.uniqueMark);
        dest.writeString(this.url);
        dest.writeStringList(this.imgList);
        dest.writeString(this.imgName);
        dest.writeInt(this.isAlone);
        dest.writeInt(this.isHot);
        dest.writeInt(this.playNum);
        dest.writeInt(this.screenDirection);
        dest.writeInt(this.type);
        dest.writeString(this.gameImgUrl);
        dest.writeString(this.gameUrl);
        dest.writeInt(this.isShow);
        dest.writeInt(this.score);
    }

    public GameBean() {
    }

    protected GameBean(Parcel in) {
        this.bannerUrl = in.readString();
        this.iconUrl = in.readString();
        this.iconDynamicUrl = in.readString();
        this.name = in.readString();
        this.propaganda = in.readString();
        this.property = in.readString();
        this.readme = in.readString();
        this.recommend = in.readString();
        this.uniqueMark = in.readString();
        this.url = in.readString();
        this.imgList = in.createStringArrayList();
        this.imgName = in.readString();
        this.isAlone = in.readInt();
        this.isHot = in.readInt();
        this.playNum = in.readInt();
        this.screenDirection = in.readInt();
        this.type = in.readInt();
        this.gameImgUrl = in.readString();
        this.gameUrl = in.readString();
        this.isShow = in.readInt();
        this.score = in.readInt();
    }

    public static final Creator<GameBean> CREATOR = new Creator<GameBean>() {
        @Override
        public GameBean createFromParcel(Parcel source) {
            return new GameBean(source);
        }

        @Override
        public GameBean[] newArray(int size) {
            return new GameBean[size];
        }
    };

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "GameBean{" +
                "bannerUrl='" + bannerUrl + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", iconDynamicUrl='" + iconDynamicUrl + '\'' +
                ", name='" + name + '\'' +
                ", propaganda='" + propaganda + '\'' +
                ", property='" + property + '\'' +
                ", readme='" + readme + '\'' +
                ", recommend='" + recommend + '\'' +
                ", uniqueMark='" + uniqueMark + '\'' +
                ", url='" + url + '\'' +
                ", imgList=" + imgList +
                ", imgName='" + imgName + '\'' +
                ", isAlone=" + isAlone +
                ", isHot=" + isHot +
                ", playNum=" + playNum +
                ", screenDirection=" + screenDirection +
                ", type=" + type +
                ", gameImgUrl='" + gameImgUrl + '\'' +
                ", gameUrl='" + gameUrl + '\'' +
                ", isShow=" + isShow +
                ", score=" + score +
                ", category=" + category +
                ", imgUrl='" + imgUrl + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
