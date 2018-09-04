package com.leyou.game.event;

/**
 * Description : com.leyou.game.event
 *
 * @author : rocky
 * @Create Time : 2017/9/5 下午4:34
 * @Modified Time : 2017/9/5 下午4:34
 */
public class WolfKillRoomEvent {
    private boolean refresh;

    public WolfKillRoomEvent(boolean refresh) {
        this.refresh = refresh;
    }

    public boolean isRefresh() {
        return refresh;
    }

    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }
}
