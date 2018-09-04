package com.leyou.game.event;

/**
 * Description : com.leyou.game.event
 *
 * @author : rocky
 * @Create Time : 2017/8/26 下午6:29
 * @Modified Time : 2017/8/26 下午6:29
 */
public class FriendDeleteEvent {
    private boolean result;
    private String userId;

    public FriendDeleteEvent(boolean result) {
        this.result = result;
    }

    public FriendDeleteEvent(boolean result, String userId) {
        this.result = result;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
