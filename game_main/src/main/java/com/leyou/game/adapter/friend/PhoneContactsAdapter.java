package com.leyou.game.adapter.friend;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.leyou.game.R;
import com.leyou.game.adapter.CustomItemClickListener;
import com.leyou.game.dao.Friend;
import com.leyou.game.util.LogUtil;
import com.umeng.socialize.utils.Log;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description : 手机通讯录
 *
 * @author : rocky
 * @Create Time : 2017/7/15 下午3:53
 * @Modified Time : 2017/7/15 下午3:53
 */
public class PhoneContactsAdapter extends RecyclerView.Adapter<PhoneContactsAdapter.ViewHolder> implements SectionIndexer {
    private Context context;
    private List<Friend> list;
    public CustomItemClickListener listener;

    public PhoneContactsAdapter(Context context, List<Friend> list) {
        this.context = context;
        this.list = list;
    }

    public void setCustomItemClickListener(CustomItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_friend_contacts_list, parent, false);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Friend contactBean = list.get(position);
        holder.tvContactName.setText(contactBean.getName());
        int index = 0;
        if (!TextUtils.isEmpty(contactBean.getName()) && contactBean.getName().length() > 1) {
            String subName = contactBean.getName().substring(index, index + 1);
            holder.tvNameHeader.setText(subName);
        } else {
            holder.tvNameHeader.setText(contactBean.getPhoneNameLetter());
        }
        LogUtil.d("Tag", "-----Friend:" + contactBean.toString());
        holder.btnInvite.setVisibility(View.VISIBLE);
        holder.btnInvite.setEnabled(true);
        switch (contactBean.getStatus()) {
            case Friend.NO_SYSTEM:
                holder.btnInvite.setText("邀请");
                break;
            case Friend.SYSTEM_NO_FRIEND:
                holder.btnInvite.setText("添加");
                break;
            case Friend.ADDING_FRIEND:
            case Friend.ADDING_WAITING_CONFIRM:
                holder.btnInvite.setText("等待验证");
                holder.btnInvite.setEnabled(false);
                break;
            default:
                holder.btnInvite.setVisibility(View.GONE);
                break;
        }

        int section = getSectionForPosition(position);
        if (position == getPositionForSection(section)) {
            holder.tvCategory.setVisibility(View.VISIBLE);
            holder.tvCategory.setText(contactBean.getPhoneNameLetter());
        } else {
            holder.tvCategory.setVisibility(View.GONE);
        }

        holder.btnInvite.setOnClickListener(new View.OnClickListener() {
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

    public void updateAdapter(List<Friend> friendList) {
        this.list.clear();
        list.addAll(friendList);
        notifyDataSetChanged();
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_category)
        TextView tvCategory;
        @BindView(R.id.tv_contact_header)
        TextView tvNameHeader;
        @BindView(R.id.re_contact_item)
        RelativeLayout reContactItem;
        @BindView(R.id.tv_contact_name)
        TextView tvContactName;
        @BindView(R.id.btn_invite)
        Button btnInvite;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
