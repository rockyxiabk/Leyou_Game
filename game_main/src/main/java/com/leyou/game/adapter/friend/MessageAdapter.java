package com.leyou.game.adapter.friend;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leyou.game.R;
import com.leyou.game.adapter.CustomItemClickListener;
import com.leyou.game.bean.MessageBean;
import com.leyou.game.event.FriendFragmentRefreshEvent;
import com.leyou.game.util.DataUtil;
import com.leyou.game.util.LogUtil;
import com.leyou.game.widget.DragPointView;
import com.leyou.game.widget.dialog.MessageDialog;
import com.leyou.game.widget.dialog.MessagePrizeDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description : com.leyou.game.adapter
 *
 * @author : rocky
 * @Create Time : 2017/4/20 下午7:30
 * @Modified Time : 2017/4/20 下午7:30
 */
public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "MessageAdapter";
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private Context context;
    private int currentNumber = 1;
    private List<MessageBean> list;
    private boolean isLoadAllData = false;
    private CustomItemClickListener listener;

    public void setOnItemClickListener(CustomItemClickListener listener) {
        this.listener = listener;
    }

    public MessageAdapter(Context context, List<MessageBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + currentNumber == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == TYPE_ITEM) {
            view = LayoutInflater.from(context).inflate(R.layout.layout_message_list, parent, false);
            return new MessageHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            view = LayoutInflater.from(context).inflate(R.layout.layout_recycler_footer, parent, false);
            return new FootViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder1, final int position) {
        holder1.setIsRecyclable(false);
        if (holder1 instanceof MessageHolder) {
            final MessageBean messageBean = list.get(position);
            MessageHolder messageHolder = (MessageHolder) holder1;
            messageHolder.tvMessageListTime.setText(DataUtil.msg_distance_time(messageBean.time, System.currentTimeMillis()));
            messageHolder.tvMessageListTitle.setText(messageBean.title);
            messageHolder.tvMessageListDes.setText(messageBean.content);
            if (messageBean.flag == 1) {
                messageHolder.tvMessageListRead.setVisibility(View.GONE);
            } else {
                messageHolder.tvMessageListRead.setVisibility(View.VISIBLE);
            }
            if (messageBean.type == 1) {
                messageHolder.ivMessageIcon.setImageResource(R.mipmap.icon_system_notice);
            } else if (messageBean.type == 2) {
                messageHolder.ivMessageIcon.setImageResource(R.mipmap.icon_message_award);
            }
            messageHolder.reMessageListItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setRead(position);
                    if (messageBean.type == 1) {
                        new MessageDialog(context, messageBean.id, messageBean.infoType, messageBean.title).show();
                    } else if (messageBean.type == 2) {
                        new MessagePrizeDialog(context,messageBean.id,messageBean.title).show();
                    }
                }
            });

        } else if (holder1 instanceof FootViewHolder) {
            FootViewHolder footViewHolder = (FootViewHolder) holder1;
            if (!isLoadAllData) {
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.loading_rotate_anim);
                footViewHolder.ivFooterProgress.startAnimation(animation);
            } else {
                footViewHolder.ivFooterProgress.setVisibility(View.GONE);
                footViewHolder.tvFooterDes.setText(context.getString(R.string.no_more_message));
            }
        }
    }

    private void setRead(int position) {
        MessageBean messageBean = list.get(position);
        messageBean.flag = 1;
        list.remove(position);
        list.add(position, messageBean);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        int ret = 0;
        if (null != list && list.size() > 0) {
            ret = list.size() + currentNumber;
        }
        return ret;
    }

    public void addAdapter(List<MessageBean> currentList) {
        list.addAll(currentList);
        notifyDataSetChanged();
    }

    public void setLoadAllData(boolean loadAllData) {
        this.isLoadAllData = loadAllData;
        LogUtil.d(TAG, "----flag:" + isLoadAllData);
    }

    public class MessageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_message_icon)
        ImageView ivMessageIcon;
        @BindView(R.id.tv_message_list_title)
        TextView tvMessageListTitle;
        @BindView(R.id.tv_message_list_time)
        TextView tvMessageListTime;
        @BindView(R.id.tv_message_read_state)
        DragPointView tvMessageListRead;
        @BindView(R.id.tv_message_list_des)
        TextView tvMessageListDes;
        @BindView(R.id.re_message_list_item)
        RelativeLayout reMessageListItem;

        public MessageHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(v, getPosition());
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
