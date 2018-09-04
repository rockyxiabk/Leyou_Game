package com.leyou.game.ipresenter.friend;

import com.leyou.game.dao.Friend;

import java.util.List;

/**
 * Description : com.leyou.game.ipresenter
 *
 * @author : rocky
 * @Create Time : 2017/7/27 下午3:11
 * @Modified Time : 2017/7/27 下午3:11
 */
public interface IContactsActivity {
    void setNewFriendCount(int count);

    void showMyFriendNet(List<Friend> list);
}
