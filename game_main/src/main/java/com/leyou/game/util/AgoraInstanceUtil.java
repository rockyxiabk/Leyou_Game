package com.leyou.game.util;

import android.content.Context;

import com.agora.CurrentUserSettings;
import com.agora.WorkerThread;

/**
 * Description : com.leyou.game.util
 *
 * @author : rocky
 * @Create Time : 2017/7/17 下午5:38
 * @Modified Time : 2017/7/17 下午5:38
 */
public class AgoraInstanceUtil {
    private static AgoraInstanceUtil instance;
    private static Context context;
    private WorkerThread mWorkerThread;

    public static AgoraInstanceUtil getInstance(Context context) {
        if (null == instance) {
            instance = new AgoraInstanceUtil();
            AgoraInstanceUtil.context = context;
        }
        return instance;
    }

    public synchronized void initWorkerThread() {
        if (mWorkerThread == null) {
            mWorkerThread = new WorkerThread(context);
            mWorkerThread.start();

            mWorkerThread.waitForReady();
        }
    }

    public synchronized WorkerThread getWorkerThread() {
        return mWorkerThread;
    }

    public static final CurrentUserSettings mAudioSettings = new CurrentUserSettings();
}
