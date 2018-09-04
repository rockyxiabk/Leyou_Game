package com.leyou.game.ipresenter;

import com.leyou.game.bean.PushBean;
import com.leyou.game.bean.UpdateAppBean;

/**
 * Description : com.leyou.game.ipresenter
 *
 * @author : rocky
 * @Create Time : 2017/6/28 上午11:29
 * @Modified Time : 2017/6/28 上午11:29
 */
public interface IMainActivity {

    void showDiamondConvert();

    void showPrizeView(PushBean pushBean, String prizeId);

    void showNewVersionView(UpdateAppBean updateAppBean);

    void offLineDialog(PushBean offLineInfo);

    void clearDownloadFile(boolean flag);

    void setMessageCount(int count);
}
