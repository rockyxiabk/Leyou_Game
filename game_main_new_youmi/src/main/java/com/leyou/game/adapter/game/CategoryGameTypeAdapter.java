package com.leyou.game.adapter.game;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.leyou.game.R;
import com.leyou.game.adapter.CustomItemClickListener;
import com.leyou.game.bean.game.GameBean;
import com.leyou.game.fragment.GameFragment_;
import com.leyou.game.ipresenter.game.IGameFragment;

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
public class CategoryGameTypeAdapter extends RecyclerView.Adapter<CategoryGameTypeAdapter.ViewHolder> {
    private IGameFragment iGameFragment;
    private Context context;
    private List<GameBean> list;
    private CustomItemClickListener listener;
    private int selectorItemPosition = -1;//当前选中的条目

    public CategoryGameTypeAdapter(Context context, IGameFragment iGameFragment, List<GameBean> gameList) {
        this.context = context;
        this.iGameFragment = iGameFragment;
        this.list = gameList;
    }

    public void setOnCustomItemClickListener(CustomItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_game_category_type_item, parent, false);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final GameBean gameBean = list.get(position);
        holder.tvGameName.setText(gameBean.title);
        if ((position + 1) % 3 == 0) {
            holder.viewLine.setVisibility(View.GONE);
        } else {
            holder.viewLine.setVisibility(View.VISIBLE);
        }

        if (position == selectorItemPosition) {
            holder.tvGameName.setTextColor(context.getResources().getColor(R.color.blue_44));
        } else {
            holder.tvGameName.setTextColor(context.getResources().getColor(R.color.text_color_selector));
        }
        holder.tvGameName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iGameFragment.showAllCategory();
                selectorItemPosition = position;
                listener.onItemClick(null, position);
                notifyDataSetChanged();
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
            selectorItemPosition = -1;
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_category_name)
        TextView tvGameName;
        @BindView(R.id.view_line)
        View viewLine;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
