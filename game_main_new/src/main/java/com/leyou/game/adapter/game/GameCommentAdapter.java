package com.leyou.game.adapter.game;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.leyou.game.R;
import com.leyou.game.bean.game.GameCommentBean;
import com.leyou.game.util.DataUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description : 游戏评论数据适配器
 *
 * @author : rocky
 * @Create Time : 2017/10/23 上午00:48
 * @Modified Time : 2017/10/23 上午00:48
 */
public class GameCommentAdapter extends RecyclerView.Adapter<GameCommentAdapter.ViewHolder> {

    private Context context;
    private List<GameCommentBean> list;

    public GameCommentAdapter(Context context, List<GameCommentBean> gameList) {
        this.context = context;
        this.list = gameList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_game_comment_item, parent, false);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GameCommentBean gameCommentBean = list.get(position);
        holder.ivGameCommentPlayerHeader.setImageURI(gameCommentBean.headImgUrl);
        holder.tvGameCommentPlayerName.setText(gameCommentBean.nickname);
        holder.tvGamePraiseCount.setText("" + gameCommentBean.praiseNum + "");
        holder.tvGameCommentContent.setText(gameCommentBean.comment);
        holder.tvGameCommentTime.setText(DataUtil.getConvertResult(gameCommentBean.createTime, DataUtil.Y_M_D));
    }

    @Override
    public int getItemCount() {
        int ret = 0;
        if (null != list && list.size() > 0) {
            ret = list.size();
        }
        return ret;
    }

    public void setAdapterData(List<GameCommentBean> data) {
        if (null != data && data.size() > 0) {
            this.list = data;
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_game_comment_player_header)
        SimpleDraweeView ivGameCommentPlayerHeader;
        @BindView(R.id.tv_game_comment_player_name)
        TextView tvGameCommentPlayerName;
        @BindView(R.id.tv_game_comment_time)
        TextView tvGameCommentTime;
        @BindView(R.id.tv_game_comment_content)
        TextView tvGameCommentContent;
        @BindView(R.id.tv_game_praise_count)
        TextView tvGamePraiseCount;
        @BindView(R.id.iv_praise_comment)
        ImageView ivPraiseComment;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
