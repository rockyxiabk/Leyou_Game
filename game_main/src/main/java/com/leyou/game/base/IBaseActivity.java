package com.leyou.game.base;

/**
 * Description :
 *
 * @author : rocky
 * @Create Time : 2017/3/24 上午11:00
 * @Modified By: ***
 * @Modified Time : 2017/3/24 上午11:00
 */
public interface IBaseActivity {
    /**
     * 网络是否连接
     *
     * @return
     */
    boolean netWorkConnection();

    /**
     * 打开网络设置
     */
    void openWifiSetting();

    /**
     * 弹出对话框
     */
    void showDialog();
}
