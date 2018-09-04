package com.leyou.game.ipresenter.mine;

import com.leyou.game.bean.ConsumeBean;

import java.util.List;

/**
 * Description : com.leyou.game.ipresenter
 *
 * @author : rocky
 * @Create Time : 2017/8/10 下午3:58
 * @Modified Time : 2017/8/10 下午3:58
 */
public interface IConsumeActivity {

    void showLoadMoreList(List<ConsumeBean> messageBeanList);

    void setConsumeLoadAll(boolean isLoadAll);

    void showErrorView();

    void showMessageToast(String msg);
}
