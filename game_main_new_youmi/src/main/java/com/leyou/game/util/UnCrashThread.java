package com.leyou.game.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.widget.Toast;

import com.leyou.game.GameApplication;
import com.leyou.game.SplashActivity;
import com.leyou.game.activity.MainActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * Description : 处理异常崩溃后重新起动应用
 *
 * @author : rocky
 * @Create Time : 2017/4/5 下午3:06
 * @Modified By: rocky
 * @Modified Time : 2017/4/5 下午3:06
 */
public class UnCrashThread implements Thread.UncaughtExceptionHandler {

    private final Thread.UncaughtExceptionHandler mDefaultHandler;
    private GameApplication application;

    public UnCrashThread(GameApplication application) {
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        this.application = application;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        MobclickAgent.reportError(application, ex);
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            Intent intent = new Intent(application.getApplicationContext(), SplashActivity.class);
            PendingIntent restartIntent = PendingIntent.getActivity(application.getApplicationContext(), 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
            // 退出程序
            AlarmManager mgr = (AlarmManager) application.getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent); // 1秒钟后重启应用
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    }

    /**
     * 自定义错误处理,收集错误信息
     * 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息，否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        // 使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(application.getApplicationContext(), "很抱歉,程序出现异常,即将退出", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }.start();
        return true;
    }
}
