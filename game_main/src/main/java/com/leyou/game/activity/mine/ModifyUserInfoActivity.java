package com.leyou.game.activity.mine;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.leyou.game.R;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.bean.UserData;
import com.leyou.game.event.RefreshEvent;
import com.leyou.game.ipresenter.mine.IModifyUserInfoActivity;
import com.leyou.game.presenter.mine.ModifyUserInfoPresenter;
import com.leyou.game.util.DataUtil;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.widget.clip.ClipImageLayout;
import com.leyou.game.widget.dialog.LoadingProgressDialog;
import com.leyou.game.widget.dialog.NickNameDialog;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;

/**
 * Description : 用户信息编辑页面
 *
 * @author : rocky
 * @Create Time : 2017/4/18 上午10:45
 * @Modified By: rocky
 * @Modified Time : 2017/4/18 上午10:45
 */
public class ModifyUserInfoActivity extends BaseActivity implements NickNameDialog.setNickNameListener, IModifyUserInfoActivity {
    private static final String TAG = "ModifyUserInfoActivity";
    @BindView(R.id.iv_person_back)
    ImageView ivPersonBack;
    @BindView(R.id.iv_modify_header_img)
    SimpleDraweeView ivModifyHeaderImg;
    @BindView(R.id.re_person_modify_header_img)
    RelativeLayout rePersonModifyHeaderImg;
    @BindView(R.id.tv_modify_name)
    TextView tvModifyName;
    @BindView(R.id.re_person_modify_name)
    RelativeLayout rePersonModifyName;
    @BindView(R.id.tv_modify_year)
    TextView tvModifyYear;
    @BindView(R.id.re_person_modify_year)
    RelativeLayout rePersonModifyYear;
    @BindView(R.id.tv_modify_sex)
    TextView tvModifySex;
    @BindView(R.id.re_person_modify_sex)
    RelativeLayout rePersonModifySex;
    @BindView(R.id.tv_modify_phone)
    TextView tvModifyPhone;
    @BindView(R.id.tv_person_info_save)
    TextView tvPersonInfoSave;
    @BindView(R.id.re_modify_title)
    RelativeLayout reModifyTitle;
    @BindView(R.id.re_popup_bg)
    RelativeLayout rePopupBg;
    private NickNameDialog nickNameDialog;
    private ModifyUserInfoPresenter presenter;
    private View rootPopup;
    private PopupWindow popupWindow;
    private LinearLayout llBackPopup;
    private TextView btnCamera;
    private TextView btnGallery;
    private TextView btnCancel;
    private ArrayList<String> photoList = new ArrayList<>();
    private View rootSexPopup;
    private LinearLayout llSexBackPopup;
    private TextView tvMale;
    private TextView tvFemale;
    private TextView tvSexCancel;
    private PopupWindow popupWindowSex;
    private long birthdayModify;
    private int sexModify;
    private String headImgModify;
    private String nickNameModify;
    private LoadingProgressDialog loadingProgressDialog;

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_modify_user_info;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);

        LogUtil.d(TAG, "---->userInfo:birthday:" + UserData.getInstance().getBirthday() + "---sex:" + UserData.getInstance().getSex());
        ivModifyHeaderImg.setImageURI(UserData.getInstance().getPictureUrl());
        tvModifyName.setText(UserData.getInstance().getNickname());
        tvModifyYear.setText(DataUtil.getConvertResult(UserData.getInstance().getBirthday(), DataUtil.Y_M_D));
        birthdayModify = UserData.getInstance().getBirthday();
        tvModifySex.setText(UserData.getInstance().getSex() == 0 ? "女" : "男");
        sexModify = UserData.getInstance().getSex();
        tvModifyPhone.setText(UserData.getInstance().getPhoneNum());

        //init photo selector popUpWindow
        View inflate = LayoutInflater.from(this).inflate(R.layout.layout_selector_img_popup, null);
        rootPopup = inflate.findViewById(R.id.ll_root_popup);
        llBackPopup = ButterKnife.findById(inflate, R.id.ll_back_popup);
        llBackPopup.setOnClickListener(this);
        btnCamera = ButterKnife.findById(inflate, R.id.tv_popup_camera);
        btnCamera.setOnClickListener(this);
        btnGallery = ButterKnife.findById(inflate, R.id.tv_popup_gallery);
        btnGallery.setOnClickListener(this);
        btnCancel = ButterKnife.findById(inflate, R.id.tv_popup_cancel);
        btnCancel.setOnClickListener(this);
        popupWindow = new PopupWindow(inflate, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        rootPopup.setFocusable(true);
        rootPopup.setFocusableInTouchMode(true);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);//防止被底部虚拟键挡住
        popupWindow.setAnimationStyle(R.style.popWindow_anim_style);

        //init sex change popUpWindow
        View inflateSex = LayoutInflater.from(this).inflate(R.layout.layout_sex_selector, null);
        rootSexPopup = inflateSex.findViewById(R.id.ll_sex_root_popup);
        llSexBackPopup = ButterKnife.findById(inflateSex, R.id.ll_sex_back_popup);
        llSexBackPopup.setOnClickListener(this);
        tvMale = ButterKnife.findById(inflateSex, R.id.tv_popup_male);
        tvMale.setOnClickListener(this);
        tvFemale = ButterKnife.findById(inflateSex, R.id.tv_popup_female);
        tvFemale.setOnClickListener(this);
        tvSexCancel = ButterKnife.findById(inflateSex, R.id.tv_sex_popup_cancel);
        tvSexCancel.setOnClickListener(this);
        popupWindowSex = new PopupWindow(inflateSex, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        rootSexPopup.setFocusable(true);
        rootSexPopup.setFocusableInTouchMode(true);
        popupWindowSex.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);//防止被底部虚拟键挡住
        popupWindowSex.setAnimationStyle(R.style.popWindow_anim_style);
    }

    @Override
    public void initPresenter() {
        presenter = new ModifyUserInfoPresenter(this, this);
    }

    @OnClick({R.id.tv_person_info_save, R.id.iv_person_back, R.id.re_person_modify_header_img, R.id.re_person_modify_name, R.id.re_person_modify_year, R.id.re_person_modify_sex})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_person_info_save:
                showLoadingView();
                presenter.savePersonInfo(nickNameModify, headImgModify, birthdayModify, sexModify);
                break;
            case R.id.iv_person_back:
                finishCurrentActivity();
                break;
            case R.id.re_person_modify_header_img:
                showPopUpWindow();
                break;
            case R.id.re_person_modify_name:
                nickNameDialog = new NickNameDialog(this, tvModifyName.getText().toString());
                nickNameDialog.setOnSetNickNameListener(this);
                nickNameDialog.show();
                break;
            case R.id.re_person_modify_year:
                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        LogUtil.d(TAG, "---year:" + year + "--month:" + monthOfYear + "--day:" + dayOfMonth);
                        showBirthDay(DataUtil.getTimeStampBuyTime(year, monthOfYear + 1, dayOfMonth));
                    }
                }, DataUtil.getResultByTimeStamp(UserData.getInstance().getBirthday(), DataUtil.Y),
                        DataUtil.getResultByTimeStamp(UserData.getInstance().getBirthday(), DataUtil.M) - 1,
                        DataUtil.getResultByTimeStamp(UserData.getInstance().getBirthday(), DataUtil.D))
                        .show();
                break;
            case R.id.re_person_modify_sex:
                showPopUpWindowSex();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_back_popup:
                hiddenPopUpWindow();
                break;
            case R.id.tv_popup_camera:
                PhotoPicker.builder()
                        .setPhotoCount(1)
                        .setShowCamera(true)
                        .setShowGif(true)
                        .setPreviewEnabled(false)
                        .start(this, PhotoPicker.REQUEST_CODE);
                hiddenPopUpWindow();
                break;
            case R.id.tv_popup_gallery:
                PhotoPicker.builder()
                        .setPhotoCount(1)
                        .setShowCamera(false)
                        .setShowGif(true)
                        .setPreviewEnabled(false)
                        .start(this, PhotoPicker.REQUEST_CODE);
                hiddenPopUpWindow();
                break;
            case R.id.tv_popup_cancel:
                hiddenPopUpWindow();
                break;
            case R.id.ll_sex_back_popup:
                hiddenPopUpWindowSex();
                break;
            case R.id.tv_popup_female:
                showSex(0);
                hiddenPopUpWindowSex();
                break;
            case R.id.tv_popup_male:
                showSex(1);
                hiddenPopUpWindowSex();
                break;
            case R.id.tv_sex_popup_cancel:
                hiddenPopUpWindowSex();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                photoList = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                if (null != photoList && photoList.size() > 0) {
                    Intent intent = new Intent(this, ClipImageActivity.class);
                    intent.putExtra("imagePath", photoList.get(0));
                    startActivityForResult(intent, ClipImageLayout.REQUEST_CODE);
                }
            }
        } else if (resultCode == RESULT_OK && requestCode == ClipImageLayout.REQUEST_CODE) {
            if (data != null) {
                String image = data.getStringExtra("image");
                showHeadImg(image);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        hiddenPopUpWindowSex();
        hiddenPopUpWindow();
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void setNickName(String name) {
        nickNameModify = name;
        tvModifyName.setText(name);
    }

    @Override
    public void showPopUpWindow() {
        rePopupBg.setVisibility(View.VISIBLE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                popupWindow.showAtLocation(rePopupBg, Gravity.BOTTOM, 0, 0);
            }
        }, 50);

    }

    @Override
    public void showPopUpWindowSex() {
        rePopupBg.setVisibility(View.VISIBLE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                popupWindowSex.showAtLocation(rePopupBg, Gravity.BOTTOM, 0, 0);
            }
        }, 50);
    }

    @Override
    public void hiddenPopUpWindow() {
        popupWindow.dismiss();
        rePopupBg.setVisibility(View.GONE);
    }

    @Override
    public void hiddenPopUpWindowSex() {
        popupWindowSex.dismiss();
        rePopupBg.setVisibility(View.GONE);
    }

    @Override
    public void showHeadImg(String url) {
        headImgModify = url;
        ivModifyHeaderImg.setImageURI("file://" + url);
    }

    @Override
    public void showBirthDay(long birthday) {
        birthdayModify = birthday;
        LogUtil.d(TAG, "----birthday-->" + birthday);
        tvModifyYear.setText(DataUtil.getConvertResult(birthday, DataUtil.Y_M_D));
    }

    @Override
    public void showSex(int flag) {
        sexModify = flag;
        tvModifySex.setText(flag == 0 ? "女" : "男");
    }

    @Override
    public void showLoadingView() {
        loadingProgressDialog = new LoadingProgressDialog(this, false);
        loadingProgressDialog.show();
    }

    @Override
    public void dismissedLoading() {
        loadingProgressDialog.dismiss();
    }

    @Override
    public void setLoadingDes(String des) {
        loadingProgressDialog.setLoadingText(des);
    }

    @Override
    public void showMessage(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showToastShort(msg);
            }
        });
    }

    @Override
    public void toOtherActivity(int flag) {
        EventBus.getDefault().post(new RefreshEvent(RefreshEvent.REFRESH, 1));
        finishCurrentActivity();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
