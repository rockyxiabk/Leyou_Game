package com.leyou.game.ipresenter.mine;

import com.leyou.game.bean.UpdateAppBean;

/**
 * Description : com.leyou.game.ipresenter
 *
 * @author : rocky
 * @Create Time : 2017/5/15 上午10:14
 * @Modified Time : 2017/5/15 上午10:14
 */
public interface IAboutActivity {

    void showNewVersionView(UpdateAppBean data);

    void showLoading();

    void changeLoadingDes(String des);

    void dismissedLoading();

    void showMessageToast(String msg);
}
