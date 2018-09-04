package com.leyou.game.adapter.fight;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leyou.game.R;
import com.leyou.game.adapter.CustomItemClickListener;
import com.leyou.game.dao.SearchHistory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description : com.leyou.game.adapter
 *
 * @author : rocky
 * @Create Time : 2017/7/14 下午4:38
 * @Modified Time : 2017/7/14 下午4:38
 */
public class GameHistorySearchAdapter extends RecyclerView.Adapter<GameHistorySearchAdapter.ViewHolder> {

    private Context context;
    private List<SearchHistory> list;
    private CustomItemClickListener listener;

    public GameHistorySearchAdapter(Context context, List<SearchHistory> list) {
        this.context = context;
        this.list = list;
    }

    public void setListener(CustomItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = inflate(parent, R.layout.layout_game_search_history_list);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    private View inflate(ViewGroup parent, int layoutRes) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tvSearchKey.setText(list.get(position).getKeyWord());
        holder.reItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(null, position);
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

    public void refreshAdapterList(List<SearchHistory> data) {
        list.clear();
        list.addAll(data);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_history_search_key)
        TextView tvSearchKey;
        @BindView(R.id.re_item)
        RelativeLayout reItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
