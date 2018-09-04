package com.leyou.game.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Description : com.leyou.game.bean
 *
 * @author : rocky
 * @Create Time : 2017/7/30 下午4:37
 * @Modified Time : 2017/7/30 下午4:37
 */
public class WolfKillRoomInfoBean extends WolfKillRoomBean {
    @SerializedName("userArr")
    public List<WolfRoleBean> roomUserInfoList;
}
