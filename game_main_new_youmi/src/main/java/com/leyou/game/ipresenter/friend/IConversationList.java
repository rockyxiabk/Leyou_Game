package com.leyou.game.ipresenter.friend;

import com.leyou.game.dao.Crowd;
import com.leyou.game.dao.Friend;

/**
 * Description : com.leyou.game.ipresenter.friend
 *
 * @author : rocky
 * @Create Time : 2017/8/26 下午6:08
 * @Modified Time : 2017/8/26 下午6:08
 */
public interface IConversationList {
    void setFriendInfo(Friend friend);

    void setCrowdInfo(boolean flag, Crowd crowd);
}
