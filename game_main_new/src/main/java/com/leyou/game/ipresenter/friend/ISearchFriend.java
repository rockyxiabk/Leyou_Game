package com.leyou.game.ipresenter.friend;

import com.leyou.game.dao.Crowd;
import com.leyou.game.dao.Friend;
import com.leyou.game.ipresenter.IBasePresenter;

import java.util.List;

/**
 * Description : com.leyou.game.ipresenter.friend
 *
 * @author : rocky
 * @Create Time : 2017/12/6 下午6:03
 * @Modified Time : 2017/12/6 下午6:03
 */
public interface ISearchFriend extends IBasePresenter {

    void showSearchFriend(List<Friend> friends);

    void showSearchCrowd(List<Crowd> crowds);

    void showNullView();
}
