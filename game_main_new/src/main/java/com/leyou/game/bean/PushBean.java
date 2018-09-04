package com.leyou.game.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Description : com.leyou.game.bean
 *
 * @author : rocky
 * @Create Time : 2017/6/23 下午3:34
 * @Modified Time : 2017/6/23 下午3:34
 */
public class PushBean implements Parcelable {
    public int type;//1：中奖 2:宝库被抢占
    public String title;
    public String des;
    public String id;//type==1时，id是中奖id
    public String awardUserId;
    public int awardGrade;
    public String awardName;
    public String awardDes;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeString(this.title);
        dest.writeString(this.des);
        dest.writeString(this.id);
        dest.writeString(this.awardUserId);
        dest.writeInt(this.awardGrade);
        dest.writeString(this.awardName);
        dest.writeString(this.awardDes);
    }

    public PushBean() {
    }

    protected PushBean(Parcel in) {
        this.type = in.readInt();
        this.title = in.readString();
        this.des = in.readString();
        this.id = in.readString();
        this.awardUserId = in.readString();
        this.awardGrade = in.readInt();
        this.awardName = in.readString();
        this.awardDes = in.readString();
    }

    public static final Parcelable.Creator<PushBean> CREATOR = new Parcelable.Creator<PushBean>() {
        @Override
        public PushBean createFromParcel(Parcel source) {
            return new PushBean(source);
        }

        @Override
        public PushBean[] newArray(int size) {
            return new PushBean[size];
        }
    };
}
