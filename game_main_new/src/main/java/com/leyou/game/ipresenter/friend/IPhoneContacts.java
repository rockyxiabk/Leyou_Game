package com.leyou.game.ipresenter.friend;

import com.leyou.game.dao.PhoneContact;

import java.util.List;

/**
 * Description : com.leyou.game.ipresenter
 *
 * @author : rocky
 * @Create Time : 2017/8/14 下午2:17
 * @Modified Time : 2017/8/14 下午2:17
 */
public interface IPhoneContacts {

    void showPhoneContactsList(List<PhoneContact> friends);

    void showNetContactSList(List<PhoneContact> friends);

    void showListOrErrorView(boolean isAuthority);

    void showLoading();

    void changeLoadingDes(String des);

    void dismissedLoading();


    void showMessageToast(String msg);
}
