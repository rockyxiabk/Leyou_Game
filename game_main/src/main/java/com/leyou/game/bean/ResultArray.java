package com.leyou.game.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Description : 接口返回集合的基类
 *
 * @author : rocky
 * @Create Time : 2017/4/12 上午9:46
 * @Modified By: rocky
 * @Modified Time : 2017/4/12 上午9:46
 */

public class ResultArray<T> {
    public int result;//返回结果状态，1：成功 0:失败
    @SerializedName("data")
    public List<T> data;
}
