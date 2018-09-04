package com.leyou.game.activity.friend;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.activity.mine.ClipImageActivity;
import com.leyou.game.activity.mine.ModifyUserInfoActivity;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.bean.Result;
import com.leyou.game.dao.Crowd;
import com.leyou.game.ipresenter.friend.ICreateCrowdInfo;
import com.leyou.game.presenter.friend.CreateCrowdInfoPresenter;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.KeyBoardUtils;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.util.newapi.CrowdApi;
import com.leyou.game.widget.clip.ClipImageLayout;
import com.leyou.game.widget.dialog.LoadingProgressDialog;
import com.leyou.game.widget.dialog.mine.ModifyItemChooseDialog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import rx.Observer;

/**
 * Description : 创建群聊前的图片和昵称编辑
 *
 * @author : rocky
 * @Create Time : 2017/12/6 下午1:50
 * @Modified By: rocky
 * @Modified Time : 2017/12/6 下午1:50
 */
public class CreateCrowdInfoActivity extends BaseActivity implements ICreateCrowdInfo {

    private static final String TAG = "CreateCrowdInfoActivity";
    @BindView(R.id.iv_order_pay_back)
    ImageView ivOrderPayBack;
    @BindView(R.id.iv_crowd_header)
    SimpleDraweeView ivCrowdHeader;
    @BindView(R.id.et_crowd_name)
    EditText etCrowdName;
    @BindView(R.id.et_crowd_introduce)
    EditText etCrowdIntroduce;
    @BindView(R.id.btn_create)
    Button btnCreate;
    private ArrayList<String> photoList = new ArrayList<>();
    private CreateCrowdInfoPresenter presenter;
    private String imageUrl;
    private String crowdName;
    private String crowdIntroduce;
    private int type;
    private Crowd crowdInfo;
    private LoadingProgressDialog progressDialog;

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_create_crowd_info;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        type = getIntent().getIntExtra("type", 1);
        if (type == 2) {
            crowdInfo = getIntent().getParcelableExtra("crowdInfo");
            ivCrowdHeader.setImageURI(crowdInfo.getHeadImgUrl());
            etCrowdIntroduce.setText(crowdInfo.getIntroduction());
            etCrowdName.setText(crowdInfo.getName());
            btnCreate.setText("修改群资料");
            crowdIntroduce = crowdInfo.introduction;
            crowdName = crowdInfo.name;
            imageUrl = crowdInfo.headImgUrl;
        }
        etCrowdName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String string = s.toString();
                crowdName = string;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etCrowdIntroduce.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String string = s.toString();
                crowdIntroduce = string;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void initPresenter() {
        presenter = new CreateCrowdInfoPresenter(this, this);
    }

    @OnClick({R.id.iv_order_pay_back, R.id.iv_crowd_header, R.id.et_crowd_name, R.id.et_crowd_introduce, R.id.btn_create})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_order_pay_back:
                finishCurrentActivity();
                break;
            case R.id.iv_crowd_header:
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
                                        .start(CreateCrowdInfoActivity.this, PhotoPicker.REQUEST_CODE);
                                break;
                            case 2:
                                PhotoPicker.builder()
                                        .setPhotoCount(1)
                                        .setShowCamera(false)
                                        .setShowGif(true)
                                        .setPreviewEnabled(false)
                                        .start(CreateCrowdInfoActivity.this, PhotoPicker.REQUEST_CODE);
                                break;
                            default:
                                modifyHeaderDialog.dismiss();
                                break;
                        }
                    }
                });
                modifyHeaderDialog.show();
                break;
            case R.id.et_crowd_name:
                etCrowdName.setFocusable(true);
                etCrowdName.setFocusableInTouchMode(true);
                break;
            case R.id.et_crowd_introduce:
                etCrowdIntroduce.setFocusable(true);
                etCrowdIntroduce.setFocusableInTouchMode(true);
                break;
            case R.id.btn_create:
                KeyBoardUtils.closeKeyboard(etCrowdIntroduce, this);
                etCrowdIntroduce.setFocusable(false);
                etCrowdName.setFocusable(false);
                if (!TextUtils.isEmpty(crowdName)) {
                    if (!TextUtils.isEmpty(crowdIntroduce)) {
                        if (!TextUtils.isEmpty(imageUrl)) {
                            Crowd crowd = new Crowd();
                            crowd.setHeadImgUrl(imageUrl);
                            crowd.setName(crowdName);
                            crowd.setIntroduction(crowdIntroduce);
                            if (type == 1) {
                                Intent intent = new Intent(this, CreateCrowdActivity.class);
                                intent.putExtra("crowdInfo", crowd);
                                startActivity(intent);
                                finishCurrentActivity();
                            } else if (type == 2) {
                                crowd.setGroupId(crowdInfo.getGroupId());
                                commitCrowdInfo(crowd);
                            }
                        } else {
                            showMessageToast("上传群头像");
                        }
                    } else {
                        showMessageToast("群简介不能为空");
                    }
                } else {
                    showMessageToast("群昵称不能为空");
                }
                break;
        }
    }

    private void commitCrowdInfo(Crowd crowd) {
        showLoading();
        changeLoadingDes("更新群资料中...");
        HttpUtil.subscribe(HttpUtil.createApi(CrowdApi.class, Constants.URL).updateCrowdInfo(crowd.getGroupId(),
                crowd.getHeadImgUrl(), crowd.getName(), crowd.getIntroduction()), new Observer<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                LogUtil.e(TAG, e.toString());
                dismissedLoading();
            }

            @Override
            public void onNext(Result result) {
                dismissedLoading();
                if (result.result == 1) {
                    showMessageToast("更新成功");
                    finishCurrentActivity();
                } else {
                    showMessageToast("更新失败，再试一次");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
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
                ivCrowdHeader.setImageURI("file://" + image);
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
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public void showLoading() {
        progressDialog = new LoadingProgressDialog(this, false);
        progressDialog.show();
    }

    @Override
    public void changeLoadingDes(String des) {
        if (null != progressDialog) {
            progressDialog.setLoadingText(des);
        }
    }

    @Override
    public void dismissedLoading() {
        if (null != progressDialog) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void showMessageToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showToastShort(msg);
            }
        });
    }
}
