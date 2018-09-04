package com.leyou.game.ipresenter.mine;

import com.leyou.game.ipresenter.IBasePresenter;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Description : com.leyou.game.ipresenter.mine
 *
 * @author : rocky
 * @Create Time : 2017/8/23 上午11:06
 * @Modified Time : 2017/8/23 上午11:06
 */
public interface IWithCashApply extends IBasePresenter {

    void showWithCashShareView();

    void shareTo(SHARE_MEDIA type);

    void startShare(String path);

    void commitApply();

    void showApplyCashResult(boolean result, String msg);

    void showMessageToast(String msg);
}
