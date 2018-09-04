package com.leyou.game.event;

/**
 * Description : com.leyou.game.event
 *
 * @author : rocky
 * @Create Time : 2017/6/23 上午10:14
 * @Modified Time : 2017/6/23 上午10:14
 */
public class RefreshWinAwardEvent {
    public static final int REFRESH = 1;
    public int event;//

    public RefreshWinAwardEvent(int event) {
        this.event = event;
    }

    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
    }
}
