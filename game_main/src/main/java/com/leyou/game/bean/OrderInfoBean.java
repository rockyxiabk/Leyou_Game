package com.leyou.game.bean;

/**
 * Description : com.leyou.game.bean
 *
 * @author : rocky
 * @Create Time : 2017/5/16 下午4:04
 * @Modified Time : 2017/5/16 下午4:04
 */
public class OrderInfoBean {
    public String id;//商品ID （可以为空）
    public double money;//金额
    public String pay_type;//支付方式
    public int pay_source;//支付来源 ： 0充值 3交易所 6交易所收购
    public double price;//钻石单价
    public int virtualCoin;//钻石个数

    public OrderInfoBean(String id, double money, String pay_type, int pay_source, double unitPrice, int diamondNumber) {
        this.id = id;
        this.money = money;
        this.pay_type = pay_type;
        this.pay_source = pay_source;
        this.price = unitPrice;
        this.virtualCoin = diamondNumber;
    }

    @Override
    public String toString() {
        return "OrderInfoBean{" +
                "id='" + id + '\'' +
                ", money=" + money +
                ", pay_type='" + pay_type + '\'' +
                ", pay_source=" + pay_source +
                '}';
    }
}
