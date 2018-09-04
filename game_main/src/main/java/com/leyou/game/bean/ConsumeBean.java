package com.leyou.game.bean;

/**
 * Description : 消费记录
 *
 * @author : rocky
 * @Create Time : 2017/8/10 下午3:59
 * @Modified Time : 2017/8/10 下午3:59
 */
public class ConsumeBean {
    public String pictureUrl;//图片类型
    public String note;//消费类型说明
    public long time;//时间
    public int virtualCoin;//购买或者消费的钻石数量（有正负号）
    public String money;//购买或者卖出的金额（含有正负号）
    public int type;//区分来源 主要是交易所的购买还是出售，控制消费记录列表金额与钻石显示顺序，0不涉及现金、1为购买类交易(金额负)、2出售(金额正)
}
