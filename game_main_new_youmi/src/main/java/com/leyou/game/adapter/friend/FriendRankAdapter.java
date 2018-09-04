package com.leyou.game.adapter.friend;

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
import com.leyou.game.util.NumberFormatUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description : com.leyou.game.adapter.friend
 *
 * @author : rocky
 * @Create Time : 2017/7/26 上午9:55
 * @Modified Time : 2017/7/26 上午9:55
 */
public class FriendRankAdapter extends RecyclerView.Adapter<FriendRankAdapter.ViewHolder> {

    private Context context;
    private List<GameRankBean> list;

    public FriendRankAdapter(Context context, List<GameRankBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_friend_rank_list, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        GameRankBean contactBean = list.get(position);
        holder.ivWinResultHead.setImageURI(contactBean.headImgUrl);
        holder.tvWinResultPlayerName.setText(contactBean.nickname);
        holder.tvWinResultRank.setText("" + contactBean.score + "分");
        holder.tvWinResultPlayerNo.setText("No." + (position + 1) + "");
        if (contactBean.isMyself()) {
            holder.reRoot.setSelected(true);
            holder.tvWinResultPlayerNo.setTextColor(context.getResources().getColor(R.color.white));
            holder.tvWinResultPlayerName.setTextColor(context.getResources().getColor(R.color.white));
            holder.tvWinResultRank.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            holder.reRoot.setSelected(false);
            holder.tvWinResultPlayerNo.setTextColor(context.getResources().getColor(R.color.purple_74));
            holder.tvWinResultPlayerName.setTextColor(context.getResources().getColor(R.color.black_ac));
            holder.tvWinResultRank.setTextColor(context.getResources().getColor(R.color.purple_74));
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

    public void refreshAdapterData(List<GameRankBean> listInfo) {
        list.clear();
        list.addAll(listInfo);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.re_root)
        RelativeLayout reRoot;
        @BindView(R.id.iv_win_result_head)
        SimpleDraweeView ivWinResultHead;
        @BindView(R.id.tv_win_result_player_no)
        TextView tvWinResultPlayerNo;
        @BindView(R.id.tv_win_result_player_name)
        TextView tvWinResultPlayerName;
        @BindView(R.id.tv_win_result_rank)
        TextView tvWinResultRank;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
