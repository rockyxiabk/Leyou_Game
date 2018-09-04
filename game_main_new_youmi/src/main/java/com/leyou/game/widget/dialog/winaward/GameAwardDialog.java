package com.leyou.game.widget.dialog.winaward;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.adapter.win.GameAwardAdapter;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.bean.game.GameBean;
import com.leyou.game.bean.win.WinGameAwardBean;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.newapi.GameApi;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

/**
 * Description : 赢大奖-奖励说明
 *
 * @author : rocky
 * @Create Time : 2017/10/31 下午8:51
 * @Modified Time : 2017/10/31 下午8:51
 */
public class GameAwardDialog extends BaseDialog {
    private Context context;
    private GameBean gameBean;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.iv_panda_rocket)
    ImageView ivPandaRocket;
    @BindView(R.id.iv_webView_close)
    ImageView ivWebViewClose;
    private List<WinGameAwardBean> list = new ArrayList<>();
    private GameAwardAdapter adapter;

    public GameAwardDialog(Context context, GameBean gameBean) {
        super(context);
        this.context = context;
        this.gameBean = gameBean;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_game_win_award;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);

        adapter = new GameAwardAdapter(context, list);
        recycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recycler.setAdapter(adapter);

        getDataList();
    }

    private void getDataList() {
        HttpUtil.subscribe(HttpUtil.createApi(GameApi.class, Constants.URL).getGameBonus(gameBean.uniqueMark), new Observer<ResultArray<WinGameAwardBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                dismiss();
            }

            @Override
            public void onNext(ResultArray<WinGameAwardBean> winGameAwardBeanResultArray) {
                if (winGameAwardBeanResultArray.result == 1) {
                    List<WinGameAwardBean> data = winGameAwardBeanResultArray.data;
                    adapter.loadMoreData(data);
                } else {
                    dismiss();
                }
            }
        });
    }

    @OnClick({R.id.iv_webView_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_webView_close:
                dismiss();
                break;
        }
    }
}
