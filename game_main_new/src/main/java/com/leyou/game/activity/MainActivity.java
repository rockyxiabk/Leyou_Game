package com.leyou.game.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.adapter.ViewPagerFragmentAdapter;
import com.leyou.game.base.BaseFragmentActivity;
import com.leyou.game.bean.PushBean;
import com.leyou.game.bean.UpdateAppBean;
import com.leyou.game.bean.UserData;
import com.leyou.game.bean.game.GameBean;
import com.leyou.game.event.MainTabEvent;
import com.leyou.game.fragment.FriendFragment_;
import com.leyou.game.fragment.GameFragment_;
import com.leyou.game.fragment.MineFragment_;
import com.leyou.game.fragment.TreasureFragment;
import com.leyou.game.fragment.WinAwardFragment;
import com.leyou.game.fragment.WinAwardFragment_;
import com.leyou.game.ipresenter.IMainActivity;
import com.leyou.game.presenter.MainActivityPresenter;
import com.leyou.game.util.DataCleanManager;
import com.leyou.game.util.DataUtil;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.MobClickUtil;
import com.leyou.game.util.NotificationsUtil;
import com.leyou.game.util.PayUtil;
import com.leyou.game.util.SPUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.widget.DragPointView;
import com.leyou.game.widget.IndexViewPager;
import com.leyou.game.widget.dialog.DiamondConvertFlowDialog;
import com.leyou.game.widget.dialog.NewVersionDialog;
import com.leyou.game.widget.dialog.NotifyAlertDialog;
import com.leyou.game.widget.dialog.PrizeDialog;
import com.leyou.game.widget.dialog.RecommendFriendDialog;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.model.Conversation;

/**
 * Description : 应用首页
 *
 * @author : rocky
 * @Create Time : 2017/3/29 下午4:21
 * @Modified By: rocky
 * @Modified Time : 2017/3/29 下午4:21
 */

public class MainActivity extends BaseFragmentActivity implements IMainActivity, IUnReadMessageObserver {

    private static final String TAG = "MainActivity";
    @BindView(R.id.viewPager_container)
    IndexViewPager viewPagerContainer;

    @BindView(R.id.re_bar_treasury)
    RelativeLayout reBarTreasury;
    @BindView(R.id.iv_bar_treasury)
    ImageView ivBarTreasury;
    @BindView(R.id.tv_bar_treasury)
    TextView tvBarTreasury;
    @BindView(R.id.tv_treasure_tips_number)
    DragPointView tvTreasureTipsNumber;
    @BindView(R.id.re_bar_friend)
    RelativeLayout reBarFriend;
    @BindView(R.id.iv_bar_friend)
    ImageView ivBarFriend;
    @BindView(R.id.tv_bar_friend)
    TextView tvBarFriend;
    @BindView(R.id.tv_friend_tips_number)
    DragPointView tvFriendTipsNumber;
    @BindView(R.id.re_bar_win)
    RelativeLayout reBarWin;
    @BindView(R.id.iv_bar_win)
    ImageView ivBarWin;
    @BindView(R.id.tv_bar_win)
    TextView tvBarWin;
    @BindView(R.id.re_bar_fight)
    RelativeLayout reBarFight;
    @BindView(R.id.iv_bar_fight)
    ImageView ivBarFight;
    @BindView(R.id.tv_bar_fight)
    TextView tvBarFight;
    @BindView(R.id.tv_fight_tips_number)
    DragPointView tvFightTipsNumber;
    @BindView(R.id.re_bar_mine)
    RelativeLayout reBarMine;
    @BindView(R.id.iv_bar_mine)
    ImageView ivBarMine;
    @BindView(R.id.tv_bar_mine)
    TextView tvBarMine;
    @BindView(R.id.tv_mine_tips_number)
    DragPointView tvMineTipsNumber;
    /**
     * floatButtonViwe
     */
    @BindView(R.id.gifImageView)
    SimpleDraweeView gitImageView;
    @BindView(R.id.re_floatView)
    RelativeLayout reFloatView;

    private List<Fragment> fragmentList = new ArrayList<>();
    private TreasureFragment treasureFragment;
    private FriendFragment_ friendFragment;
    private WinAwardFragment_ winAwardFragment;
    private WinAwardFragment winAwardFragment_;
    private GameFragment_ gameFragment;
    private MineFragment_ mineFragment;
    private ViewPagerFragmentAdapter viewPagerFragmentAdapter;
    private MainActivityPresenter presenter;
    private int index = 2;
    private boolean isShowPrizeDialog = false;
    private Handler handler = new Handler();
    private long exitTime;

    private int unReadMessageCount;
    private GameBean sliderBean = new GameBean();

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case Constants.PRIZE_ACTION:
                    PushBean pushInfo = intent.getParcelableExtra("pushBean");
                    String prizeId = intent.getStringExtra("winId");
                    if (null != pushInfo) {
                        showPrizeView(pushInfo, prizeId);
                    }
                    break;
                case Constants.NEW_VERSION_ACTION:
                    presenter.checkVersion();
                    break;
                case Constants.FRIEND_ACTION:
                    showFriendRecommend();
                    break;
                case Constants.DIAMOND_CONVERT:
                    showDiamondConvert();
                    break;
                case Constants.OFF_LINE_ACTION:
                    PushBean offLineInfo = intent.getParcelableExtra("pushBean");
                    offLineDialog(offLineInfo);
                    break;
            }
        }
    };
    private Conversation.ConversationType[] conversationTypes = new Conversation.ConversationType[]{
            Conversation.ConversationType.PRIVATE,
            Conversation.ConversationType.GROUP,
            Conversation.ConversationType.SYSTEM,
            Conversation.ConversationType.PUBLIC_SERVICE,
            Conversation.ConversationType.APP_PUBLIC_SERVICE
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setTabState(MainTabEvent tabEvent) {
        setTabState(tabEvent.event);
    }

    @Override
    public void initWindows() {
    }

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.PRIZE_ACTION);
        intentFilter.addAction(Constants.FRIEND_ACTION);
        intentFilter.addAction(Constants.NEW_VERSION_ACTION);
        intentFilter.addAction(Constants.DIAMOND_CONVERT);
        intentFilter.addAction(Constants.OFF_LINE_ACTION);
        registerReceiver(receiver, intentFilter);

        RongIM.getInstance().addUnReadMessageCountChangedObserver(this, conversationTypes);

        viewPagerContainer.setCanScroll(true);

        treasureFragment = new TreasureFragment();
        friendFragment = new FriendFragment_();
        winAwardFragment = new WinAwardFragment_();
        winAwardFragment_ = new WinAwardFragment();
        gameFragment = new GameFragment_();
        mineFragment = new MineFragment_();

        fragmentList.add(treasureFragment);
        fragmentList.add(friendFragment);
        fragmentList.add(winAwardFragment_);
        fragmentList.add(gameFragment);
        fragmentList.add(mineFragment);

        viewPagerContainer.setOffscreenPageLimit(5);
        viewPagerFragmentAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), fragmentList);
        viewPagerContainer.setAdapter(viewPagerFragmentAdapter);
        viewPagerContainer.setCurrentItem(2);
        viewPagerContainer.setCanScroll(false);
        setTabState(2);

        showDragView();
    }

    @Override
    public void initPresenter() {
        presenter = new MainActivityPresenter(this, this);
        presenter.checkVersion();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        if (UserData.getInstance().isNewUser() && UserData.getInstance().isLogIn()) {
            boolean isGuide = SPUtil.getBoolean(this, SPUtil.INDUCE, "isGuide");
            if (!isGuide) {
                startActivity(new Intent(this, WebViewGuideActivity.class));
            }
        }
        unReadMessageCount = 0;
        checkDialogPermission();
    }

    private void checkDialogPermission() {
        if (!NotificationsUtil.isNotificationEnabled(this)) {
            long notify_time = SPUtil.getLong(this, SPUtil.SETTING, "notify_time");
            int distanceDay = DataUtil.distanceDay(notify_time, System.currentTimeMillis());
            if (distanceDay >= 1) {
                new NotifyAlertDialog(this).show();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                exitTime = System.currentTimeMillis();
                ToastUtils.showToastShort("再按一次退出熊猫玩玩");
                return false;
            } else {
                finishCurrentActivity();
            }
        }
        return false;
    }

    @OnClick({R.id.re_bar_treasury, R.id.re_bar_friend, R.id.re_bar_win, R.id.re_bar_fight, R.id.re_bar_mine, R.id.gifImageView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.re_bar_treasury:
                setTabState(0);
                break;
            case R.id.re_bar_friend:
                setTabState(1);
                break;
            case R.id.re_bar_win:
                setTabState(2);
                break;
            case R.id.re_bar_fight:
                setTabState(3);
                break;
            case R.id.re_bar_mine:
                setTabState(4);
                break;
            case R.id.gifImageView:
                if (clickormove) {
                    MobClickUtil.mobEvent(this, MobClickUtil.EVENT_SLIDE_GAME);
                    Intent intent = new Intent(MainActivity.this, PlayGameActivity.class);
                    intent.putExtra("game", sliderBean);
                    startActivity(intent);
                }
                break;
        }
    }

    private void setTabState(int position) {
        index = position;
        ivBarTreasury.setSelected(false);
        ivBarFriend.setSelected(false);
        ivBarWin.setSelected(false);
        ivBarFight.setSelected(false);
        ivBarMine.setSelected(false);
        tvBarTreasury.setSelected(false);
        tvBarFriend.setSelected(false);
        tvBarWin.setSelected(false);
        tvBarFight.setSelected(false);
        tvBarMine.setSelected(false);

        switch (position) {
            case 0:
                ivBarTreasury.setSelected(true);
                tvBarTreasury.setSelected(true);
                break;
            case 1:
                ivBarFriend.setSelected(true);
                tvBarFriend.setSelected(true);
                break;
            case 2:
                ivBarWin.setSelected(true);
                tvBarWin.setSelected(true);
                break;
            case 3:
                ivBarFight.setSelected(true);
                tvBarFight.setSelected(true);
                break;
            case 4:
                ivBarMine.setSelected(true);
                tvBarMine.setSelected(true);
                break;
        }
        viewPagerContainer.setCurrentItem(position);
    }

    @Override
    public void showDiamondConvert() {
        new DiamondConvertFlowDialog(this).show();
    }

    @Override
    public void showFriendRecommend() {
        new RecommendFriendDialog(this).show();
    }

    @Override
    public void showPrizeView(PushBean pushBean, String prizeId) {
        if (!isShowPrizeDialog) {
            PrizeDialog prizeDialog = new PrizeDialog(this, pushBean, prizeId);
            prizeDialog.show();
        }
    }

    @Override
    public void showNewVersionView(UpdateAppBean updateAppBean) {
        NewVersionDialog newVersionDialog = new NewVersionDialog(this, updateAppBean);
        newVersionDialog.show();
    }

    @Override
    public void offLineDialog(PushBean offLineInfo) {
        new AlertDialog.Builder(this).setTitle("下线通知")
                .setMessage(offLineInfo.des)
                .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishCurrentActivity();
                    }
                })
                .setPositiveButton("重新登陆", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                        finishCurrentActivity();
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }

    @Override
    public void clearDownloadFile(boolean flag) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                DataCleanManager.deleteFile(new File(Constants.UPDATE_DIR));
            }
        });
    }

    @Override
    public void showWinDiamondGame(GameBean sliderBean) {
        this.sliderBean = sliderBean;
        if (null != sliderBean) {
            reFloatView.setVisibility(View.VISIBLE);
            gitImageView.setImageURI(sliderBean.iconUrl);
        } else {
            reFloatView.setVisibility(View.GONE);
        }
    }

    @Override
    public void setMessageCount(int count) {
        count = unReadMessageCount + count;
        if (count == 0) {
            tvFriendTipsNumber.setVisibility(View.GONE);
        } else if (count > 0 && count < 100) {
            tvFriendTipsNumber.setVisibility(View.VISIBLE);
            tvFriendTipsNumber.setText(String.valueOf(count));
            unReadMessageCount = count;
        } else {
            tvFriendTipsNumber.setVisibility(View.VISIBLE);
            tvFriendTipsNumber.setText(getString(R.string.no_read_message));
        }
    }

    @Override
    public void onCountChanged(int count) {
        tvFriendTipsNumber.setOnTouchListener(null);
        tvFriendTipsNumber.setEnabled(false);
        if (count == 0) {
            tvFriendTipsNumber.setVisibility(View.GONE);
            unReadMessageCount = 0;
        } else if (count > 0 && count < 100) {
            tvFriendTipsNumber.setVisibility(View.VISIBLE);
            tvFriendTipsNumber.setText(String.valueOf(count));
            unReadMessageCount = count;
        } else {
            tvFriendTipsNumber.setVisibility(View.VISIBLE);
            tvFriendTipsNumber.setText(getString(R.string.no_read_message));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PayUtil.destroy(this);
        unregisterReceiver(receiver);
        EventBus.getDefault().unregister(this);
    }

    private View content;
    private int screenWidth;
    private int screenHeight;
    private boolean clickormove = true;//点击或拖动，点击为true，拖动为false
    private int downX, downY;//按下时的X，Y坐标
    private boolean hasMeasured = false;//ViewTree是否已被测量过，是为true，否为false

    private void showDragView() {
        content = getWindow().findViewById(Window.ID_ANDROID_CONTENT);//获取界面的ViewTree根节点View

        DisplayMetrics dm = getResources().getDisplayMetrics();//获取显示屏属性
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;

        ViewTreeObserver vto = content.getViewTreeObserver();
        //获取ViewTree的监听器
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (!hasMeasured) {
                    screenHeight = content.getMeasuredHeight();//获取ViewTree的高度
                    hasMeasured = true;//设置为true，使其不再被测量。
                }
                return true;//如果返回false，界面将为空。
            }
        });
        gitImageView.setOnTouchListener(new View.OnTouchListener() {//设置按钮被触摸的时间
            int lastX, lastY; // 记录移动的最后的位置

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        downX = lastX;
                        downY = lastY;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 移动中动态设置位置
                        int dx = (int) event.getRawX() - lastX;//位移量X
                        int dy = (int) event.getRawY() - lastY;//位移量Y
                        int left = v.getLeft() + dx;
                        int top = v.getTop() + dy;
                        int right = v.getRight() + dx;
                        int bottom = v.getBottom() + dy;
                        //限定按钮被拖动的范围
                        if (left < 0) {
                            left = 0;
                            right = left + v.getWidth();
                        }
                        if (right > screenWidth) {
                            right = screenWidth;
                            left = right - v.getWidth();
                        }
                        if (top < 0) {
                            top = 0;
                            bottom = top + v.getHeight();
                        }
                        if (bottom > screenHeight) {
                            bottom = screenHeight;
                            top = bottom - v.getHeight();
                        }
                        //--限定按钮被拖动的范围
                        LogUtil.d(TAG, "--left:" + left + "---top:" + top + "---right:" + right + "---bottom:" + bottom);
                        v.layout(left, top, right, bottom);//按钮重画
                        // 记录当前的位置
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        //判断是单击事件或是拖动事件，位移量大于5则断定为拖动事件
                        if (Math.abs((int) (event.getRawX() - downX)) > 5 || Math.abs((int) (event.getRawY() - downY)) > 5) {
                            clickormove = false;
                        } else {
                            clickormove = true;
                        }
                        int dx1 = (int) event.getRawX() - lastX;//位移量X
                        int dy1 = (int) event.getRawY() - lastY;//位移量Y
                        int top1 = v.getTop() + dy1;
                        int bottom1 = v.getBottom() + dy1;
                        v.layout(screenWidth - v.getWidth(), top1, screenWidth, bottom1);//按钮重画
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        break;
                }
                return false;
            }

        });
    }
}
