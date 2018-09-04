package com.leyou.game.activity.mine;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.UserData;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.util.newapi.SystemApi;
import com.leyou.game.widget.dialog.AgreementDialog;
import com.leyou.game.zxing.CaptureActivity;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

/**
 * Description : 登陆网站
 *
 * @author : rocky
 * @Create Time : 2017/7/7 上午11:01
 * @Modified By: rocky
 * @Modified Time : 2017/7/7 上午11:01
 */
public class LogInWebActivity extends BaseActivity {

    public static final int RESULT_CODE = 9527;
    @BindView(R.id.iv_url)
    ImageView ivUrl;
    @BindView(R.id.btn_scan)
    Button btnScan;
    private String code;
    private int state;

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_log_in_web;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);
    }

    @Override
    public void initPresenter() {
        if (UserData.getInstance().getIsDeveloper() == 1) {
            new AgreementDialog(this).show();
        }
    }

    @OnClick({R.id.iv_about_back, R.id.iv_url, R.id.btn_scan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_about_back:
                finishCurrentActivity();
                break;
            case R.id.iv_url:
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText("http://www.pandawanwan.com");
                ToastUtils.showToastShort("http://www.pandawanwan.com已复制到剪切板");
                break;
            case R.id.btn_scan:
                if (btnScan.getText().toString().equalsIgnoreCase("已扫描，确认登录")) {
                    finishCurrentActivity();
                } else if (btnScan.getText().toString().equalsIgnoreCase("已打开网址,点击扫描登录")) {
                    startActivityForResult(new Intent(this, CaptureActivity.class), RESULT_CODE);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CODE) {
            code = data.getStringExtra("code");
            btnScan.setText("已扫描，确认登录");
            sendRequestCode(code);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getName());
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getName());
        MobclickAgent.onPause(this);
    }

    private void sendRequestCode(String code) {
        HttpUtil.subscribe(HttpUtil.createApi(SystemApi.class, Constants.DEVELOP_URL).sendCode(code), new Observer<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Result result) {
                if (result.result == 1) {
                    btnScan.setText("已扫描，确认登录");
                } else {
                    btnScan.setText("已打开网址,点击扫描登录");
                }
            }
        });
    }
}
