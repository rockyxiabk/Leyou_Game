package com.leyou.game.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Entity mapped to table "CROWD".
 */
public class Crowd implements Parcelable {

    private Long id;
    private String crowdId;
    private String crowdName;
    private String pictureUrl;
    private Integer crowdNumbers;
    private Integer top;
    private Integer status;
    private Integer listenMessage;
    private String myName;

    public Crowd() {
    }

    public Crowd(Long id) {
        this.id = id;
    }

    public Crowd(Long id, String crowdId, String crowdName, String pictureUrl, Integer crowdNumbers, Integer top, Integer status, Integer listenMessage, String myName) {
        this.id = id;
        this.crowdId = crowdId;
        this.crowdName = crowdName;
        this.pictureUrl = pictureUrl;
        this.crowdNumbers = crowdNumbers;
        this.top = top;
        this.status = status;
        this.listenMessage = listenMessage;
        this.myName = myName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCrowdId() {
        return crowdId;
    }

    public void setCrowdId(String crowdId) {
        this.crowdId = crowdId;
    }

    public String getCrowdName() {
        return crowdName;
    }

    public void setCrowdName(String crowdName) {
        this.crowdName = crowdName;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public Integer getCrowdNumbers() {
        return crowdNumbers;
    }

    public void setCrowdNumbers(Integer crowdNumbers) {
        this.crowdNumbers = crowdNumbers;
    }

    public Integer getTop() {
        return top;
    }

    public void setTop(Integer top) {
        this.top = top;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getListenMessage() {
        return listenMessage;
    }

    public void setListenMessage(Integer listenMessage) {
        this.listenMessage = listenMessage;
    }

    public String getMyName() {
        return myName;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.crowdId);
        dest.writeString(this.crowdName);
        dest.writeString(this.pictureUrl);
        dest.writeValue(this.crowdNumbers);
        dest.writeValue(this.top);
        dest.writeValue(this.status);
        dest.writeValue(this.listenMessage);
        dest.writeString(this.myName);
    }

    protected Crowd(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.crowdId = in.readString();
        this.crowdName = in.readString();
        this.pictureUrl = in.readString();
        this.crowdNumbers = (Integer) in.readValue(Integer.class.getClassLoader());
        this.top = (Integer) in.readValue(Integer.class.getClassLoader());
        this.status = (Integer) in.readValue(Integer.class.getClassLoader());
        this.listenMessage = (Integer) in.readValue(Integer.class.getClassLoader());
        this.myName = in.readString();
    }

    public static final Parcelable.Creator<Crowd> CREATOR = new Parcelable.Creator<Crowd>() {
        @Override
        public Crowd createFromParcel(Parcel source) {
            return new Crowd(source);
        }

        @Override
        public Crowd[] newArray(int size) {
            return new Crowd[size];
        }
    };

    @Override
    public String toString() {
        return "Crowd{" +
                "id=" + id +
                ", crowdId='" + crowdId + '\'' +
                ", crowdName='" + crowdName + '\'' +
                ", pictureUrl='" + pictureUrl + '\'' +
                ", crowdNumbers=" + crowdNumbers +
                ", top=" + top +
                ", status=" + status +
                ", listenMessage=" + listenMessage +
                ", myName='" + myName + '\'' +
                '}';
    }
}