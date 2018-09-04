package com.leyou.game.ipresenter.friend;

import com.leyou.game.dao.Crowd;

/**
 * Description : com.leyou.game.ipresenter.friend
 *
 * @author : rocky
 * @Create Time : 2017/8/25 下午3:29
 * @Modified Time : 2017/8/25 下午3:29
 */
public interface ICreateCrowd {

    void showCreateCrowdResult(boolean result, Crowd crowd);

    void showMessageToast(String msg);
}
