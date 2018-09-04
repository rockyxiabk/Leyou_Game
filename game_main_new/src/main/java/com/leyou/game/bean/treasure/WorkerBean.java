package com.leyou.game.bean.treasure;

/**
 * Description : 矿工相关属性信息
 *
 * @author : rocky
 * @Create Time : 2017/4/26 下午7:21
 * @Modified Time : 2017/4/26 下午7:21
 */
public class WorkerBean {
    public String id;//矿工ID
    public int typeId;//矿工类型ID
    public String typeName;//矿工类型（白色 蓝色 黄色 红色 紫色）
    public int levelUpConsume;//属性升值所需钻石。0说明当前矿工属性已满，不能升级
    public int attribute;//矿工属性（力量值）
    public int maxAttribute;//属性能力升级的额最大值
    public String treasureId;//宝窟标识
    public String pictureUrl;//矿工图片
    public int available;//0不可用(钻石消耗殆尽)，1可用
    public int starLevel;//矿工星级（100起步，一个星代表10个武力值）
    public String starUrl;//升星图片gif
    public int phyPower;//当前体力值

    public WorkerBean() {
    }

    public WorkerBean(String id) {
        this.id = id;
    }
}
