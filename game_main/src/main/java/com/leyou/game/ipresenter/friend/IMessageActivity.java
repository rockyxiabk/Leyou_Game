package com.leyou.game.ipresenter.friend;

import com.leyou.game.bean.MessageBean;

import java.util.List;

/**
 * Description : com.leyou.game.ipresenter
 *
 * @author : rocky
 * @Create Time : 2017/5/22 下午4:53
 * @Modified Time : 2017/5/22 下午4:53
 */
public interface IMessageActivity {

    void showLoadMoreList(List<MessageBean> messageBeanList);

    void setMessageLoadAll(boolean isLoadAll);

    void showErrorView();

    void showMessageToast(String msg);
}
