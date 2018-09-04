package com.leyou.game.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Description : 游戏相关参数
 *
 * @author : rocky
 * @Create Time : 2017/4/13 上午10:25
 * @Modified Time : 2017/4/13 上午10:25
 */
public class GameBean implements Parcelable {
    public long mark;//游戏标识
    public String name;//游戏名称
    public String type;//游戏类型
    public String url;//游戏试玩地址
    public String pictureUrl;//游戏说明图片
    public String bonusState;//奖励说明
    public int score;//游戏积分
    public String readme; //备注说明
    public int directivity; // 屏幕方向 0横屏游戏，1竖屏游戏
    public long virtualCoin;//所要消耗的钻石个数

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mark);
        dest.writeString(this.name);
        dest.writeString(this.type);
        dest.writeString(this.url);
        dest.writeString(this.pictureUrl);
        dest.writeString(this.bonusState);
        dest.writeInt(this.score);
        dest.writeString(this.readme);
        dest.writeInt(this.directivity);
        dest.writeLong(this.virtualCoin);
    }

    public GameBean() {
    }

    protected GameBean(Parcel in) {
        this.mark = in.readLong();
        this.name = in.readString();
        this.type = in.readString();
        this.url = in.readString();
        this.pictureUrl = in.readString();
        this.bonusState = in.readString();
        this.score = in.readInt();
        this.readme = in.readString();
        this.directivity = in.readInt();
        this.virtualCoin = in.readLong();
    }

    public static final Parcelable.Creator<GameBean> CREATOR = new Parcelable.Creator<GameBean>() {
        @Override
        public GameBean createFromParcel(Parcel source) {
            return new GameBean(source);
        }

        @Override
        public GameBean[] newArray(int size) {
            return new GameBean[size];
        }
    };
}
