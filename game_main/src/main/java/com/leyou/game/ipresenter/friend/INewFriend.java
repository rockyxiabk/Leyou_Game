package com.leyou.game.ipresenter.friend;

import com.leyou.game.dao.Friend;

import java.util.List;

/**
 * Description : com.leyou.game.ipresenter
 *
 * @author : rocky
 * @Create Time : 2017/8/12 上午11:17
 * @Modified Time : 2017/8/12 上午11:17
 */
public interface INewFriend {

    void setAddFriendList(boolean hasData, List<Friend> data);

    void newFriendAddResult(boolean result);

    void showMessageToast(String msg);
}
