package com.leyou.game.ipresenter.friend;

import com.leyou.game.bean.GameBean;
import com.leyou.game.dao.Friend;

import java.util.List;

/**
 * Description : com.leyou.game.ipresenter
 *
 * @author : rocky
 * @Create Time : 2017/8/7 下午2:16
 * @Modified Time : 2017/8/7 下午2:16
 */
public interface IFriendDetail {
    void setFriendInfo(Friend friend);

    void showDeleteFriendResult(boolean result, String userId);

    void setGameList(boolean isLoad, List<GameBean> list);

    void showMessageToast(String msg);
}
