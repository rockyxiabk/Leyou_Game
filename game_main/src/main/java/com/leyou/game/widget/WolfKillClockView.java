package com.leyou.game.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leyou.game.R;

/**
 * Description : 自定义组合控件 显示狼人杀倒计时
 *
 * @author : rocky
 * @Create Time : 2017/7/31 上午10:25
 * @Modified Time : 2017/7/31 上午10:25
 */
public class WolfKillClockView extends RelativeLayout {

    private TextView tvCuntDownTime;
    private int second;
    private boolean isCancel = false;
    private InternalCountTimeListener listener;

    public WolfKillClockView(Context context) {
        super(context);
    }

    public WolfKillClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_dialog_wolf_kill_room_clock, this, true);
    }

    public WolfKillClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setCountDownClockListener(InternalCountTimeListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tvCuntDownTime = ((TextView) findViewById(R.id.tv_cunt_down_time));
    }

    public void setCountDownTimeSecond(int second) {
        this.second = second;
    }

    private void setTextView(int timer) {
        tvCuntDownTime.setText(String.valueOf(timer));
    }

    public synchronized void start() {
        this.setVisibility(VISIBLE);
        isCancel = false;
        handler.sendEmptyMessage(1);
        listener.clockFinish(false);
    }

    public synchronized void cancel() {
        isCancel = true;
        handler.removeMessages(1);
        this.setVisibility(GONE);
    }

    private void onFinish() {
        setTextView(0);
        listener.clockFinish(true);
        this.setVisibility(GONE);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            synchronized (WolfKillClockView.class) {
                if (isCancel) {
                    return;
                }
                if (second >= 0) {
                    setTextView(second);
                    second = second - 1;
                    sendMessageDelayed(obtainMessage(1), 1000);
                } else {
                    onFinish();
                }
            }
        }
    };

    public interface InternalCountTimeListener {
        void clockFinish(boolean isClockFinish);
    }
}
