package com.leyou.game.bean;

/**
 * Description : 推送消息
 *
 * @author : rocky
 * @Create Time : 2017/4/20 下午7:28
 * @Modified Time : 2017/4/20 下午7:28
 */
public class MessageBean {
    public String content;//内容
    public long createTime;//时间
    public int id;//id消息id或者中奖id
    public int isRead;//消息已读状态
    public int msgType;// 1代表系统消息通知2代表中奖信息
    public int prizeType;//奖品类型1实物2虚拟产品
    public int status;//发货状态 1未填写收货状态信息 2等待发货 3已发货 4已确认收货
    public String title;//标题

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public int getPrizeType() {
        return prizeType;
    }

    public void setPrizeType(int prizeType) {
        this.prizeType = prizeType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
//    public String id;// 信息id
//    public String title;//标题
//    public String content;//消息内容
//    public int infoType;//消息类型（和透传消息一致）
//    public int type;//消息类型 1系统消息、赢大奖活动 2中奖消息
//    public long time;//时间
//    public int flag;//标记，0未读，1已读
}
