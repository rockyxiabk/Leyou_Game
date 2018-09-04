package com.leyou.game.adapter.mine;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.leyou.game.R;
import com.leyou.game.bean.game.GameWinPriseBean;
import com.leyou.game.util.NumberFormatUtil;
import com.leyou.game.widget.TextViewRotate45;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description : com.leyou.game.adapter
 *
 * @author : rocky
 * @Create Time : 2017/4/21 下午2:10
 * @Modified Time : 2017/4/21 下午2:10
 */
public class WinResultAdapter extends RecyclerView.Adapter<WinResultAdapter.WinResultHolder> {

    private Context context;
    private List<GameWinPriseBean> list;

    public WinResultAdapter(Context context, List<GameWinPriseBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public WinResultHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_last_win_result_list, parent, false);
        WinResultHolder holder = new WinResultHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(WinResultHolder holder, int position) {
        holder.setIsRecyclable(false);
        GameWinPriseBean personInfo = list.get(position);
//        switch (personInfo.prizeRank) {
//            case 1:
//                holder.ivWinResultRank.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_first_rank));
//                holder.tvWinResultRank.setVisibility(View.GONE);
//                break;
//            case 2:
//                holder.ivWinResultRank.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_second_rank));
//                holder.tvWinResultRank.setVisibility(View.GONE);
//                break;
//            case 3:
//                holder.ivWinResultRank.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_third_rank));
//                holder.tvWinResultRank.setVisibility(View.GONE);
//                break;
//            default:
//                holder.ivWinResultRank.setVisibility(View.GONE);
//                holder.tvWinResultRank.setText(NumberFormatUtil.formatInteger(personInfo.prizeRank) + "等奖");
//                break;
//        }
        holder.ivWinResultHead.setImageURI(personInfo.prizeImgUrl);
        holder.tvName.setText(personInfo.nickname);
//        holder.tvWinResultPlayerName.setText(personInfo.nickname);
//        holder.tvWinResultPrizeInfo.setText(personInfo.prizeName);
    }

    public void refreshAdapterData(List<GameWinPriseBean> listInfo) {
        list.clear();
        list.addAll(listInfo);
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

    public class WinResultHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_win_result_head)
        SimpleDraweeView ivWinResultHead;
        @BindView(R.id.tv_nick_name)
        TextViewRotate45 tvName;
//        @BindView(R.id.tv_win_result_player_name)
//        TextView tvWinResultPlayerName;
//        @BindView(R.id.tv_win_result_prize_info)
//        TextView tvWinResultPrizeInfo;
//        @BindView(R.id.iv_win_result_rank)
//        ImageView ivWinResultRank;
//        @BindView(R.id.tv_win_result_rank)
//        TextView tvWinResultRank;

        public WinResultHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
