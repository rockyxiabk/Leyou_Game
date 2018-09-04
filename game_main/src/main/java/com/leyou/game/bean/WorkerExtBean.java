package com.leyou.game.bean;

/**
 * Description : com.leyou.game.bean
 *
 * @author : rocky
 * @Create Time : 2017/6/6 下午3:14
 * @Modified Time : 2017/6/6 下午3:14
 */
public class WorkerExtBean {
    public long nextRefreshTime;//下次刷新时间时间戳
    public int presentWorkerNumber;//当前拥有矿工
    public int presentWorkerPlaceholder;//当前拥有最大矿工位
    public String message;//若失败返回失败信息，若成功此字段返回null
}
