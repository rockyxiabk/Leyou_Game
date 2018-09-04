package com.leyou.game.widget.dialog;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.adapter.CustomItemClickListener;
import com.leyou.game.adapter.friend.FriendAdapter;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.dao.Friend;
import com.leyou.game.util.DBUtil;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.util.newapi.FriendApi;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

/**
 * Description : com.leyou.game.widget.dialog
 *
 * @author : rocky
 * @Create Time : 2017/12/7 下午12:13
 * @Modified Time : 2017/12/7 下午12:13
 */
public class RecommendFriendDialog extends BaseDialog implements CustomItemClickListener {
    private static final String TAG = "RecommendFriendDialog";
    private Context context;
    @BindView(R.id.recycler_recommend)
    RecyclerView recyclerRecommend;
    @BindView(R.id.btn_ignore)
    Button btnIgnore;
    private List<Friend> friendList = new ArrayList<>();
    private FriendAdapter friendAdapter;

    public RecommendFriendDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_recommend_friend;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        friendAdapter = new FriendAdapter(context, friendList);
        friendAdapter.setCustomItemClickListener(this);
        recyclerRecommend.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerRecommend.setAdapter(friendAdapter);

        initData();
    }

    private void initData() {
        HttpUtil.subscribe(HttpUtil.createApi(FriendApi.class, Constants.URL).getPotentialFriends(), new Observer<ResultArray<Friend>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                dismiss();
                e.printStackTrace();
                LogUtil.e(TAG, e.toString());
            }

            @Override
            public void onNext(ResultArray<Friend> friendResultArray) {
                if (friendResultArray.result == 1) {
                    List<Friend> friends = friendResultArray.data;
                    friendList.clear();
                    friendList.addAll(friends);
                    friendAdapter.setAdapterData(friends);
                } else {
                    dismiss();
                }
            }
        });
    }

    public void addFriend(final Friend friend) {
        HttpUtil.subscribe(HttpUtil.createApi(FriendApi.class, Constants.URL).addFriend(friend.getPhone()), new Observer<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                LogUtil.e(TAG, e.toString());
                ToastUtils.showToastShort("添加好友失败");
            }

            @Override
            public void onNext(Result result) {
                if (result.result == 1) {
                    ToastUtils.showToastShort("等待好友确认");
                    initData();
                } else {
                    ToastUtils.showToastShort(result.msg);
                }
            }
        });
    }

    @OnClick(R.id.btn_ignore)
    public void onViewClicked() {
        dismiss();
    }

    @Override
    public void onItemClick(View view, int position) {
        Friend friend = friendList.get(position);
        addFriend(friend);
    }
}
