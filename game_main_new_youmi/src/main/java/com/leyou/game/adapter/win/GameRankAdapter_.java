package com.leyou.game.adapter.win;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.leyou.game.R;
import com.leyou.game.bean.game.GameRankBean;
import com.leyou.game.util.DataUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description : 游戏玩家排行榜适配器
 *
 * @author : rocky
 * @Create Time : 2017/10/22 下午3:10
 * @Modified By: rocky
 * @Modified Time : 2017/10/22 下午3:10
 */
public class GameRankAdapter_ extends RecyclerView.Adapter<GameRankAdapter_.GameRankHolder> {

    private Context context;
    private List<GameRankBean> list;

    public GameRankAdapter_(Context context, List<GameRankBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public GameRankHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_dialog_game_rank_item, parent, false);
        GameRankHolder holder = new GameRankHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(GameRankHolder holder, int position) {
        holder.setIsRecyclable(false);
        if (position <= list.size() - 1) {
            GameRankBean gameRankBean = list.get(position);
            holder.reRoot.setVisibility(View.VISIBLE);
            holder.ivGameRankHeaderImg.setImageURI(gameRankBean.headImgUrl);
            holder.tvGameRankNo.setText("No." + String.valueOf(gameRankBean.rank));
            holder.tvGameRankName.setText(gameRankBean.nickname);
            holder.tvGameRankScore.setText(gameRankBean.score + "分");
            if (gameRankBean.isMyself()) {
                holder.reRoot.setSelected(true);
                holder.tvGameRankNo.setTextColor(context.getResources().getColor(R.color.white));
                holder.tvGameRankName.setTextColor(context.getResources().getColor(R.color.white));
                holder.tvGameRankScore.setTextColor(context.getResources().getColor(R.color.white));
                if (gameRankBean.score <= 0) {
                    holder.tvGameRankNo.setText("No.--");
                    holder.tvGameRankScore.setText("暂无排名");
                }
            } else {
                holder.reRoot.setSelected(false);
                holder.tvGameRankNo.setTextColor(context.getResources().getColor(R.color.purple_74));
                holder.tvGameRankName.setTextColor(context.getResources().getColor(R.color.black_ac));
                holder.tvGameRankScore.setTextColor(context.getResources().getColor(R.color.purple_74));
            }
        } else {
            holder.reRoot.setVisibility(View.INVISIBLE);
        }

    }

    public void loadMoreData(List<GameRankBean> gameRankBeanList) {
        if (null != gameRankBeanList && gameRankBeanList.size() > 0) {
            list.addAll(gameRankBeanList);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        int ret = 0;
        if (null != list && list.size() > 0) {
            ret = list.size();
        }
        return 6;
    }

    public class GameRankHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.re_game_rank_root)
        RelativeLayout reRoot;
        @BindView(R.id.iv_game_rank_header_img)
        SimpleDraweeView ivGameRankHeaderImg;
        @BindView(R.id.tv_game_rank_no)
        TextView tvGameRankNo;
        @BindView(R.id.tv_game_rank_name)
        TextView tvGameRankName;
        @BindView(R.id.tv_game_rank_score)
        TextView tvGameRankScore;

        public GameRankHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
