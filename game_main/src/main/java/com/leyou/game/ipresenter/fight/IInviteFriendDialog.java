package com.leyou.game.ipresenter.fight;

import com.leyou.game.bean.ContactBean;
import com.leyou.game.dao.Friend;

import java.util.List;

/**
 * Description : com.leyou.game.ipresenter
 *
 * @author : rocky
 * @Create Time : 2017/7/26 下午4:54
 * @Modified Time : 2017/7/26 下午4:54
 */
public interface IInviteFriendDialog {
    void showLoadingView();

    void showErrorView();

    void showListView();

    void showData(List<Friend> list);

    void showError(String error);
}
