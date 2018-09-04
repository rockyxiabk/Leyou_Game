package com.leyou.game.ipresenter.friend;

import com.leyou.game.dao.Friend;

import java.util.List;

/**
 * Description : com.leyou.game.ipresenter.friend
 *
 * @author : rocky
 * @Create Time : 2017/8/31 下午9:57
 * @Modified Time : 2017/8/31 下午9:57
 */
public interface IDeleteCrowdMember {
    void showCrowdMemberAdapter(List<Friend> data);

    void showMessageToast(String msg);
}
