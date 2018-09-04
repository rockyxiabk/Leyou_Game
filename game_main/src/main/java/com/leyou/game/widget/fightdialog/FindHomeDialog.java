package com.leyou.game.widget.fightdialog;

import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.activity.wolfkill.WolfKillFightActivity;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.WolfKillRoomBean;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.KeyBoardUtils;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.ToastUtils;
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
 * @Create Time : 2017/7/11 下午2:33
 * @Modified Time : 2017/7/11 下午2:33
 */
public class FindHomeDialog extends BaseDialog {
    private final Context context;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.et_fight_home_id_input)
    EditText etFightHomeIdInput;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    private long homeID = 0l;
    private Handler handler = new Handler();
    private LoadingProgressDialog progressDialog;

    public FindHomeDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_fight_find_home;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        progressDialog = new LoadingProgressDialog(context, true);
        etFightHomeIdInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String string = s.toString();
                if (!TextUtils.isEmpty(string)) {
                    try {
                        homeID = Long.parseLong(string);
                    } catch (Exception e) {
                        e.printStackTrace();
                        homeID = 0l;
                    }
                }
                LogUtil.d("tag", string);
                LogUtil.d("tag", homeID + "");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick({R.id.iv_close, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                KeyBoardUtils.closeKeyboard(etFightHomeIdInput, context);
                dismiss();
                break;
            case R.id.btn_confirm:
                KeyBoardUtils.closeKeyboard(etFightHomeIdInput, context);
                findRoomByRoomId(homeID);
                break;
        }
    }

    private void findRoomByRoomId(final long homeID) {
        progressDialog.show();
        progressDialog.setLoadingText("正在查找房间...");
        HttpUtil.subscribe(HttpUtil.createApi(WolfKillApi.class, Constants.WOLF_KILL_URL).checkRoom(homeID), new Observer<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                sendRoom(0);
            }

            @Override
            public void onNext(Result result) {
                if (result.result == 1) {
                    sendRoom(homeID);
                } else {
                    sendRoom(0);
                }
            }
        });
    }

    private void sendRoom(final long homeID) {
        if (homeID > 0) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    Intent intent = new Intent(context, WolfKillFightActivity.class);
                    intent.putExtra("roomId", homeID);
                    context.startActivity(intent);
                    dismiss();
                }
            }, 1200);
        } else {
            progressDialog.dismiss();
            ToastUtils.showToastShort("房间不存在");
        }
    }
}
