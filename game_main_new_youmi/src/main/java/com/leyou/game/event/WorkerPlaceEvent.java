package com.leyou.game.event;

/**
 * Description : com.leyou.game.event
 *
 * @author : rocky
 * @Create Time : 2017/6/7 下午4:55
 * @Modified Time : 2017/6/7 下午4:55
 */
public class WorkerPlaceEvent {
    private int type;//1刷新

    public WorkerPlaceEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
