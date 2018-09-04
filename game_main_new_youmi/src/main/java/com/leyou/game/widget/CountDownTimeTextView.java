package com.leyou.game.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.widget.TextView;

import com.leyou.game.event.WorkerCanEmployEvent;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;

/**
 * Description : 自定义倒计时控件
 *
 * @author : rocky
 * @Create Time : 2017/6/6 下午3:39
 * @Modified By: rocky
 * @Modified Time : 2017/6/6 下午3:39
 */
public class CountDownTimeTextView extends TextView {

    private boolean mCancelled = false;
    private long mStopTimeInFuture;
    private static final int MSG = 1;
    private long mMillisInFuture;//要显示的倒计时
    private long mCountdownInterval;//时间间隔
    private SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");

    public CountDownTimeTextView(Context context) {
        super(context);
    }


    public CountDownTimeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setCountdownInterval(long mCountdownInterval) {
        this.mCountdownInterval = mCountdownInterval;
    }

    public long getCountdownInterval() {
        return mCountdownInterval;
    }

    public void setMillisInFuture(long mMillisInFuture) {
        this.mMillisInFuture = mMillisInFuture;
    }

    public void setSimpleDataFormat(SimpleDateFormat simpleDataFormat) {
        this.formatTime = simpleDataFormat;
    }

    public long getMillisInFuture() {
        return mMillisInFuture;
    }

    /**
     * Cancel the countdown.
     */
    public synchronized final void cancel() {
        mCancelled = true;
        mHandler.removeMessages(MSG);
    }

    /**
     * Start the countdown.
     */
    public synchronized final CountDownTimeTextView start() {
        mCancelled = false;
        if (getMillisInFuture() <= 0) {
            onFinish();
            return this;
        }
        mStopTimeInFuture = SystemClock.elapsedRealtime() + getMillisInFuture();
        mHandler.sendMessage(mHandler.obtainMessage(MSG));
        return this;
    }

    private void onFinish() {
        EventBus.getDefault().post(new WorkerCanEmployEvent(1));
        setText("00:00:00");
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            synchronized (CountDownTimeTextView.this) {
                if (mCancelled) {
                    return;
                }

                final long millisLeft = mStopTimeInFuture - SystemClock.elapsedRealtime();

                if (millisLeft <= 0) {
                    onFinish();
                } else if (millisLeft < getCountdownInterval()) {
                    sendMessageDelayed(obtainMessage(MSG), millisLeft);
                } else {
                    long lastTickStart = SystemClock.elapsedRealtime();
                    setText(formatTime.format((millisLeft - 8 * 60 * 60 * 1000)));
                    long delay = lastTickStart + getCountdownInterval() - SystemClock.elapsedRealtime();
                    while (delay < 0) {
                        delay += getCountdownInterval();
                    }

                    sendMessageDelayed(obtainMessage(MSG), delay);
                }
            }
        }
    };
}
