package com.leyou.game.adapter.friend;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.leyou.game.R;
import com.leyou.game.bean.ContactBean;
import com.leyou.game.dao.Friend;
import com.leyou.game.util.LogUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description : com.leyou.game.adapter.friend
 *
 * @author : rocky
 * @Create Time : 2017/7/22 下午4:59
 * @Modified Time : 2017/7/22 下午4:59
 */
public class FriendChoseAdapter extends RecyclerView.Adapter<FriendChoseAdapter.ViewHolder> {
    private Context context;
    private List<Friend> list;

    public FriendChoseAdapter(Context context, List<Friend> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_friend_chose_friend_list, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Friend friend = list.get(position);
        holder.ivContactsHeader.setImageURI(friend.getPictureUrl());
    }

    @Override
    public int getItemCount() {
        int ret = 0;
        if (null != list && list.size() > 0) {
            ret = list.size();
        }
        return ret;
    }

    public void setChoseAdapter(List<Friend> currentList) {
        if (null != currentList && currentList.size() > 0) {
            this.list = currentList;
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_contacts_header)
        SimpleDraweeView ivContactsHeader;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
