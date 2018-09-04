package com.leyou.game.bean.friend;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Description : com.leyou.game.bean
 *
 * @author : rocky
 * @Create Time : 2017/7/15 下午3:28
 * @Modified Time : 2017/7/15 下午3:28
 */
public class ContactBean implements Parcelable {
    public String remark;//用户备注名
    public String nickName;//用户昵称
    public String phoneName;//联系人姓名
    public String userId;//若用户是平台注册，则返回userId，否则返回null
    public String pictureUrl;//用户头像
    public String phoneNameLetter;//联系人首字母
    public String phone;//手机号
    public String phoneAddress;//联系人地址（手机号所在地）
    public int rawId;//查询手机通讯录数据库时获得的rawId；
    public int status;//0.不在平台内 1.我的好友 2.好友添加中 3.平台内部非好友

    @Override
    public String toString() {
        return "ContactBean{" +
                "remark='" + remark + '\'' +
                ", nickName='" + nickName + '\'' +
                ", phoneName='" + phoneName + '\'' +
                ", userId='" + userId + '\'' +
                ", pictureUrl='" + pictureUrl + '\'' +
                ", phoneNameLetter='" + phoneNameLetter + '\'' +
                ", status=" + status +
                ", phone='" + phone + '\'' +
                ", phoneAddress='" + phoneAddress + '\'' +
                ", rawId=" + rawId +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.remark);
        dest.writeString(this.nickName);
        dest.writeString(this.phoneName);
        dest.writeString(this.userId);
        dest.writeString(this.pictureUrl);
        dest.writeString(this.phoneNameLetter);
        dest.writeString(this.phone);
        dest.writeString(this.phoneAddress);
        dest.writeInt(this.rawId);
        dest.writeInt(this.status);
    }

    public ContactBean() {
    }

    protected ContactBean(Parcel in) {
        this.remark = in.readString();
        this.nickName = in.readString();
        this.phoneName = in.readString();
        this.userId = in.readString();
        this.pictureUrl = in.readString();
        this.phoneNameLetter = in.readString();
        this.phone = in.readString();
        this.phoneAddress = in.readString();
        this.rawId = in.readInt();
        this.status = in.readInt();
    }

    public static final Parcelable.Creator<ContactBean> CREATOR = new Parcelable.Creator<ContactBean>() {
        @Override
        public ContactBean createFromParcel(Parcel source) {
            return new ContactBean(source);
        }

        @Override
        public ContactBean[] newArray(int size) {
            return new ContactBean[size];
        }
    };
}
