package com.leyou.game.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.leyou.game.R;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.bean.UserData;
import com.leyou.game.bean.user.BankCardBean;
import com.leyou.game.event.RefreshEvent;
import com.leyou.game.ipresenter.mine.IModifyUserInfoActivity;
import com.leyou.game.presenter.mine.ModifyUserInfoPresenter;
import com.leyou.game.util.DataUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.widget.clip.ClipImageLayout;
import com.leyou.game.widget.dialog.LoadingProgressDialog;
import com.leyou.game.widget.dialog.mine.ChangePhoneDialog;
import com.leyou.game.widget.dialog.mine.EditTextDialog;
import com.leyou.game.widget.dialog.mine.ModifyItemChooseDialog;
import com.leyou.game.widget.wheel.dialog.ChangeBirthdayDialog;
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
public class ModifyUserInfoActivity extends BaseActivity implements IModifyUserInfoActivity {
    private static final String TAG = "ModifyUserInfoActivity";
    @BindView(R.id.iv_person_back)
    ImageView ivPersonBack;
    @BindView(R.id.ll_card_un_band)
    LinearLayout llCardUnBand;
    @BindView(R.id.iv_add_card)
    ImageView ivAddCard;
    @BindView(R.id.ll_card_band)
    LinearLayout llCardBand;
    @BindView(R.id.tv_change)
    TextView tvChange;
    @BindView(R.id.tv_card_bank_name)
    TextView tvCardBankName;
    @BindView(R.id.tv_card_number)
    TextView tvCardNumber;
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
    @BindView(R.id.re_person_modify_phone)
    RelativeLayout getRePersonModifyPhone;
    @BindView(R.id.tv_modify_phone)
    TextView tvModifyPhone;
    @BindView(R.id.re_modify_title)
    RelativeLayout reModifyTitle;
    private ModifyUserInfoPresenter presenter;

    private ArrayList<String> photoList = new ArrayList<>();

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

        ivModifyHeaderImg.setImageURI(UserData.getInstance().getPictureUrl());
        birthdayModify = UserData.getInstance().getBirthday();
        sexModify = UserData.getInstance().getSex();
        nickNameModify = UserData.getInstance().getNickname();
        headImgModify = UserData.getInstance().getPictureUrl();
    }

    @Override
    public void initPresenter() {
        presenter = new ModifyUserInfoPresenter(this, this);
    }

    @OnClick({R.id.iv_person_back, R.id.ll_card_un_band, R.id.ll_card_band, R.id.tv_change, R.id.re_person_modify_header_img, R.id.re_person_modify_name,
            R.id.re_person_modify_year, R.id.re_person_modify_sex, R.id.re_person_modify_phone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_person_back:
                finishCurrentActivity();
                break;
            case R.id.ll_card_un_band:
            case R.id.tv_change:
                startActivity(new Intent(this, WithCashBindCardActivity.class));
                break;
            case R.id.re_person_modify_header_img:
                final ModifyItemChooseDialog modifyHeaderDialog = new ModifyItemChooseDialog(this, getString(R.string.person_head_img_camera), getString(R.string.person_head_img_gallery), getString(R.string.cancel));
                modifyHeaderDialog.setOnItemClickListener(new ModifyItemChooseDialog.OnClickItemListener() {
                    @Override
                    public void onClickItem(int position) {
                        switch (position) {
                            case 1:
                                PhotoPicker.builder()
                                        .setPhotoCount(1)
                                        .setShowCamera(true)
                                        .setShowGif(true)
                                        .setPreviewEnabled(false)
                                        .start(ModifyUserInfoActivity.this, PhotoPicker.REQUEST_CODE);
                                break;
                            case 2:
                                PhotoPicker.builder()
                                        .setPhotoCount(1)
                                        .setShowCamera(false)
                                        .setShowGif(true)
                                        .setPreviewEnabled(false)
                                        .start(ModifyUserInfoActivity.this, PhotoPicker.REQUEST_CODE);
                                break;
                            default:
                                modifyHeaderDialog.dismiss();
                                break;
                        }
                    }
                });
                modifyHeaderDialog.show();
                break;
            case R.id.re_person_modify_name:
                EditTextDialog editNameDialog = new EditTextDialog(this, getString(R.string.person_change_nick_name), tvModifyName.getText().toString(), getString(R.string.finish));
                editNameDialog.setOnConfirmListener(new EditTextDialog.OnConfirmListener() {
                    @Override
                    public void onFinished(String name) {
                        nickNameModify = name;
                        tvModifyName.setText(name);
                        presenter.upgradePersonInfo(nickNameModify, birthdayModify, sexModify, UserData.getInstance().getPictureUrl());
                    }
                });
                editNameDialog.show();
                break;
            case R.id.re_person_modify_year:
                ChangeBirthdayDialog changeBirthdayDialog = new ChangeBirthdayDialog(this);
                changeBirthdayDialog.setDate(DataUtil.getResultByTimeStamp(UserData.getInstance().getBirthday(), DataUtil.Y),
                        DataUtil.getResultByTimeStamp(UserData.getInstance().getBirthday(), DataUtil.M),
                        DataUtil.getResultByTimeStamp(UserData.getInstance().getBirthday(), DataUtil.D));
                changeBirthdayDialog.setBirthdayListener(new ChangeBirthdayDialog.OnBirthListener() {
                    @Override
                    public void onClick(String year, String month, String day) {
                        showBirthDay(DataUtil.getTimeStampBuyTime(Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day)));
                    }
                });
                changeBirthdayDialog.show();
                break;
            case R.id.re_person_modify_sex:
                final ModifyItemChooseDialog modifySexDialog = new ModifyItemChooseDialog(this, getString(R.string.male), getString(R.string.female), getString(R.string.cancel));
                modifySexDialog.setOnItemClickListener(new ModifyItemChooseDialog.OnClickItemListener() {
                    @Override
                    public void onClickItem(int position) {
                        switch (position) {
                            case 1:
                                showSex(1);
                                break;
                            case 2:
                                showSex(2);
                                break;
                            default:
                                modifySexDialog.dismiss();
                                break;
                        }
                    }
                });
                modifySexDialog.show();
                break;
            case R.id.re_person_modify_phone:
                ChangePhoneDialog changePhoneDialog = new ChangePhoneDialog(this, ChangePhoneDialog.TYPE_VERIFY);
                changePhoneDialog.show();
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
                final String image = data.getStringExtra("image");
                showHeadImg(image);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        presenter.sendImgOkHttp(image);
                    }
                });
            }
        }
    }

    @Override
    public void showHeadImg(String url) {
        headImgModify = url;
        ivModifyHeaderImg.setImageURI("file://" + url);
    }

    @Override
    public void showBirthDay(long birthday) {
        birthdayModify = birthday;
        tvModifyYear.setText(DataUtil.getConvertResult(birthday, DataUtil.Y_M_D));
        presenter.upgradePersonInfo(nickNameModify, birthdayModify, sexModify, UserData.getInstance().getPictureUrl());
    }

    @Override
    public void showSex(int flag) {
        sexModify = flag;
        tvModifySex.setText(flag == 1 ? getString(R.string.male) : getString(R.string.female));
        presenter.upgradePersonInfo(nickNameModify, birthdayModify, sexModify, UserData.getInstance().getPictureUrl());
    }

    @Override
    public void showBankCardInfo(boolean isBand, BankCardBean bankCardBean) {
        if (isBand) {
            llCardUnBand.setVisibility(View.GONE);
            llCardBand.setVisibility(View.VISIBLE);
            tvCardBankName.setText(bankCardBean.bankName);
            tvCardNumber.setText(bankCardBean.cardNum);
        } else {
            llCardBand.setVisibility(View.GONE);
            llCardUnBand.setVisibility(View.VISIBLE);
        }
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

        tvModifyName.setText(UserData.getInstance().getNickname());
        tvModifyYear.setText(DataUtil.getConvertResult(UserData.getInstance().getBirthday(), DataUtil.Y_M_D));
        birthdayModify = UserData.getInstance().getBirthday();
        tvModifySex.setText(UserData.getInstance().getSex() == 1 ? getString(R.string.male) : getString(R.string.female));
        tvModifyPhone.setText(UserData.getInstance().getPhoneNum());
        if (UserData.getInstance().isBindCard()) {
            tvCardBankName.setText(UserData.getInstance().getBankName());
            tvCardNumber.setText(UserData.getInstance().getCardNum());
            llCardUnBand.setVisibility(View.GONE);
            llCardBand.setVisibility(View.VISIBLE);
        } else {
            llCardBand.setVisibility(View.GONE);
            llCardUnBand.setVisibility(View.VISIBLE);
        }
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
