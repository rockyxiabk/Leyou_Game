package com.leyou.game.event;

/**
 * Description : com.leyou.game.event
 *
 * @author : rocky
 * @Create Time : 2017/6/19 下午6:27
 * @Modified Time : 2017/6/19 下午6:27
 */
public class PayEvent {
    private boolean flag;//充值结果

    public PayEvent(boolean flag) {
        this.flag = flag;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
