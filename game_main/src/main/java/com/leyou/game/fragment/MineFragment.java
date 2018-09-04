package com.leyou.game.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.leyou.game.R;
import com.leyou.game.activity.RegisterActivity;
import com.leyou.game.activity.mine.AwardListActivity;
import com.leyou.game.activity.mine.ConsumeNoteActivity;
import com.leyou.game.activity.mine.ExchangeActivity;
import com.leyou.game.activity.mine.LogInWebActivity;
import com.leyou.game.activity.mine.ModifyUserInfoActivity;
import com.leyou.game.activity.mine.SettingActivity;
import com.leyou.game.activity.mine.SignDiamondActivity;
import com.leyou.game.activity.mine.TradeNoteActivity;
import com.leyou.game.activity.mine.WinResultActivity;
import com.leyou.game.activity.mine.WithCashApplyActivity;
import com.leyou.game.activity.mine.WithCashBindCardActivity;
import com.leyou.game.base.BaseFragment;
import com.leyou.game.bean.UserData;
import com.leyou.game.event.RefreshEvent;
import com.leyou.game.event.SaleEvent;
import com.leyou.game.ipresenter.mine.IMineFragment;
import com.leyou.game.presenter.mine.MineFragmentPresenter;
import com.leyou.game.util.DataUtil;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.SPUtil;
import com.leyou.game.widget.dialog.BuyDiamondDialog;
import com.leyou.game.widget.zoomview.PullToZoomScrollViewEx;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Description : 我的个人页面
 *
 * @author : rocky
 * @Create Time : 2017/4/5 下午3:10
 * @Modified By: rocky
 * @Modified Time : 2017/4/5 下午3:10
 */
public class MineFragment extends BaseFragment implements IMineFragment {


    private static final String TAG = "MineFragment";
    @BindView(R.id.scroll_view)
    PullToZoomScrollViewEx scrollView;
    TextView tvUserName;
    ImageView ivUserModify;
    ImageView ivUserSetting;
    SimpleDraweeView ivUserHead;
    TextView tvUserDiamond;
    TextView tvUserMoney;
    ImageView ivHeaderBg;
    LinearLayout llMineListChongzhi;
    LinearLayout llMineListExchange;
    LinearLayout llMineListSignDiamond;
    ImageView ivSignDiamond;
    TextView tvPrizeState;
    LinearLayout llMineListAwardNotes;
    LinearLayout llMineListconsume;
    LinearLayout llMineListWinResult;
    LinearLayout llMineListApplyDeveloper;
    TextView tvApplyState;
    LinearLayout llMineListUpload;
    LinearLayout llMineListWithCash;
    private Unbinder unbinder;//root布局文件
    private MineFragmentPresenter presenter;
    private boolean isLogIn;
    private int state;
    private boolean isBindCard;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshUserData(RefreshEvent event) {
        if (event.getIsRefresh() == RefreshEvent.REFRESH) {
            if (event.getSourceType() == RefreshEvent.MINE) {
                presenter.getUserDate();
                presenter.getUserWealth();
                presenter.getDeveloperState();
                presenter.getBankInfo();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sellDiamond(SaleEvent event) {
        if (event.getType() == 1) {
            presenter.getUserWealth();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && null != presenter) {
            presenter.getUserDate();
            presenter.getUserWealth();
            presenter.getDeveloperState();
            presenter.getBankInfo();
            setSignDiamondImage();
        }
    }

    public MineFragment() {
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, view);
        View headView = LayoutInflater.from(context).inflate(R.layout.profile_head_view, null, false);
        tvUserName = ButterKnife.findById(headView, R.id.tv_user_name);
        tvUserName.setOnClickListener(this);
        ivUserModify = ButterKnife.findById(headView, R.id.iv_user_modify);
        ivUserModify.setOnClickListener(this);
        ivUserHead = ButterKnife.findById(headView, R.id.iv_user_head);
        ivUserHead.setOnClickListener(this);
        ivUserSetting = ButterKnife.findById(headView, R.id.iv_user_setting);
        ivUserSetting.setOnClickListener(this);
        tvUserDiamond = ButterKnife.findById(headView, R.id.tv_user_diamond);
        tvUserMoney = ButterKnife.findById(headView, R.id.tv_user_money);
        View zoomView = LayoutInflater.from(context).inflate(R.layout.profile_zoom_view, null, false);
        ivHeaderBg = ButterKnife.findById(zoomView, R.id.iv_header_bg);
        View contentView = LayoutInflater.from(context).inflate(R.layout.profile_content_view, null, false);
        llMineListChongzhi = ButterKnife.findById(contentView, R.id.ll_mine_list_chongzhi);
        llMineListChongzhi.setOnClickListener(this);
        llMineListExchange = ButterKnife.findById(contentView, R.id.ll_mine_list_exchange);
        llMineListExchange.setOnClickListener(this);
        llMineListSignDiamond = ButterKnife.findById(contentView, R.id.ll_mine_list_sign_diamond);
        llMineListSignDiamond.setOnClickListener(this);
        llMineListconsume = ButterKnife.findById(contentView, R.id.ll_mine_list_consume);
        llMineListconsume.setOnClickListener(this);
        ivSignDiamond = ButterKnife.findById(contentView, R.id.iv_sign_diamond);
        llMineListAwardNotes = ButterKnife.findById(contentView, R.id.ll_mine_list_win_award);
        llMineListAwardNotes.setOnClickListener(this);
        tvPrizeState = ButterKnife.findById(contentView, R.id.tv_prize_state);
        llMineListWinResult = ButterKnife.findById(contentView, R.id.ll_mine_list_win_result);
        llMineListWinResult.setOnClickListener(this);
        llMineListApplyDeveloper = ButterKnife.findById(contentView, R.id.ll_mine_list_apply_developer);
        llMineListApplyDeveloper.setOnClickListener(this);
        tvApplyState = ButterKnife.findById(contentView, R.id.tv_apply_developer_state);
        llMineListUpload = ButterKnife.findById(contentView, R.id.ll_mine_list_upload);
        llMineListUpload.setOnClickListener(this);
        llMineListWithCash = ButterKnife.findById(contentView, R.id.ll_mine_list_with_cash);
        llMineListWithCash.setOnClickListener(this);

        scrollView.setHeaderView(headView);
        scrollView.setZoomView(zoomView);
        scrollView.setScrollContentView(contentView);
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenWidth = localDisplayMetrics.widthPixels;
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth, (int) (9.0F * (mScreenWidth / 16.0F)));
        scrollView.setHeaderLayoutParams(localObject);
        // TODO: 2017/8/16 是否添加高斯模糊效果
//        Glide.with(context).load(UserData.getInstance().getPictureUrl()).error(R.mipmap.icon_header_bg).bitmapTransform(new BlurTransformation(context, 100)).into(ivHeaderBg);
    }

    @Override
    protected void initPresenter() {
        presenter = new MineFragmentPresenter(context, this);
        isLogIn = UserData.getInstance().isLogIn();
        setSignDiamondImage();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_user_name:
            case R.id.iv_user_modify:
            case R.id.iv_user_head:
                startOtherActivity(ModifyUserInfoActivity.class, RegisterActivity.class);
                break;
            case R.id.iv_user_setting:
                startOtherActivity(SettingActivity.class, SettingActivity.class);
                break;
            case R.id.ll_mine_list_chongzhi:
                if (isLogIn) {
                    BuyDiamondDialog buyDiamondDialog = new BuyDiamondDialog(getContext(), 0);
                    buyDiamondDialog.show();
                } else {
                    startOtherActivity(null, RegisterActivity.class);
                }
                break;
            case R.id.ll_mine_list_exchange:
                startOtherActivity(ExchangeActivity.class, RegisterActivity.class);
                break;
            case R.id.ll_mine_list_sign_diamond:
                startOtherActivity(SignDiamondActivity.class, RegisterActivity.class);
                ivSignDiamond.setImageResource(R.mipmap.icon_diamond);
                break;
            case R.id.ll_mine_list_trade_notes:
                startOtherActivity(TradeNoteActivity.class, RegisterActivity.class);
                break;
            case R.id.ll_mine_list_consume:
                startOtherActivity(ConsumeNoteActivity.class, RegisterActivity.class);
                break;
            case R.id.ll_mine_list_win_award:
                startOtherActivity(AwardListActivity.class, RegisterActivity.class);
                break;
            case R.id.ll_mine_list_win_result:
                Intent intent_result = new Intent(getContext(), WinResultActivity.class);
                context.startActivity(intent_result);
                break;
            case R.id.ll_mine_list_apply_developer:
            case R.id.ll_mine_list_upload:
                if (isLogIn) {
                    Intent intent = new Intent(context, LogInWebActivity.class);
                    intent.putExtra("state", state);
                    context.startActivity(intent);
                } else {
                    startOtherActivity(null, RegisterActivity.class);
                }
                break;
            case R.id.ll_mine_list_with_cash:
                if (isLogIn) {
                    if (isBindCard) {
                        startOtherActivity(WithCashApplyActivity.class, RegisterActivity.class);
                    } else {
                        startOtherActivity(WithCashBindCardActivity.class, RegisterActivity.class);
                    }
                } else {
                    startOtherActivity(null, RegisterActivity.class);
                }
                break;
        }
    }

    private void setSignDiamondImage() {
        String lastStampTime = SPUtil.getString(context, SPUtil.SETTING, "lastStampTime");
        if (!TextUtils.isEmpty(lastStampTime)) {
            Long aLong = Long.valueOf(lastStampTime);
            String lastTime = DataUtil.getConvertResult(aLong, DataUtil.YMD);
            String currentTime = DataUtil.getConvertResult(System.currentTimeMillis(), DataUtil.YMD);
            if (currentTime.equalsIgnoreCase(lastTime)) {
                ivSignDiamond.setImageResource(R.mipmap.icon_diamond);
            } else {
                ivSignDiamond.setImageResource(R.mipmap.icon_diamond_unvisited);
            }
        } else {
            ivSignDiamond.setImageResource(R.mipmap.icon_diamond_unvisited);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getName());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.unSubscribe();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void showUserData(String name, String pictureUrl, String backgroundImage) {
        ivUserHead.setImageURI(pictureUrl);
        tvUserName.setText(name);
    }

    @Override
    public void showWealth(int diamondCount, double money) {
        tvUserDiamond.setText(diamondCount + "钻石");
        tvUserMoney.setText(money + "元");
    }

    @Override
    public void isDeveloper(boolean flag, int state) {
        this.state = state;
        if (flag) {
            llMineListApplyDeveloper.setVisibility(View.VISIBLE);
            llMineListUpload.setVisibility(View.GONE);
            switch (state) {
                case 0:
                    tvApplyState.setText(context.getString(R.string.mine_apply_developer));
                    break;
                case 1:
                    tvApplyState.setText(context.getString(R.string.mine_apply_developer_committed));
                    break;
                case 2:
                    tvApplyState.setText(context.getString(R.string.mine_apply_developer_checking));
                    break;
                case 3:
                    tvApplyState.setText(context.getString(R.string.mine_apply_developer_check_failed));
                    break;
                case 4:
                    tvApplyState.setText(context.getString(R.string.mine_apply_developer_check_successes));
                    break;
            }
        } else {
            llMineListApplyDeveloper.setVisibility(View.VISIBLE);
            llMineListUpload.setVisibility(View.GONE);
        }
    }

    @Override
    public void isLogIn(boolean flag) {
        isLogIn = flag;
        if (flag) {
            llMineListAwardNotes.setVisibility(View.VISIBLE);
        } else {
            llMineListAwardNotes.setVisibility(View.GONE);
        }
    }

    @Override
    public void isBindCard(boolean flag) {
        isBindCard = flag;
    }

    @Override
    public void setPrizeDynamic(boolean result) {
        if (result) {
            tvPrizeState.setVisibility(View.VISIBLE);
        } else {
            tvPrizeState.setVisibility(View.GONE);
        }
    }

    @Override
    public void startOtherActivity(Class<?> cls, Class<?> unLonIn) {
        if (isLogIn) {
            Intent intent_user = new Intent(getContext(), cls);
            context.startActivity(intent_user);
        } else {
            Intent register = new Intent(getContext(), unLonIn);
            context.startActivity(register);
        }
    }

//    private void showNewPersonGuide() {
//
//        // 使用图片
//        final ImageView iv = new ImageView(context);
//        iv.setImageResource(R.mipmap.icon_arrow_diamond);
//        iv.setScaleType(ImageView.ScaleType.FIT_XY);
//
//        guideViewDiamond = GuideView.Builder
//                .newInstance(context)
//                .setTargetView(tvUserDiamond)//设置目标
//                .setCustomGuideView(iv)
//                .setDirction(GuideView.Direction.BOTTOM)
//                .setOnclickExit(true)
//                .setShape(GuideView.MyShape.ELLIPSE)   // 设置圆形显示区域，
//                .setBgColor(getResources().getColor(R.color.transparent))
//                .setOnclickListener(new GuideView.OnClickCallback() {
//                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//                    @Override
//                    public void onClickedGuideView() {
//                        guideViewDiamond.hide();
//                        iv.setImageResource(R.mipmap.icon_arrow_exchange);
//                        guideViewExChange.show();
//                    }
//                })
//                .build();
//        boolean isDiamondSign = SPUtil.getBoolean(context, SPUtil.INDUCE, "sign");
//        boolean isNewPerson = SPUtil.getBoolean(context, SPUtil.INDUCE, "isNewPerson");
//        if (!isDiamondSign && isNewPerson) {
//            SPUtil.setBoolean(context, SPUtil.INDUCE, "sign", true);
//            showGuideSign();
//        } else {
//            boolean isDiamond = SPUtil.getBoolean(context, SPUtil.INDUCE, "diamond");
//            if (!isDiamond && isNewPerson) {
//                SPUtil.setBoolean(context, SPUtil.INDUCE, "diamond", true);
//                guideViewDiamond.show();
//            } else {
//                showCashGuide();
//            }
//        }
//
//
//        guideViewExChange = GuideView.Builder.newInstance(context)
//                .setTargetView(llMineListExchange)
//                .setCustomGuideView(iv)
//                .setDirction(GuideView.Direction.BOTTOM)
//                .setShape(GuideView.MyShape.ELLIPSE)
//                .setBgColor(getResources().getColor(R.color.transparent))
//                .setOnclickExit(true)
//                .setOnclickListener(new GuideView.OnClickCallback() {
//                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//                    @Override
//                    public void onClickedGuideView() {
//                        guideViewExChange.hide();
//                        startOtherActivity(ExchangeActivity.class, RegisterActivity.class);
//                    }
//                })
//                .build();
//    }
//
//    private void showCashGuide() {
//        // 使用图片
//        final ImageView iv = new ImageView(context);
//        iv.setImageResource(R.mipmap.icon_arrow_money);
//        iv.setScaleType(ImageView.ScaleType.FIT_XY);
//
//        guideViewMoney = GuideView.Builder
//                .newInstance(context)
//                .setTargetView(tvUserMoney)//设置目标
//                .setCustomGuideView(iv)
//                .setDirction(GuideView.Direction.BOTTOM)
//                .setShape(GuideView.MyShape.ELLIPSE)
//                .setOnclickExit(true)
//                .setBgColor(getResources().getColor(R.color.transparent))
//                .setOnclickListener(new GuideView.OnClickCallback() {
//                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//                    @Override
//                    public void onClickedGuideView() {
//                        guideViewMoney.hide();
//                        iv.setImageResource(R.mipmap.icon_arrow_cash);
//                        guideViewCash.show();
//                    }
//                })
//                .build();
//        boolean isDiamond = SPUtil.getBoolean(context, SPUtil.INDUCE, "cash");
//        boolean isNewPerson = SPUtil.getBoolean(context, SPUtil.INDUCE, "isNewPerson");
//        if (!isDiamond && isNewPerson) {
//            SPUtil.setBoolean(context, SPUtil.INDUCE, "cash", true);
//            guideViewMoney.show();
//        }
//
//        guideViewCash = GuideView.Builder.newInstance(context)
//                .setTargetView(llMineListWithCash)
//                .setCustomGuideView(iv)
//                .setDirction(GuideView.Direction.TOP)
//                .setShape(GuideView.MyShape.RECTANGULAR)
//                .setOnclickExit(true)
//                .setBgColor(getResources().getColor(R.color.transparent))
//                .setOnclickListener(new GuideView.OnClickCallback() {
//                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//                    @Override
//                    public void onClickedGuideView() {
//                        guideViewCash.hide();
//                        startOtherActivity(WithCashBindCardActivity.class, RegisterActivity.class);
//                    }
//                })
//                .build();
//    }
//
//    private void showGuideSign() {
//        // 使用图片
//        final ImageView iv = new ImageView(context);
//        iv.setImageResource(R.mipmap.icon_arrow_sign);
//        iv.setScaleType(ImageView.ScaleType.FIT_XY);
//
//        guideViewSign = GuideView.Builder
//                .newInstance(context)
//                .setTargetView(llMineListSignDiamond)//设置目标
//                .setCustomGuideView(iv)
//                .setDirction(GuideView.Direction.LEFT_BOTTOM)
//                .setShape(GuideView.MyShape.ELLIPSE)
//                .setBgColor(getResources().getColor(R.color.transparent))
//                .setOnclickExit(true)
//                .setOnclickListener(new GuideView.OnClickCallback() {
//                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//                    @Override
//                    public void onClickedGuideView() {
//                        guideViewSign.hide();
//                        startOtherActivity(SignDiamondActivity.class, RegisterActivity.class);
//                    }
//                })
//                .build();
//        guideViewSign.show();
//    }
}
