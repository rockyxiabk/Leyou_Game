package com.leyou.game.adapter.game;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.leyou.game.R;
import com.leyou.game.activity.GameDetailActivity_;
import com.leyou.game.activity.PlayGameActivity;
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
public class GameAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private Context context;
    private List<GameBean> list;
    private boolean isLoadAllData = false;

    public GameAdapter(Context context, List<GameBean> gameList) {
        this.context = context;
        this.list = gameList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        if (viewType == TYPE_ITEM) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_game_list, parent, false);
            return new ItemViewHolder(itemView);
        } else if (viewType == TYPE_FOOTER) {
            itemView = LayoutInflater.from(context).inflate(R.layout.layout_recycler_footer, parent, false);
            return new FootViewHolder(itemView);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            final GameBean gameBean = list.get(position);
            itemViewHolder.ivGameImg.setImageURI(gameBean.bannerUrl);
            itemViewHolder.tvGameDes.setText(gameBean.recommend);
            itemViewHolder.tvGameSlogn.setText(gameBean.propaganda);
            itemViewHolder.btnStartGame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (UserData.getInstance().isLogIn()) {
                        Intent intent = new Intent(context, PlayGameActivity.class);
                        intent.putExtra("game", gameBean);
                        context.startActivity(intent);
                    } else {
                        new LogInDialog(context, false).show();
                    }
                }
            });
            itemViewHolder.ivGameImg.setOnClickListener(new View.OnClickListener() {
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
        } else if (holder instanceof FootViewHolder) {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            if (!isLoadAllData) {
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.loading_rotate_anim);
                footViewHolder.ivFooterProgress.startAnimation(animation);
            } else {
                footViewHolder.ivFooterProgress.setVisibility(View.GONE);
                footViewHolder.tvFooterDes.setText(context.getString(R.string.no_more_message));
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

    public void setAdapterData(List<GameBean> data) {
        if (null != data && data.size() > 0) {
            this.list = data;
            notifyDataSetChanged();
        }
    }


    public void loadMoreData(List<GameBean> data) {
        if (null != data && data.size() > 0) {
            list.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void setLoadAllData(boolean loadAllData) {
        this.isLoadAllData = loadAllData;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_game_img)
        SimpleDraweeView ivGameImg;
        @BindView(R.id.tv_game_slogn)
        TextView tvGameSlogn;
        @BindView(R.id.tv_game_des)
        TextView tvGameDes;
        @BindView(R.id.btn_start_game)
        Button btnStartGame;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class FootViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_footer_progress)
        ImageView ivFooterProgress;
        @BindView(R.id.tv_footer_des)
        TextView tvFooterDes;

        public FootViewHolder(View temView) {
            super(temView);
            ButterKnife.bind(this, itemView);
        }
    }
}
