package com.leyou.game.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Description : 接口返回基类
 *
 * @author : rocky
 * @Create Time : 2017/4/12 上午9:46
 * @Modified By: rocky
 * @Modified Time : 2017/4/12 上午9:46
 */
public class Result<T> {
    public int result;//返回结果状态，1：成功 0:失败
    public String msg;//相关错误信息
    @SerializedName("data")
    public T data;
}
