package com.leyou.game.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Entity mapped to table "FRIEND".
 */
public class Friend implements Parcelable {

    private Long id;
    public static final int NO_SYSTEM = 0;//不在平台内 显示邀请好友
    public static final int FRIEND = 1;//我的好友
    public static final int ADDING_FRIEND = 2;//是否通过验证
    public static final int SYSTEM_NO_FRIEND = 3;//平台内非好友
    public static final int ADDING_WAITING_CONFIRM = 4;//等待验证
    private Integer status;
    private String userId;
    private String name;
    private String phoneNameLetter;
    private String nickname;
    private String pictureUrl;
    private Integer sex;
    private Long birthday;
    /** Not-null value. */
    private String phone;
    private String phoneAddress;
    private String remarkName;
    private Integer source;

    public Friend() {
    }

    public Friend(Long id) {
        this.id = id;
    }

    public Friend(Long id, Integer status, String userId, String name, String phoneNameLetter, String nickname, String pictureUrl, Integer sex, Long birthday, String phone, String phoneAddress, String remarkName, Integer source) {
        this.id = id;
        this.status = status;
        this.userId = userId;
        this.name = name;
        this.phoneNameLetter = phoneNameLetter;
        this.nickname = nickname;
        this.pictureUrl = pictureUrl;
        this.sex = sex;
        this.birthday = birthday;
        this.phone = phone;
        this.phoneAddress = phoneAddress;
        this.remarkName = remarkName;
        this.source = source;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNameLetter() {
        return phoneNameLetter;
    }

    public void setPhoneNameLetter(String phoneNameLetter) {
        this.phoneNameLetter = phoneNameLetter;
    }

    public String getNickName() {
        return nickname;
    }

    public void setNickName(String nickname) {
        this.nickname = nickname;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Long getBirthday() {
        return birthday;
    }

    public void setBirthday(Long birthday) {
        this.birthday = birthday;
    }

    /** Not-null value. */
    public String getPhone() {
        return phone;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoneAddress() {
        return phoneAddress;
    }

    public void setPhoneAddress(String phoneAddress) {
        this.phoneAddress = phoneAddress;
    }

    public String getRemarkName() {
        return remarkName;
    }

    public void setRemarkName(String remarkName) {
        this.remarkName = remarkName;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeValue(this.status);
        dest.writeString(this.userId);
        dest.writeString(this.name);
        dest.writeString(this.phoneNameLetter);
        dest.writeString(this.nickname);
        dest.writeString(this.pictureUrl);
        dest.writeValue(this.sex);
        dest.writeValue(this.birthday);
        dest.writeString(this.phone);
        dest.writeString(this.phoneAddress);
        dest.writeString(this.remarkName);
        dest.writeValue(this.source);
    }

    protected Friend(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.status = (Integer) in.readValue(Integer.class.getClassLoader());
        this.userId = in.readString();
        this.name = in.readString();
        this.phoneNameLetter = in.readString();
        this.nickname = in.readString();
        this.pictureUrl = in.readString();
        this.sex = (Integer) in.readValue(Integer.class.getClassLoader());
        this.birthday = (Long) in.readValue(Long.class.getClassLoader());
        this.phone = in.readString();
        this.phoneAddress = in.readString();
        this.remarkName = in.readString();
        this.source = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<Friend> CREATOR = new Parcelable.Creator<Friend>() {
        @Override
        public Friend createFromParcel(Parcel source) {
            return new Friend(source);
        }

        @Override
        public Friend[] newArray(int size) {
            return new Friend[size];
        }
    };

    @Override
    public String toString() {
        return "Friend{" +
                "id=" + id +
                ", status=" + status +
                ", userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", phoneNameLetter='" + phoneNameLetter + '\'' +
                ", nickname='" + nickname + '\'' +
                ", pictureUrl='" + pictureUrl + '\'' +
                ", sex=" + sex +
                ", birthday=" + birthday +
                ", phone='" + phone + '\'' +
                ", phoneAddress='" + phoneAddress + '\'' +
                ", remarkName='" + remarkName + '\'' +
                ", source=" + source +
                '}';
    }
}