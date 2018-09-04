package com.leyou.game.event;

/**
 * Description : com.leyou.game.event
 *
 * @author : rocky
 * @Create Time : 2017/9/11 下午12:17
 * @Modified Time : 2017/9/11 下午12:17
 */
public class FriendAddEvent {
    private boolean result;
    private String userId;

    public FriendAddEvent(boolean result) {
        this.result = result;
    }

    public FriendAddEvent(boolean result, String userId) {
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
