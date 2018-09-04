package com.leyou.game.bean;

/**
 * Description : 推送消息
 *
 * @author : rocky
 * @Create Time : 2017/4/20 下午7:28
 * @Modified Time : 2017/4/20 下午7:28
 */
public class MessageBean {
    public String id;// 信息id
    public String title;//标题
    public String content;//消息内容
    public int infoType;//消息类型（和透传消息一致）
    public int type;//消息类型 1系统消息、赢大奖活动 2中奖消息
    public long time;//时间
    public int flag;//标记，0未读，1已读
}
