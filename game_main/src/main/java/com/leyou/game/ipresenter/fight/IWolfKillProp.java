package com.leyou.game.ipresenter.fight;

import com.leyou.game.bean.WolfPropBean;

import java.util.List;

/**
 * Description : com.leyou.game.ipresenter
 *
 * @author : rocky
 * @Create Time : 2017/7/19 下午4:45
 * @Modified Time : 2017/7/19 下午4:45
 */
public interface IWolfKillProp {
    void showPropList(List<WolfPropBean> data);

    void isShowNullListView(boolean flag);

    void showMessage(String msg);
}
