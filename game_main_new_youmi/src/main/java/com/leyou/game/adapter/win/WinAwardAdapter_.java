package com.leyou.game.adapter.win;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.leyou.game.R;
import com.leyou.game.activity.PlayGameActivity;
import com.leyou.game.bean.game.GameBean;
import com.leyou.game.util.ScreenUtil;
import com.leyou.game.widget.dialog.winaward.GameAwardDialog;
import com.leyou.game.widget.dialog.winaward.GameRankDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description : 赢大奖页面列表数据适配器
 *
 * @author : rocky
 * @Create Time : 2017/4/12 下午6:46
 * @Modified Time : 2017/4/12 下午6:46
 */
public class WinAwardAdapter_ extends RecyclerView.Adapter<WinAwardAdapter_.WinAwardHolder> {

    private Context context;
    private List<GameBean> list;
    private Map<Integer, Boolean> map = new HashMap<>();
    private int lastPosition = -1;

    public WinAwardAdapter_(Context context, List<GameBean> list) {
        this.context = context;
        this.list = list;
        resetMap();
    }


    @Override
    public WinAwardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_win_award_list, parent, false);
        WinAwardHolder holder = new WinAwardHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final WinAwardHolder holder, final int position) {
        final GameBean gameBean = list.get(position);
        holder.ivGamePic.setImageURI(gameBean.bannerUrl);
        holder.tvGameScore.setText("当前成绩：" + String.valueOf(gameBean.score));
        holder.tvAwardExplain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameAwardDialog AwardDialog = new GameAwardDialog(context, gameBean);
                AwardDialog.show();
            }
        });
        holder.tvGameRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameRankDialog rankDialog = new GameRankDialog(context, gameBean);
                rankDialog.show();
            }
        });
        holder.btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlayGameActivity.class);
                intent.putExtra("game", gameBean);
                context.startActivity(intent);
            }
        });
        holder.cvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastPosition != position) {
                    map.put(lastPosition, false);
                }
                map.put(position, !map.get(position));
                if (map.get(position)) {
                    lastPosition = position;
                    YoYo.with(Techniques.FadeInDown).duration(100).playOn(holder.reGameDetail);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            holder.reGameDetail.setVisibility(View.VISIBLE);
                        }
                    }, 50);
                } else {
                    YoYo.with(Techniques.FadeOutUp).duration(50).playOn(holder.reGameDetail);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            holder.reGameDetail.setVisibility(View.GONE);
                        }
                    }, 100);
                }
                setImageRadius(holder, position);
                notifyDataSetChanged();
            }
        });
        if (map.get(position)) {
            holder.reGameDetail.setVisibility(View.VISIBLE);
        } else {
            holder.reGameDetail.setVisibility(View.GONE);
        }
        setImageRadius(holder, position);
    }

    private void setImageRadius(final WinAwardHolder holder, int position) {
        if (map.get(position)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                RoundingParams roundingParams = RoundingParams.fromCornersRadius(ScreenUtil.dp2px(context, 0f));
                roundingParams.setCornersRadii(ScreenUtil.dp2px(context, 10f), ScreenUtil.dp2px(context, 10f), 0f, 0f);
                holder.ivGamePic.getHierarchy().setRoundingParams(roundingParams);
            } else {
                RoundingParams roundingParams = RoundingParams.fromCornersRadius(ScreenUtil.dp2px(context, 0f));
                roundingParams.setCornersRadii(ScreenUtil.dp2px(context, 5f), ScreenUtil.dp2px(context, 5f), 0f, 0f);
                holder.ivGamePic.getHierarchy().setRoundingParams(roundingParams);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                RoundingParams roundingParams = RoundingParams.fromCornersRadius(ScreenUtil.dp2px(context, 0f));
                roundingParams.setCornersRadii(ScreenUtil.dp2px(context, 10f), ScreenUtil.dp2px(context, 10f), ScreenUtil.dp2px(context, 10f), ScreenUtil.dp2px(context, 10f));
                holder.ivGamePic.getHierarchy().setRoundingParams(roundingParams);
            } else {
                RoundingParams roundingParams = RoundingParams.fromCornersRadius(ScreenUtil.dp2px(context, 0f));
                roundingParams.setCornersRadii(ScreenUtil.dp2px(context, 5f), ScreenUtil.dp2px(context, 5f), ScreenUtil.dp2px(context, 5f), ScreenUtil.dp2px(context, 5f));
                holder.ivGamePic.getHierarchy().setRoundingParams(roundingParams);
            }
        }
    }

    @Override
    public int getItemCount() {
        int ret = 0;
        if (null != list && list.size() > 0) {
            ret = list.size();
        }
        return ret;
    }

    private void resetMap() {
        lastPosition = -1;
        for (int i = 0; i < list.size(); i++) {
            map.put(i, false);
        }
    }

    public void updateAdapter(List<GameBean> gameBeanList) {
        list.clear();
        if (null != gameBeanList && gameBeanList.size() > 0) {
            list.addAll(gameBeanList);
        }
        resetMap();
        notifyDataSetChanged();
    }

    public class WinAwardHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cv_content)
        CardView cvItem;
        @BindView(R.id.iv_game_pic)
        SimpleDraweeView ivGamePic;
        @BindView(R.id.tv_award_explain)
        TextView tvAwardExplain;
        @BindView(R.id.re_game_detail)
        RelativeLayout reGameDetail;
        @BindView(R.id.tv_game_score)
        TextView tvGameScore;
        @BindView(R.id.btn_start_game)
        Button btnStartGame;
        @BindView(R.id.tv_game_rank)
        TextView tvGameRank;

        public WinAwardHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
