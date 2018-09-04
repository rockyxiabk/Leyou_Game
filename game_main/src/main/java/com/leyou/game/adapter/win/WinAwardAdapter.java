package com.leyou.game.adapter.win;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.leyou.game.R;
import com.leyou.game.activity.PlayGameActivity;
import com.leyou.game.bean.GameBean;
import com.leyou.game.util.LogUtil;
import com.leyou.game.widget.dialog.GameRankDialog;
import com.leyou.game.widget.dialog.TextExplainDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description : 赢大奖页面列表数据适配器
 *
 * @author : rocky
 * @Create Time : 2017/4/12 下午6:46
 * @Modified Time : 2017/4/12 下午6:46
 */
public class WinAwardAdapter extends RecyclerView.Adapter<WinAwardAdapter.WinAwardHolder> {

    private Context context;
    private List<GameBean> list;

    public WinAwardAdapter(Context context, List<GameBean> list) {
        this.context = context;
        this.list = list;
    }


    @Override
    public WinAwardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_win_award_recycler_list, parent, false);
        WinAwardHolder holder = new WinAwardHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(WinAwardHolder holder, int position) {
        final GameBean gameBean = list.get(position);
        LogUtil.d("tag", "----pic--" + gameBean.pictureUrl);
        Glide.with(context).load(gameBean.pictureUrl).error(R.mipmap.icon_game_big_bg).into(holder.ivGameImg);
        holder.tvGameName.setText(gameBean.name);
        holder.tvGameDes.setText(gameBean.readme);
        holder.tvGameAchievement.setText("当前成绩：" + gameBean.score);
        holder.tvGameAwardExplain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextExplainDialog dialog = new TextExplainDialog(context, context.getString(R.string.game_award_explain), gameBean.bonusState);
                dialog.show();
            }
        });
        holder.tvGameCharts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameRankDialog rankDialog = new GameRankDialog(context, gameBean.mark);
                rankDialog.show();
            }
        });
        holder.btnGamePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlayGameActivity.class);
                intent.putExtra("game", gameBean);
                context.startActivity(intent);
            }
        });
        holder.ivGameImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        int ret = 0;
        if (null != list && list.size() > 0) {
            ret = list.size();
        }
        return ret;
    }

    public void updateAdapter(List<GameBean> gameBeanList) {
        list.clear();
        if (null != gameBeanList && gameBeanList.size() > 0) {
            list.addAll(gameBeanList);
        }
        notifyDataSetChanged();
    }

    public class WinAwardHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.win_lump_view)
        View winLumpView;
        @BindView(R.id.tv_game_name)
        TextView tvGameName;
        @BindView(R.id.tv_game_achievement)
        TextView tvGameAchievement;
        @BindView(R.id.re_win_title)
        RelativeLayout reWinTitle;
        @BindView(R.id.iv_game_img)
        ImageView ivGameImg;
        @BindView(R.id.tv_game_des)
        TextView tvGameDes;
        @BindView(R.id.tv_game_award_explain)
        TextView tvGameAwardExplain;
        @BindView(R.id.re_win_container)
        RelativeLayout reWinContainer;
        @BindView(R.id.tv_game_charts)
        TextView tvGameCharts;
        @BindView(R.id.btn_game_play)
        Button btnGamePlay;

        public WinAwardHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
