package com.leyou.game.event;

/**
 * Description : com.leyou.game.event
 *
 * @author : rocky
 * @Create Time : 2017/8/26 下午3:53
 * @Modified Time : 2017/8/26 下午3:53
 */
public class NewFriendAddEvent {
    private boolean flag;//好友添加结果

    public NewFriendAddEvent(boolean flag) {
        this.flag = flag;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
