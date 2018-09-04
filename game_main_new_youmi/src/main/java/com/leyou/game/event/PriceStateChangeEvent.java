package com.leyou.game.event;

/**
 * Description : com.leyou.game.event
 *
 * @author : rocky
 * @Create Time : 2017/11/27 下午6:41
 * @Modified Time : 2017/11/27 下午6:41
 */
public class PriceStateChangeEvent {
    private int type;//1刷新

    public PriceStateChangeEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
