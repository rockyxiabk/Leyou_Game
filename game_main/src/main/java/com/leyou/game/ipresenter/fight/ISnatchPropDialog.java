package com.leyou.game.ipresenter.fight;

import com.leyou.game.bean.WolfPropBean;

import java.util.List;

/**
 * Description : com.leyou.game.ipresenter.fight
 *
 * @author : rocky
 * @Create Time : 2017/9/5 下午6:53
 * @Modified Time : 2017/9/5 下午6:53
 */
public interface ISnatchPropDialog {
    void showPropData(List<WolfPropBean> data);

    void showUsePropResult(boolean result);
}
