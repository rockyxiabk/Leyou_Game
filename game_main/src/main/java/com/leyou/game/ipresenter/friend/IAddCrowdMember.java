package com.leyou.game.ipresenter.friend;

import com.leyou.game.dao.Friend;

import java.util.List;

/**
 * Description : com.leyou.game.ipresenter.friend
 *
 * @author : rocky
 * @Create Time : 2017/8/31 下午6:27
 * @Modified Time : 2017/8/31 下午6:27
 */
public interface IAddCrowdMember {
    void showCrowdMemberAdapter(List<Friend> data);

    void showInviteResult(boolean result);

    void showMessageToast(String msg);
}
