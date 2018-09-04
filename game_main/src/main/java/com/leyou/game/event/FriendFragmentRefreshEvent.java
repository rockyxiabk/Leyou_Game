package com.leyou.game.event;

/**
 * Description : com.leyou.game.event
 *
 * @author : rocky
 * @Create Time : 2017/8/3 上午9:28
 * @Modified Time : 2017/8/3 上午9:28
 */
public class FriendFragmentRefreshEvent {
    private int type;//1刷新

    public FriendFragmentRefreshEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
