package com.leyou.game.widget.fightdialog;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.activity.wolfkill.WolfKillFightActivity;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.WolfKillRoomBean;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.util.api.WinAwardApi;
import com.leyou.game.util.api.WolfKillApi;
import com.leyou.game.widget.dialog.LoadingProgressDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

/**
 * Description : com.leyou.game.widget.fightdialog
 *
 * @author : rocky
 * @Create Time : 2017/7/11 下午2:43
 * @Modified Time : 2017/7/11 下午2:43
 */
public class CreateHomeDialog extends BaseDialog {
    private Context context;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.iv_fight_six)
    ImageView ivFightSix;
    @BindView(R.id.iv_fight_nine)
    ImageView ivFightNine;
    @BindView(R.id.iv_fight_twelve)
    ImageView ivFightTwelve;
    private Handler handler = new Handler();
    private LoadingProgressDialog progressDialog;

    public CreateHomeDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_fight_create_home;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        progressDialog = new LoadingProgressDialog(context, true);
    }

    @OnClick({R.id.iv_close, R.id.iv_fight_six, R.id.iv_fight_nine, R.id.iv_fight_twelve})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.iv_fight_six:
                createRoom(6);
                break;
            case R.id.iv_fight_nine:
                createRoom(9);
                break;
            case R.id.iv_fight_twelve:
                createRoom(12);
                break;
        }
    }

    private void createRoom(int type) {
        progressDialog.show();
        progressDialog.setLoadingText("正在创建房间...");
        HttpUtil.subscribe(HttpUtil.createApi(WolfKillApi.class, Constants.WOLF_KILL_URL).createRoom(type), new Observer<Result<WolfKillRoomBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                sendRoom(null);
            }

            @Override
            public void onNext(Result<WolfKillRoomBean> wolfKillRoomBeanResult) {
                if (wolfKillRoomBeanResult.result == 1) {
                    WolfKillRoomBean data = wolfKillRoomBeanResult.data;
                    if (null != data) {
                        sendRoom(data);
                    } else {
                        sendRoom(null);
                    }
                } else {
                    sendRoom(null);
                }
            }
        });
    }

    private void sendRoom(final WolfKillRoomBean wolfKillRoomBean) {
        if (null != wolfKillRoomBean) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    Intent intent = new Intent(context, WolfKillFightActivity.class);
                    intent.putExtra("roomId", wolfKillRoomBean.roomId);
                    intent.putExtra("comment", wolfKillRoomBean.comment);
                    context.startActivity(intent);
                    dismiss();
                }
            }, 1200);
        } else {
            progressDialog.dismiss();
            ToastUtils.showToastShort("创建房间失败");
        }
    }
}
