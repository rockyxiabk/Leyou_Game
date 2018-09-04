package com.leyou.game.bean.diamond;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Description : com.leyou.game.bean.user
 *
 * @author : rocky
 * @Create Time : 2017/10/27 下午4:39
 * @Modified Time : 2017/10/27 下午4:39
 */
public class DiamondBean implements Parcelable {

    public String imgUrl;//图片地址
    public String title;//类型说明
    public int type;//消费类型
    public long createTime;//创建时间
    public String money;//涉及金钱的时候显示
    public int num;//钻石的变动，带有正负号，没有单位

    public double maxPrice;//钻石最低交易价格
    public double minPrice;//钻石最高交易价格


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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imgUrl);
        dest.writeString(this.title);
        dest.writeInt(this.type);
        dest.writeLong(this.createTime);
        dest.writeString(this.money);
        dest.writeInt(this.num);
        dest.writeDouble(this.maxPrice);
        dest.writeDouble(this.minPrice);
    }

    public DiamondBean() {
    }

    protected DiamondBean(Parcel in) {
        this.imgUrl = in.readString();
        this.title = in.readString();
        this.type = in.readInt();
        this.createTime = in.readLong();
        this.money = in.readString();
        this.num = in.readInt();
        this.maxPrice = in.readDouble();
        this.minPrice = in.readDouble();
    }

    public static final Parcelable.Creator<DiamondBean> CREATOR = new Parcelable.Creator<DiamondBean>() {
        @Override
        public DiamondBean createFromParcel(Parcel source) {
            return new DiamondBean(source);
        }

        @Override
        public DiamondBean[] newArray(int size) {
            return new DiamondBean[size];
        }
    };
}
