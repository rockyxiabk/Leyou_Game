package com.leyou.game.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.leyou.game.R;
import com.leyou.game.adapter.game.GameCommentAdapter;
import com.leyou.game.adapter.game.GameScreenShotAdapter;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.bean.game.GameBean;
import com.leyou.game.bean.game.GameCommentBean;
import com.leyou.game.ipresenter.IGameDetailActivity;
import com.leyou.game.presenter.GameDetailPresenter;
import com.leyou.game.util.AnimatorUtils;
import com.leyou.game.util.KeyBoardUtils;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.ScreenUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.widget.fluid.FluidLayout;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 游戏详情页面
 *
 * @author : rocky
 * @Create Time : 2017/10/22 下午4:50
 * @Modified By: rocky
 * @Modified Time : 2017/10/22 下午4:50
 */
public class GameDetailActivity_ extends BaseActivity implements IGameDetailActivity {
    private static final String TAG = "GameDetailActivity_";

    @BindView(R.id.tv_game_detail_title)
    TextView tvGameDetailTitle;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.re_game_detail_toolBar)
    RelativeLayout reGameDetailToolBar;
    @BindView(R.id.nested_scroll_view)
    NestedScrollView nestedScrollView;
    @BindView(R.id.iv_game_icon)
    SimpleDraweeView ivGameIcon;
    @BindView(R.id.tv_game_name)
    TextView tvGameName;
    @BindView(R.id.tv_game_slogan)
    TextView tvGameSlogan;
    @BindView(R.id.fluid_game_attribute)
    FluidLayout fluidGameAttribute;
    @BindView(R.id.tv_game_description_content)
    TextView tvGameDescriptionContent;
    @BindView(R.id.tv_game_show_all)
    TextView tvGameShowAll;
    @BindView(R.id.recycler_screen_shot)
    RecyclerView recyclerScreenShot;
    @BindView(R.id.tv_game_comment)
    TextView tvGameComment;
    @BindView(R.id.tv_game_comment_all)
    TextView tvGameCommentAll;
    @BindView(R.id.recycler_comment)
    RecyclerView recyclerComment;
    @BindView(R.id.btn_start_game)
    Button btnStartGame;
    @BindView(R.id.iv_share)
    ImageView ivShare;
    @BindView(R.id.et_input_comment)
    EditText etInputComment;
    @BindView(R.id.iv_comment)
    ImageView ivComment;
    @BindView(R.id.ll_function)
    LinearLayout llFunction;
    private boolean state = true;//默认显示开始游戏true，false显示功能区
    private int currentState = 1;
    private GameScreenShotAdapter screenShotAdapter;
    private List<String> screenShotList = new ArrayList<>();

    private GameCommentAdapter commentAdapter;
    private List<GameCommentBean> commentList = new ArrayList<>();

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            LogUtil.d("plat", "platform" + platform);
            Toast.makeText(GameDetailActivity_.this, " 分享成功 ", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(GameDetailActivity_.this, " 分享失败 ", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(GameDetailActivity_.this, " 分享取消 ", Toast.LENGTH_SHORT).show();
        }
    };
    private GameDetailPresenter presenter;
    private GameBean gameBean;

    @Override
    public void initWindows() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_game_detail_;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        gameBean = getIntent().getParcelableExtra("game");

        screenShotAdapter = new GameScreenShotAdapter(this, screenShotList);
        recyclerScreenShot.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerScreenShot.setAdapter(screenShotAdapter);

        commentAdapter = new GameCommentAdapter(this, commentList);
        recyclerComment.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerComment.setAdapter(commentAdapter);
        recyclerComment.setNestedScrollingEnabled(false);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                LogUtil.d(TAG, "---x:" + scrollX + "---y:" + scrollY + "----ox:" + oldScrollX + "---oy:" + oldScrollY);
                LogUtil.d(TAG, "---state:" + state + "-----currentState:" + currentState);
            }
        });
    }

    @Override
    public void initPresenter() {
        presenter = new GameDetailPresenter(this, this);
        presenter.getGameDetail(gameBean);
        presenter.getGameCommentList(gameBean);
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

    @OnClick({R.id.iv_close, R.id.tv_game_show_all, R.id.tv_game_comment_all, R.id.btn_start_game, R.id.iv_share, R.id.iv_comment})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                KeyBoardUtils.closeKeyboard(etInputComment, this);
                finishCurrentActivity();
                break;
            case R.id.tv_game_show_all:
                if (tvGameShowAll.getText().toString().equals("显示全部")) {
                    tvGameDescriptionContent.setMaxLines(Integer.MAX_VALUE);
                    tvGameShowAll.setText("收起全部");
                } else if (tvGameShowAll.getText().toString().equals("收起全部")) {
                    tvGameDescriptionContent.setLines(3);
                    tvGameShowAll.setText("显示全部");
                }
                break;
            case R.id.tv_game_comment_all:
                break;
            case R.id.btn_start_game:
                Intent intent = new Intent(this, PlayGameActivity.class);
                intent.putExtra("game", gameBean);
                this.startActivity(intent);
                break;
            case R.id.iv_share:
                startShare("http://www.baidu.com");
                break;
            case R.id.iv_comment:
                KeyBoardUtils.closeKeyboard(etInputComment, this);
                break;
        }
    }

    private void changeBottomView() {
        if (!state) {
            if (currentState == 1) {
                currentState = -1;
                tvGameDetailTitle.setText(getString(R.string.app_name));
                AnimatorUtils.translationYTop(btnStartGame);
                AnimatorUtils.translationYTop(llFunction);
            }
        } else {
            if (currentState == -1) {
                currentState = 1;
                tvGameDetailTitle.setText(getString(R.string.game_detail));
                AnimatorUtils.translationYBottom(btnStartGame);
                AnimatorUtils.translationYBottom(llFunction);
            }
        }
    }

    private void startShare(final String url) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                UMImage umImage = new UMImage(GameDetailActivity_.this, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
                UMWeb umWeb = new UMWeb(url);
                umWeb.setThumb(umImage);
                umWeb.setDescription("游戏着，快乐着");
                umWeb.setTitle(getString(R.string.app_name));
                new ShareAction(GameDetailActivity_.this)
                        .withText(getString(R.string.app_name))
                        .withMedia(umWeb)
                        .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                        .setCallback(umShareListener)
                        .open();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showGameDetail(GameBean gameBean) {
        ivGameIcon.setImageURI(gameBean.iconUrl);
        tvGameName.setText(gameBean.name);
        tvGameSlogan.setText(gameBean.propaganda);
        tvGameDescriptionContent.setText(gameBean.readme);
//        showCheckAll(tvGameDescriptionContent);
        String property = gameBean.property;
        if (!TextUtils.isEmpty(property)) {
            try {
                String[] properties = property.split(",");
                setProperty(properties);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        screenShotAdapter.setAdapterData(gameBean.imgList);
    }

    private void setProperty(String[] properties) {
        for (int i = 0; i < properties.length; i++) {
            final TextView tv = new TextView(this);
            tv.setPadding(ScreenUtil.dp2px(this, 6), ScreenUtil.dp2px(this, 3), ScreenUtil.dp2px(this, 6), ScreenUtil.dp2px(this, 3));
            tv.setText(properties[i]);
            tv.setBackgroundResource(R.drawable.checkbox_btn_bg_selector);
            tv.setTextSize(10);
            tv.setTextColor(getResources().getColor(R.color.text_color_selector));
            FluidLayout.LayoutParams params = new FluidLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 12, 12, 12);
            fluidGameAttribute.addView(tv, params);
        }
    }

    private void showCheckAll(final TextView tv_content) {
        Layout l = tv_content.getLayout();
        if (l != null) {
            int lines = l.getLineCount();
            if (lines > 0) {
                if (l.getEllipsisCount(lines) > 0) {
                    tvGameShowAll.setClickable(true);
                } else {
                    tvGameShowAll.setClickable(false);
                }
            }
        }
    }

    @Override
    public void showCommentListData(List<GameCommentBean> list) {
        if (null != list && list.size() > 0) {
            commentAdapter.setAdapterData(list);
        }
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
}
