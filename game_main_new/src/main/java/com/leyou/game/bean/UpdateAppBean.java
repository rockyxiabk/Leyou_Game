package com.leyou.game.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Description : 应用检查更新
 *
 * @author : rocky
 * @Create Time : 2017/5/15 上午10:07
 * @Modified Time : 2017/5/15 上午10:07
 */
public class UpdateAppBean implements Parcelable {
    public String name;
    public String url;
    public String versionName;
    public int versionCode;
    public String description;
    public long upgradeTime;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.url);
        dest.writeString(this.versionName);
        dest.writeInt(this.versionCode);
        dest.writeString(this.description);
        dest.writeLong(this.upgradeTime);
    }

    public UpdateAppBean() {
    }

    protected UpdateAppBean(Parcel in) {
        this.name = in.readString();
        this.url = in.readString();
        this.versionName = in.readString();
        this.versionCode = in.readInt();
        this.description = in.readString();
        this.upgradeTime = in.readLong();
    }

    public static final Parcelable.Creator<UpdateAppBean> CREATOR = new Parcelable.Creator<UpdateAppBean>() {
        @Override
        public UpdateAppBean createFromParcel(Parcel source) {
            return new UpdateAppBean(source);
        }

        @Override
        public UpdateAppBean[] newArray(int size) {
            return new UpdateAppBean[size];
        }
    };
}
