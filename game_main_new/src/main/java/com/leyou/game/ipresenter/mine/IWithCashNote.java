package com.leyou.game.ipresenter.mine;

import com.leyou.game.bean.user.WithCashNoteBean;
import com.leyou.game.ipresenter.IBasePresenter;

import java.util.List;

/**
 * Description : com.leyou.game.ipresenter.mine
 *
 * @author : rocky
 * @Create Time : 2017/11/29 下午2:35
 * @Modified Time : 2017/11/29 下午2:35
 */
public interface IWithCashNote extends IBasePresenter {
    void showDataList(List<WithCashNoteBean> messageBeanList);

    void showLoadMoreList(List<WithCashNoteBean> messageBeanList);

    void setConsumeLoadAll(boolean isLoadAll);

    void showNullView();
}
