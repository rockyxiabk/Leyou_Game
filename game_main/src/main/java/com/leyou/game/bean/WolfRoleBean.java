package com.leyou.game.bean;

/**
 * Description : com.leyou.game.bean
 *
 * @author : rocky
 * @Create Time : 2017/7/16 下午3:55
 * @Modified Time : 2017/7/16 下午3:55
 */
public class WolfRoleBean {
    public String userId;//用户id
    public String nickname;//用户昵称
    public String roleName;//角色昵称
    public String rolePicture;//角色头像地址
    public String gameRoleName;//用户游戏角色名称（狼人、平民……）
    public int gameRoleMark;//用户游戏角色名称对应角色标识 0民1狼人2猎人3预言家4女巫5守卫
    public int role;//房间身份（0普通，1房主）
    public int gameCount;//游戏总次数
    public String winRate;//胜率(98%)
    public int prepare;//是否准备 0未准备，1准备
    public int order;
}
