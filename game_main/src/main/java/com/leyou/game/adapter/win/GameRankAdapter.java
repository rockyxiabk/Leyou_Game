package com.leyou.game.adapter.win;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.leyou.game.R;
import com.leyou.game.bean.GameRankBean;
import com.leyou.game.util.DataUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description : 游戏玩家排行榜适配器
 *
 * @author : rocky
 * @Create Time : 2017/4/25 下午6:21
 * @Modified Time : 2017/4/25 下午6:21
 */
public class GameRankAdapter extends RecyclerView.Adapter<GameRankAdapter.GameRankHolder> {

    private Context context;
    private List<GameRankBean> list;

    public GameRankAdapter(Context context, List<GameRankBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public GameRankHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_dialog_game_rank_list, parent, false);
        GameRankHolder holder = new GameRankHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(GameRankHolder holder, int position) {
        holder.setIsRecyclable(false);
        GameRankBean gameRankBean = list.get(position);
        holder.ivGameRankHeaderImg.setImageURI(gameRankBean.pictureUrl);
        holder.tvGameRankName.setText(gameRankBean.userName);
        holder.tvGameRankScore.setText(gameRankBean.score + "分");
        String constellation = DataUtil.getConstellation(gameRankBean.birthday);//星座
        String age = DataUtil.getAgeBuyTimeStamp(gameRankBean.birthday);//年龄
        String sex;
        if (1 == gameRankBean.sex) {
            sex = "男";
        } else {
            sex = "女";
        }
        holder.tvGameRankInfo.setText(age + "岁  " + sex + "  " + constellation);
        switch (gameRankBean.ranking) {
            case 1:
                holder.tvGameRankText.setVisibility(View.GONE);
                holder.ivGameRankImg.setVisibility(View.VISIBLE);
                holder.ivGameRankImg.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_no1));
                break;
            case 2:
                holder.tvGameRankText.setVisibility(View.GONE);
                holder.ivGameRankImg.setVisibility(View.VISIBLE);
                holder.ivGameRankImg.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_no2));
                break;
            case 3:
                holder.tvGameRankText.setVisibility(View.GONE);
                holder.ivGameRankImg.setVisibility(View.VISIBLE);
                holder.ivGameRankImg.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_no3));
                break;
            default:
                holder.ivGameRankImg.setVisibility(View.GONE);
                holder.tvGameRankText.setVisibility(View.VISIBLE);
                holder.tvGameRankText.setText("" + gameRankBean.ranking);
                break;
        }

    }

    public void updateAdapter(List<GameRankBean> gameRankBeanList) {
        list.clear();
        if (null != gameRankBeanList && gameRankBeanList.size() > 0) {
            list.addAll(gameRankBeanList);
        }
        notifyDataSetChanged();
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
        return ret;
    }

    public class GameRankHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_game_rank_header_img)
        SimpleDraweeView ivGameRankHeaderImg;
        @BindView(R.id.iv_game_rank_img)
        ImageView ivGameRankImg;
        @BindView(R.id.tv_game_rank_text)
        TextView tvGameRankText;
        @BindView(R.id.tv_game_rank_name)
        TextView tvGameRankName;
        @BindView(R.id.tv_game_rank_info)
        TextView tvGameRankInfo;
        @BindView(R.id.tv_game_rank_score)
        TextView tvGameRankScore;

        public GameRankHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
