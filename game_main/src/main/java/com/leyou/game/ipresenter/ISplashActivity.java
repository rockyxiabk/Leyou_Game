package com.leyou.game.ipresenter;

import com.leyou.game.bean.AdBean;

/**
 * Description : com.leyou.game.ipresenter
 *
 * @author : rocky
 * @Create Time : 2017/4/19 下午7:27
 * @Modified Time : 2017/4/19 下午7:27
 */
public interface ISplashActivity {

    void showMessageToast(String msg);

    void showAdView(AdBean adBean);

    void setLogInState(boolean isLogIn);

    void startLogoIn();

    void startApp();
}
