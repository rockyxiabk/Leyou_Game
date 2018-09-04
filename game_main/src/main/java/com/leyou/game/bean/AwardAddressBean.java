package com.leyou.game.bean;

/**
 * Description : com.leyou.game.bean
 *
 * @author : rocky
 * @Create Time : 2017/6/19 下午7:13
 * @Modified Time : 2017/6/19 下午7:13
 */
public class AwardAddressBean {
    public String id;//奖品id
    public String userName;//收件人
    public String userPhone;//手机号
    public String userAddress;//收货地址
    public String userEmail;//收件人邮箱

    public AwardAddressBean(String id, String userName, String userPhone, String userAddress, String userEmail) {
        this.id = id;
        this.userName = userName;
        this.userPhone = userPhone;
        this.userAddress = userAddress;
        this.userEmail = userEmail;
    }
}
