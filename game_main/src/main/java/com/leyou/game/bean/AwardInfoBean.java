package com.leyou.game.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Description : 中奖信息和发货状态
 *
 * @author : rocky
 * @Create Time : 2017/6/14 下午6:01
 * @Modified Time : 2017/6/14 下午6:01
 */
public class AwardInfoBean implements Parcelable {
    public String id;//获奖id
    public String prizeName;//奖品描述
    public String prizeUrl;//奖品图片
    public long addTime;//获奖时间
    public int flag;//信息读取状态 0 未读 1已读
    public int status;//发货状态 0.未填写地址1.待发货2.已发货
    public static final int STATUS_NULL = 0;//未填写地址
    public static final int STATUS_WAITING = 1;//待发货
    public static final int STATUS_SEND = 2;//已发货
    public String game;//游戏名称
    public int rank;//获奖名次(5个等级)

    public String userName;//收件人
    public String userPhone;//手机号
    public String userAddress;//收货地址
    public String userEmail;//收件人邮箱

    public String courierCompany;//快递公司
    public String courierNumber;//快递单号
    public static final int PRIZE_TYPE_VIRTUAL = 2;
    public static final int PRIZE_TYPE_TRUE = 1;
    public int type;//奖品类型 ，分为实物和虚拟，2代表虚拟，1代表实物
    public double price;//奖品价格

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.prizeName);
        dest.writeString(this.prizeUrl);
        dest.writeLong(this.addTime);
        dest.writeInt(this.flag);
        dest.writeInt(this.status);
        dest.writeString(this.game);
        dest.writeInt(this.rank);
        dest.writeString(this.userName);
        dest.writeString(this.userPhone);
        dest.writeString(this.userAddress);
        dest.writeString(this.userEmail);
        dest.writeString(this.courierCompany);
        dest.writeString(this.courierNumber);
        dest.writeInt(this.type);
        dest.writeDouble(this.price);
    }

    public AwardInfoBean() {
    }

    protected AwardInfoBean(Parcel in) {
        this.id = in.readString();
        this.prizeName = in.readString();
        this.prizeUrl = in.readString();
        this.addTime = in.readLong();
        this.flag = in.readInt();
        this.status = in.readInt();
        this.game = in.readString();
        this.rank = in.readInt();
        this.userName = in.readString();
        this.userPhone = in.readString();
        this.userAddress = in.readString();
        this.userEmail = in.readString();
        this.courierCompany = in.readString();
        this.courierNumber = in.readString();
        this.type = in.readInt();
        this.price = in.readDouble();
    }

    public static final Parcelable.Creator<AwardInfoBean> CREATOR = new Parcelable.Creator<AwardInfoBean>() {
        @Override
        public AwardInfoBean createFromParcel(Parcel source) {
            return new AwardInfoBean(source);
        }

        @Override
        public AwardInfoBean[] newArray(int size) {
            return new AwardInfoBean[size];
        }
    };
}
