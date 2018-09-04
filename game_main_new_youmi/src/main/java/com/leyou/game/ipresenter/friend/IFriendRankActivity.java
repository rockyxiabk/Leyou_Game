package com.leyou.game.ipresenter.friend;

import com.leyou.game.bean.game.GameBean;

import java.util.List;

/**
 * Description : com.leyou.game.ipresenter
 *
 * @author : rocky
 * @Create Time : 2017/7/26 上午9:39
 * @Modified Time : 2017/7/26 上午9:39
 */
public interface IFriendRankActivity {

    void showData(List<GameBean> list);

    void showError();
}
