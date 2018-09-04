package com.leyou.game.adapter.mine;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.leyou.game.R;
import com.leyou.game.bean.TradeBean;
import com.leyou.game.util.DataUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description : com.leyou.game.adapter
 *
 * @author : rocky
 * @Create Time : 2017/5/10 下午3:33
 * @Modified Time : 2017/5/10 下午3:33
 */
public class TradeNoteAdapter extends RecyclerView.Adapter<TradeNoteAdapter.TradeHolder> {

    private int type;
    private Context context;
    private List<TradeBean> list;

    public TradeNoteAdapter(Context context, List<TradeBean> list, int type) {
        this.context = context;
        this.list = list;
        this.type = type;
    }

    @Override
    public TradeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_trade_note_list, parent, false);
        TradeHolder holder = new TradeHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(TradeHolder holder, int position) {
        TradeBean tradeBean = list.get(position);
        holder.tvTradeNoteMoney.setText("¥" + tradeBean.price);
        holder.tvTradeNoteDiamondNumber.setText(tradeBean.virtualCoin + "钻石");
        if (type == 1) {
            holder.tvTradeNoteTime.setText("交易时间：" + DataUtil.getConvertResult(tradeBean.time, DataUtil.Y_M_D_HM));
            holder.tvTradeNoteState.setText("交易成功");
        } else {
            if (tradeBean.flag==1) {//交易成功
                holder.tvTradeNoteTime.setText("交易时间：" + DataUtil.getConvertResult(tradeBean.time, DataUtil.Y_M_D_HM));
                holder.tvTradeNoteState.setText("交易成功");
            } else {//正在交易
                holder.tvTradeNoteTime.setText("出售时间：" + DataUtil.getConvertResult(tradeBean.time, DataUtil.Y_M_D_HM));
                holder.tvTradeNoteState.setTextColor(context.getResources().getColor(R.color.red_f2));
                holder.tvTradeNoteState.setText("正在出售中");
            }
        }
    }

    public void setTradeNotAdapter(List<TradeBean> tradeBeen) {
        list.clear();
        list.addAll(tradeBeen);
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

    public class TradeHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_trade_note_diamond_number)
        TextView tvTradeNoteDiamondNumber;
        @BindView(R.id.tv_trade_note_time)
        TextView tvTradeNoteTime;
        @BindView(R.id.tv_trade_note_money)
        TextView tvTradeNoteMoney;
        @BindView(R.id.tv_trade_note_state)
        TextView tvTradeNoteState;

        public TradeHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
