package com.leyou.game.bean;

/**
 * Description : com.leyou.game.bean
 *
 * @author : rocky
 * @Create Time : 2017/7/19 下午12:02
 * @Modified Time : 2017/7/19 下午12:02
 */
public class WolfKillRoomBean {
    public long roomId;//房间id
    public String comment;//房间角色分配信息
    public int roundNum;//第几天（轮）（游戏未开始时返回-1）
    public int type;//房间类型（几人局）
    public int state;//0黑夜1白天（游戏未开始时返回-1）
}
