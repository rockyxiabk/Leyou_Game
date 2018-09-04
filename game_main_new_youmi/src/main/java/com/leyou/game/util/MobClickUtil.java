package com.leyou.game.util;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

/**
 * Description : 友盟统计
 *
 * @author : rocky
 * @Create Time : 2017/11/26 下午4:35
 * @Modified Time : 2017/11/26 下午4:35
 */
public class MobClickUtil {
    public static final String EVENT_TREASURE = "footer_treasure";//宝库
    public static final String EVENT_FRIEND = "footer_friend";//好友
    public static final String EVENT_WIN_AWARD = "footer_win_award";//赢大奖
    public static final String EVENT_GAME = "footer_game";//游戏版块
    public static final String EVENT_MINE = "footer_mine";//我的页面
    public static final String EVENT_FRIEND_RANK = "event_friend_rank";//好友排行
    public static final String EVENT_MINE_EXCHANGE = "event_mine_exchange";//交易所
    public static final String EVENT_SLIDE_GAME = "view_slide_game";//侧边栏游戏

    public static void mobEvent(Context context, String eventId) {
        MobclickAgent.onEvent(context, eventId);
    }
}
