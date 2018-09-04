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
public interface ICrowdApply extends IBasePresenter {

    void agreeApply(Crowd crowd);

    void ignoreApply(Crowd crowd);

    void showApplyCrowd(List<Crowd> crowds);

    void showNullView();
}
