package com.leyou.game.adapter.win;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.leyou.game.R;
import com.leyou.game.bean.game.GameRankBean;
import com.leyou.game.bean.win.WinGameAwardBean;
import com.leyou.game.util.NumberFormatUtil;
import com.leyou.game.util.NumberUtil;

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
public class GameAwardAdapter extends RecyclerView.Adapter<GameAwardAdapter.GameRankHolder> {

    private Context context;
    private List<WinGameAwardBean> list;

    public GameAwardAdapter(Context context, List<WinGameAwardBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public GameRankHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_dialog_game_award_item, parent, false);
        GameRankHolder holder = new GameRankHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(GameRankHolder holder, int position) {
        holder.setIsRecyclable(false);
        WinGameAwardBean WinGameAwardBean = list.get(position);
        holder.reRoot.setVisibility(View.VISIBLE);
        holder.ivGameRankHeaderImg.setImageURI(WinGameAwardBean.imgSmallUrl);
        holder.tvGameRankNo.setText("" + NumberFormatUtil.formatInteger(position + 1) + "等奖");
        holder.tvGameRankName.setText(WinGameAwardBean.name);
        holder.tvGameRankScore.setText("" + WinGameAwardBean.price + "元");
    }

    public void loadMoreData(List<WinGameAwardBean> WinGameAwardBeanList) {
        if (null != WinGameAwardBeanList && WinGameAwardBeanList.size() > 0) {
            list.clear();
            list.addAll(WinGameAwardBeanList);
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
