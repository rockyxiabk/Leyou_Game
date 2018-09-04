package com.leyou.game.adapter.friend;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.leyou.game.R;
import com.leyou.game.bean.ContactBean;
import com.leyou.game.bean.GameRankBean;

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
        holder.ivWinResultHead.setImageURI(contactBean.pictureUrl);
        holder.tvWinResultPlayerName.setText(contactBean.userName);
        holder.tvWinResultRank.setText(contactBean.score + "分");
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
        @BindView(R.id.iv_win_result_head)
        SimpleDraweeView ivWinResultHead;
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
