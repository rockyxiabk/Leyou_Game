package com.leyou.game.ipresenter.friend;

import com.leyou.game.dao.Crowd;
import com.leyou.game.dao.Friend;
import com.leyou.game.ipresenter.IBasePresenter;

import java.util.List;

/**
 * Description : com.leyou.game.ipresenter.friend
 *
 * @author : rocky
 * @Create Time : 2017/12/6 下午5:55
 * @Modified Time : 2017/12/6 下午5:55
 */
public interface IAddFriend extends IBasePresenter {

    void showRecommendFriend(List<Friend> friends);

    void showRecommendCrowd(List<Crowd> crowds);
}
