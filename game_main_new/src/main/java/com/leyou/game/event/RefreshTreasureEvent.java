package com.leyou.game.event;

/**
 * Description : com.leyou.game.event
 *
 * @author : rocky
 * @Create Time : 2017/6/13 下午7:53
 * @Modified Time : 2017/6/13 下午7:53
 */
public class RefreshTreasureEvent {
    private int type;//1刷新

    public RefreshTreasureEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
