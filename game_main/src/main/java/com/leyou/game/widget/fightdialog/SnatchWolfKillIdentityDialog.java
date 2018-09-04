package com.leyou.game.widget.fightdialog;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.leyou.game.R;
import com.leyou.game.adapter.CustomItemClickListener;
import com.leyou.game.adapter.wolfkill.ChoosePropAdapter;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.bean.WolfPropBean;
import com.leyou.game.ipresenter.fight.ISnatchPropDialog;
import com.leyou.game.presenter.game.SnatchPropDialogPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description :狼人杀抢身份
 *
 * @author : rocky
 * @Create Time : 2017/8/1 下午7:33
 * @Modified Time : 2017/8/1 下午7:33
 */
public class SnatchWolfKillIdentityDialog extends BaseDialog implements ISnatchPropDialog, CustomItemClickListener {
    private Context context;
    private long roomId;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    private List<WolfPropBean> list = new ArrayList<>();
    private ChoosePropAdapter adapter;
    private SnatchPropDialogPresenter presenter;
    private CountDownTimer timer = new CountDownTimer(5000, 100) {
        @Override
        public void onTick(long millisUntilFinished) {
            tvTitle.setText("抢角色(" + millisUntilFinished / 1000 + ")");
        }

        @Override
        public void onFinish() {
            tvTitle.setText("抢角色");
            dismiss();
        }
    };

    public SnatchWolfKillIdentityDialog(Context context, long roomId) {
        super(context);
        this.context = context;
        this.roomId = roomId;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_wolf_kill_snatch_identity;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);

        tvTitle.setText("抢角色(5)");

        adapter = new ChoosePropAdapter(context, list);
        adapter.setCustomItemClick(this);
        recycler.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recycler.setAdapter(adapter);

        presenter = new SnatchPropDialogPresenter(context, this);
    }

    @OnClick(R.id.iv_close)
    public void onViewClicked() {
        dismiss();
    }

    @Override
    public void onItemClick(View view, int position) {
        WolfPropBean wolfPropBean = list.get(position);
        if (null != wolfPropBean) {
            presenter.useProp(roomId, wolfPropBean.mark);
        }
    }

    @Override
    public void showPropData(List<WolfPropBean> data) {
        if (null != data && data.size() > 0) {
            list.clear();
            list.addAll(data);
            adapter.setPropAdapter(data);
            timer.start();
        } else {
            dismiss();
        }
    }

    @Override
    public void showUsePropResult(boolean result) {
        timer.onFinish();
        if (result) {
            dismiss();
        } else {

        }
    }
}
