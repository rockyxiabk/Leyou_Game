package com.leyou.game.adapter.friend;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.leyou.game.R;
import com.leyou.game.activity.friend.FriendDetailActivity;
import com.leyou.game.adapter.CustomItemClickListener;
import com.leyou.game.dao.Friend;
import com.leyou.game.util.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imlib.model.UserInfo;

/**
 * Description :新的好友
 *
 * @author : rocky
 * @Create Time : 2017/7/15 下午3:53
 * @Modified Time : 2017/7/15 下午3:53
 */
public class NewContactsAdapter extends RecyclerView.Adapter<NewContactsAdapter.ViewHolder> implements SectionIndexer {
    private Context context;
    private List<Friend> list;
    private CustomItemClickListener listener;

    public NewContactsAdapter(Context context, List<Friend> list) {
        this.context = context;
        this.list = list;
    }

    public void setCustomItemClickListener(CustomItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = inflate(parent, R.layout.layout_new_friend_contacts_list);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    private View inflate(ViewGroup parent, int layoutRes) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        final Friend contactBean = list.get(position);
        holder.tvContactName.setText(!TextUtils.isEmpty(contactBean.getName()) ? contactBean.getName() : contactBean.getNickName());
        holder.ivContactHeader.setImageURI(contactBean.getPictureUrl());

        int section = getSectionForPosition(position);
        if (position == getPositionForSection(section)) {
            holder.tvCategory.setVisibility(View.VISIBLE);
            holder.tvCategory.setText(contactBean.getPhoneNameLetter());
        } else {
            holder.tvCategory.setVisibility(View.GONE);
        }
        if (contactBean.getStatus() == 1) {
            holder.btnInvite.setVisibility(View.GONE);
            holder.tvInviteStatus.setVisibility(View.VISIBLE);
            holder.tvInviteStatus.setText("已通过");
        } else if (contactBean.getStatus() == 2) {
            holder.btnInvite.setVisibility(View.VISIBLE);
            holder.tvInviteStatus.setVisibility(View.GONE);
        } else if (contactBean.getStatus() == 4) {
            holder.btnInvite.setVisibility(View.GONE);
            holder.tvInviteStatus.setVisibility(View.VISIBLE);
        }
        holder.btnInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(null, position);
            }
        });
        holder.reContactItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FriendDetailActivity.class);
                intent.putExtra("userId", contactBean.getUserId());
                UserInfo userInfo = new UserInfo(contactBean.getUserId(), contactBean.getNickName(), Uri.parse(contactBean.getPictureUrl()));
                intent.putExtra("userInfo", userInfo);
                context.startActivity(intent);
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

    @Override
    public Object[] getSections() {
        return null;
    }

    @Override
    public int getPositionForSection(int section) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = list.get(i).getPhoneNameLetter();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {
        return list.get(position).getPhoneNameLetter().charAt(0);
    }

    public void setAdapterData(List<Friend> data) {
        if (null != data && data.size() > 0) {
            list.clear();
            list.addAll(data);
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_category)
        TextView tvCategory;
        @BindView(R.id.re_contact_item)
        RelativeLayout reContactItem;
        @BindView(R.id.iv_contact_header)
        SimpleDraweeView ivContactHeader;
        @BindView(R.id.tv_contact_name)
        TextView tvContactName;
        @BindView(R.id.btn_invite)
        Button btnInvite;
        @BindView(R.id.tv_invite_status)
        TextView tvInviteStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
