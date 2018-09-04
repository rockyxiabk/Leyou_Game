package com.leyou.game.ipresenter.fight;

import com.leyou.game.bean.WolfKillMilitaryBean;
import com.leyou.game.bean.WolfRoleBean;

import java.util.List;

/**
 * Description : com.leyou.game.ipresenter
 *
 * @author : rocky
 * @Create Time : 2017/7/16 下午3:54
 * @Modified Time : 2017/7/16 下午3:54
 */
public interface IWolfKillScore {

    void showWolfRolePercent(List<WolfRoleBean> data);

    void refreshFightResultList(List<WolfKillMilitaryBean> data);

    void loadMoreFightResultList(List<WolfKillMilitaryBean> data);

    void setMilitaryIsLoadAll(boolean flag);

}
