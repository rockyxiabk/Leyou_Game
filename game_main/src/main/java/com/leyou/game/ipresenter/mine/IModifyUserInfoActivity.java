package com.leyou.game.ipresenter.mine;

/**
 * Description : com.leyou.game.ipresenter
 *
 * @author : rocky
 * @Create Time : 2017/5/2 下午5:28
 * @Modified Time : 2017/5/2 下午5:28
 */
public interface IModifyUserInfoActivity {

    void showPopUpWindow();

    void showPopUpWindowSex();

    void hiddenPopUpWindow();

    void hiddenPopUpWindowSex();

    void showHeadImg(String url);

    void showBirthDay(long birthday);

    void showSex(int flag);

    void showLoadingView();

    void dismissedLoading();

    void setLoadingDes(String des);

    void showMessage(String msg);

    void toOtherActivity(int flag);
}
