package com.leyou.game.adapter.friend;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.leyou.game.R;
import com.leyou.game.bean.UserData;
import com.leyou.game.dao.Friend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description : com.leyou.game.adapter.friend
 *
 * @author : rocky
 * @Create Time : 2017/7/22 下午4:28
 * @Modified Time : 2017/7/22 下午4:28
 */
public class FriendCreateCrowdAdapter extends RecyclerView.Adapter<FriendCreateCrowdAdapter.ViewHolder> implements SectionIndexer {
    private Context context;
    private List<Friend> list;
    private Map<Integer, Boolean> map = new HashMap<>();
    private onMapSelectorListener listener;

    public FriendCreateCrowdAdapter(Context context, List<Friend> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnMapSelectorListener(onMapSelectorListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_friend_create_crowd_list, parent, false);
        ViewHolder holder = new ViewHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Friend contactBean = list.get(position);
        holder.tvContactName.setText(!TextUtils.isEmpty(contactBean.getComment()) ?
                contactBean.getComment() : !TextUtils.isEmpty(contactBean.getName()) ?
                contactBean.getName() : contactBean.getNickname());
        int section = getSectionForPosition(position);
        if (position == getPositionForSection(section)) {
            holder.tvCategory.setVisibility(View.VISIBLE);
            holder.tvCategory.setText(contactBean.getPhoneNameLetter());
        } else {
            holder.tvCategory.setVisibility(View.GONE);
        }
        holder.ivContactHeader.setImageURI(contactBean.getHeadImgUrl());
        holder.checkBox.setChecked(map.get(position));
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(contactBean.getPhone()) || !contactBean.getPhone().
                        equalsIgnoreCase(UserData.getInstance().getPhoneNum())) {
                    setMap(position);
                    holder.checkBox.setChecked(map.get(position));
                }
            }
        });
        holder.reItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(contactBean.getPhone()) || !contactBean.getPhone()
                        .equalsIgnoreCase(UserData.getInstance().getPhoneNum())) {
                    setMap(position);
                    holder.checkBox.setChecked(map.get(position));
                }
            }
        });
    }

    private void setMap(int position) {
        Boolean isChecked = map.get(position);
        map.put(position, !isChecked);
        listener.showSelectorContacts(map);
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

    public void setContactsAdapter(List<Friend> beanList) {
        if (null != beanList && beanList.size() > 0) {
            this.list = beanList;
            resetMap();
        }
        notifyDataSetChanged();
    }

    private void resetMap() {
        for (int i = 0; i < list.size(); i++) {
            Friend contactBean = list.get(i);
            if (!TextUtils.isEmpty(contactBean.getPhone()) && contactBean.getPhone().contains(UserData.getInstance().getPhoneNum())) {
                map.put(i, true);
            } else {
                map.put(i, false);
            }
        }
        listener.showSelectorContacts(map);
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
        @BindView(R.id.iv_contact_header)
        SimpleDraweeView ivContactHeader;
        @BindView(R.id.tv_contact_name)
        TextView tvContactName;
        @BindView(R.id.cb)
        CheckBox checkBox;
        @BindView(R.id.re_item)
        RelativeLayout reItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface onMapSelectorListener {
        void showSelectorContacts(Map<Integer, Boolean> map);
    }
}
