package com.leyou.game.adapter.friend;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.leyou.game.R;
import com.leyou.game.activity.GameDetailActivity_;
import com.leyou.game.bean.UserData;
import com.leyou.game.bean.game.GameBean;
import com.leyou.game.widget.dialog.LogInDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description : com.leyou.game.adapter.friend
 *
 * @author : rocky
 * @Create Time : 2017/8/12 上午10:48
 * @Modified Time : 2017/8/12 上午10:48
 */
public class FriendPlayedGameAdapter extends RecyclerView.Adapter<FriendPlayedGameAdapter.ViewHolder> {

    private Context context;
    private List<GameBean> list;

    public FriendPlayedGameAdapter(Context context, List<GameBean> gameList) {
        this.context = context;
        this.list = gameList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_friend_played_game_list, parent, false);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final GameBean gameBean = list.get(position);
        holder.tvGameName.setText(gameBean.name);
        holder.ivContactHeader.setImageURI(gameBean.iconUrl);
        holder.ivContactHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserData.getInstance().isLogIn()) {
                    Intent intent = new Intent(context, GameDetailActivity_.class);
                    intent.putExtra("game", gameBean);
                    context.startActivity(intent);
                } else {
                    new LogInDialog(context, false).show();
                }
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

    public void setAdapterData(List<GameBean> data) {
        if (null != data && data.size() > 0) {
            this.list = data;
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_contact_header)
        SimpleDraweeView ivContactHeader;
        @BindView(R.id.tv_game_name)
        TextView tvGameName;
        @BindView(R.id.ll_item_game)
        RelativeLayout llItemGame;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
