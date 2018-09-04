package com.leyou.game.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Description : 宝窟相关属性
 *
 * @author : rocky
 * @Create Time : 2017/5/4 上午9:56
 * @Modified Time : 2017/5/4 上午9:56
 */
public class TreasureBean implements Parcelable {
    public String id;//宝窟ID (无人占领的矿不存在ID值)
    public int level;//矿等级
    public int attribute;//矿属性（防御值）
    public int type;//0是无人占领，1是已被占领
    public String pictureUrl;//宝库图片
    public int workerNum;//矿工数量
    public int phyPower;//占领宝库消耗的体力值
    public long residueTime;//截止时间的时间戳
    public String message;//抢占有人宝窟后的碎钻收益，无人宝窟返回null

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeInt(this.level);
        dest.writeInt(this.attribute);
        dest.writeInt(this.type);
        dest.writeString(this.pictureUrl);
        dest.writeInt(this.workerNum);
        dest.writeInt(this.phyPower);
        dest.writeLong(this.residueTime);
        dest.writeString(this.message);
    }

    public TreasureBean() {
    }

    protected TreasureBean(Parcel in) {
        this.id = in.readString();
        this.level = in.readInt();
        this.attribute = in.readInt();
        this.type = in.readInt();
        this.pictureUrl = in.readString();
        this.workerNum = in.readInt();
        this.phyPower = in.readInt();
        this.residueTime = in.readLong();
        this.message = in.readString();
    }

    public static final Parcelable.Creator<TreasureBean> CREATOR = new Parcelable.Creator<TreasureBean>() {
        @Override
        public TreasureBean createFromParcel(Parcel source) {
            return new TreasureBean(source);
        }

        @Override
        public TreasureBean[] newArray(int size) {
            return new TreasureBean[size];
        }
    };
}
