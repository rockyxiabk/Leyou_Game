package com.leyou.game.bean.game;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Description : 我的获奖
 *
 * @author : rocky
 * @Create Time : 2017/10/27 下午1:44
 * @Modified Time : 2017/10/27 下午1:44
 */
public class GameWinPriseBean implements Parcelable {

    public String address;//收货地址
    public long createDate;//获奖日期
    public long deliveryTime;//发货时间
    public String email;//电子邮箱
    public String expressCompany;//快递公司
    public String expressNo;//快递单号
    public String gameName;//游戏名称
    public String headImgUrl;//中奖人头像地址
    public String nickname;//中奖人昵称
    public String phone;//收获电话
    public String prizeImgUrl;//获奖图片url
    public String prizeImgSmallUrl;//奖品图片 小图（方图片）
    public String prizeName;//奖品名称
    public int prizePrice;//奖品价值
    public int prizeRank;//第几名
    public int prizeType;//奖品类型1实物2虚拟产品 ,
    public static final int PRIZE_TYPE_VIRTUAL = 2;
    public static final int PRIZE_TYPE_TRUE = 1;
    public int isRead;//0没有读 1已读
    public String realName;//收货真实姓名
    public int status;//发货状态 1未填写收货状态信息 2等待发货 3已发货 4已确认收货
    public static final int STATUS_NULL = 1;//未填写地址
    public static final int STATUS_WAITING = 2;//待发货
    public static final int STATUS_SEND = 3;//已发货
    public String uniqueMark;//游戏唯一标识
    public int winId;//获奖信息id

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(long deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getExpressCompany() {
        return expressCompany;
    }

    public void setExpressCompany(String expressCompany) {
        this.expressCompany = expressCompany;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPrizeImgUrl() {
        return prizeImgUrl;
    }

    public void setPrizeImgUrl(String prizeImgUrl) {
        this.prizeImgUrl = prizeImgUrl;
    }

    public String getImgSmallUrl() {
        return prizeImgSmallUrl;
    }

    public void setPrizeImgSmallUrl(String prizeImgSmallUrl) {
        this.prizeImgSmallUrl = prizeImgSmallUrl;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public int getPrizePrice() {
        return prizePrice;
    }

    public void setPrizePrice(int prizePrice) {
        this.prizePrice = prizePrice;
    }

    public int getPrizeRank() {
        return prizeRank;
    }

    public void setPrizeRank(int prizeRank) {
        this.prizeRank = prizeRank;
    }

    public int getPrizeType() {
        return prizeType;
    }

    public void setPrizeType(int prizeType) {
        this.prizeType = prizeType;
    }

    public static int getPrizeTypeVirtual() {
        return PRIZE_TYPE_VIRTUAL;
    }

    public static int getPrizeTypeTrue() {
        return PRIZE_TYPE_TRUE;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUniqueMark() {
        return uniqueMark;
    }

    public void setUniqueMark(String uniqueMark) {
        this.uniqueMark = uniqueMark;
    }

    public int getWinId() {
        return winId;
    }

    public void setWinId(int winId) {
        this.winId = winId;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.address);
        dest.writeLong(this.createDate);
        dest.writeLong(this.deliveryTime);
        dest.writeString(this.email);
        dest.writeString(this.expressCompany);
        dest.writeString(this.expressNo);
        dest.writeString(this.gameName);
        dest.writeString(this.headImgUrl);
        dest.writeString(this.nickname);
        dest.writeString(this.phone);
        dest.writeString(this.prizeImgUrl);
        dest.writeString(this.prizeImgSmallUrl);
        dest.writeString(this.prizeName);
        dest.writeInt(this.prizePrice);
        dest.writeInt(this.prizeRank);
        dest.writeInt(this.prizeType);
        dest.writeInt(this.isRead);
        dest.writeString(this.realName);
        dest.writeInt(this.status);
        dest.writeString(this.uniqueMark);
        dest.writeInt(this.winId);
    }

    public GameWinPriseBean() {
    }

    protected GameWinPriseBean(Parcel in) {
        this.address = in.readString();
        this.createDate = in.readLong();
        this.deliveryTime = in.readLong();
        this.email = in.readString();
        this.expressCompany = in.readString();
        this.expressNo = in.readString();
        this.gameName = in.readString();
        this.headImgUrl = in.readString();
        this.nickname = in.readString();
        this.phone = in.readString();
        this.prizeImgUrl = in.readString();
        this.prizeImgSmallUrl = in.readString();
        this.prizeName = in.readString();
        this.prizePrice = in.readInt();
        this.prizeRank = in.readInt();
        this.prizeType = in.readInt();
        this.isRead = in.readInt();
        this.realName = in.readString();
        this.status = in.readInt();
        this.uniqueMark = in.readString();
        this.winId = in.readInt();
    }

    public static final Parcelable.Creator<GameWinPriseBean> CREATOR = new Parcelable.Creator<GameWinPriseBean>() {
        @Override
        public GameWinPriseBean createFromParcel(Parcel source) {
            return new GameWinPriseBean(source);
        }

        @Override
        public GameWinPriseBean[] newArray(int size) {
            return new GameWinPriseBean[size];
        }
    };
}
