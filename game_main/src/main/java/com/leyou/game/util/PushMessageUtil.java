package com.leyou.game.util;

import android.content.Context;
import android.content.Intent;

import com.leyou.game.service.PushConvertService;

/**
 * Description : 处理自定义的消息
 *
 * @author : rocky
 * @Create Time : 2017/6/2 下午5:50
 * @Modified Time : 2017/6/2 下午5:50
 */
public class PushMessageUtil {

    public static void startService(Context context, String json) {
        Intent intent = new Intent(context, PushConvertService.class);
        intent.putExtra("push", json);
        context.startService(intent);
    }

}
