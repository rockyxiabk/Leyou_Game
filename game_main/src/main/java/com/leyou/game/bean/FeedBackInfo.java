package com.leyou.game.bean;

/**
 * Description : 用户反馈信息
 *
 * @author : rocky
 * @Create Time : 2017/4/14 下午4:07
 * @Modified Time : 2017/4/14 下午4:07
 */
public class FeedBackInfo {
    public String title;//意见标题
    public String content;//意见内容
    public String note;//联系方式

    public FeedBackInfo(String title, String content, String note) {
        this.title = title;
        this.content = content;
        this.note = note;
    }
}
