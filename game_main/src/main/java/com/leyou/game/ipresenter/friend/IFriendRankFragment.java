package com.leyou.game.ipresenter.friend;

import com.leyou.game.bean.ContactBean;
import com.leyou.game.bean.GameRankBean;

import java.util.List;

/**
 * Description : com.leyou.game.ipresenter
 *
 * @author : rocky
 * @Create Time : 2017/7/26 上午10:03
 * @Modified Time : 2017/7/26 上午10:03
 */
public interface IFriendRankFragment {
    void showData(List<GameRankBean> list);

    void showError();
}
