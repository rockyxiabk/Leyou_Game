package com.leyou.game.bean;

/**
 * Description : 商城道具属性
 *
 * @author : rocky
 * @Create Time : 2017/6/30 上午9:33
 * @Modified Time : 2017/6/30 上午9:33
 */
public class PropBean {
    public String id;//道具ID
    public String itemName;//道具名称
    public String itemContent;//道具说明
    public int itemNum;//道具个数
    public int type;//0.回复体力使用1.寻找宝窟使用2.升星使用
    public int itemPrice1;//道具价钱（碎钻）
    public int itemPrice2;//道具价钱（钻石）
    public String pictureUrl;//道具图片
}
