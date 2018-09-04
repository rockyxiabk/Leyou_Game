package com.leyou.game.adapter.mine;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.leyou.game.R;
import com.leyou.game.activity.GameDetailActivity;
import com.leyou.game.bean.GameBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description : 签到页面 推荐位游戏适配器
 *
 * @author : rocky
 * @Create Time : 2017/4/20 下午6:26
 * @Modified Time : 2017/4/20 下午6:26
 */
public class SignRecommendGameAdapter extends RecyclerView.Adapter<SignRecommendGameAdapter.SignHolder> {

    private Context context;
    private List<GameBean> list;

    public SignRecommendGameAdapter(Context context, List<GameBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public SignHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_sign_list, parent, false);
        SignHolder holder = new SignHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(SignHolder holder, int position) {
        holder.ivSignListGameImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, GameDetailActivity.class));
            }
        });
    }

    public void setRecommendGameData(List<GameBean> gameBeanList) {
        list.clear();
        list.addAll(gameBeanList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        int ret = 6;
        if (null != list && list.size() > 0) {
            ret = list.size();
        }
        return ret;
    }

    public class SignHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_sign_list_game_img)
        SimpleDraweeView ivSignListGameImg;
        @BindView(R.id.tv_sign_list_game_name)
        TextView tvSignListGameName;
        @BindView(R.id.ll_sign_list_item)
        LinearLayout llSignListItem;

        public SignHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
