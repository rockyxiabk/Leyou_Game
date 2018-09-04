package com.leyou.game.event;

/**
 * Description : com.leyou.game.event
 *
 * @author : rocky
 * @Create Time : 2017/5/2 下午2:18
 * @Modified Time : 2017/5/2 下午2:18
 */
public class RefreshEvent {
    private int isRefresh;
    private int sourceType;//1:我的页面，2:赢大奖，3：宝窟
    public static final int MINE = 1;
    public static final int WINAWARD = 2;
    public static final int TREASURE = 3;
    public static final int REFRESH = 1;
    public static final int NOT_REFRESH = 0;

    public RefreshEvent(int isRefresh) {
        this.isRefresh = isRefresh;
    }

    public RefreshEvent(int isRefresh, int sourceType) {
        this.isRefresh = isRefresh;
        this.sourceType = sourceType;
    }

    public int getIsRefresh() {
        return isRefresh;
    }

    public void setIsRefresh(int isRefresh) {
        this.isRefresh = isRefresh;
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }
}
