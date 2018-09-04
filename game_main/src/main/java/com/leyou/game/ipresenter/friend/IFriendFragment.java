package com.leyou.game.ipresenter.friend;

import java.util.List;

import io.rong.imlib.model.Conversation;

/**
 * Description : com.leyou.game.ipresenter
 *
 * @author : rocky
 * @Create Time : 2017/7/22 下午2:18
 * @Modified Time : 2017/7/22 下午2:18
 */
public interface IFriendFragment {

    void setMessageCount(int count);

    void setNewFriendCount(int count);

    void setConversationList(List<Conversation> data);

    void setConversationListNull(boolean flag);
}
