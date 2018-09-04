package com.leyou.game.adapter.game;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.leyou.game.R;
import com.leyou.game.activity.PlayGameActivity;
import com.leyou.game.bean.UserData;
import com.leyou.game.bean.game.GameBean;
import com.leyou.game.widget.dialog.LogInDialog;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description : com.leyou.game.adapter.game
 *
 * @author : rocky
 * @Create Time : 2017/10/18 下午3:51
 * @Modified Time : 2017/10/18 下午3:51
 */
public class BannerViewHolder implements MZViewHolder<GameBean> {
    @BindView(R.id.iv_banner_img)
    SimpleDraweeView ivBannerImg;

    @Override
    public View createView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_game_banner_item, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onBind(final Context context, int i, final GameBean gameBean) {
        ivBannerImg.setImageURI(gameBean.bannerUrl);
        ivBannerImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserData.getInstance().isLogIn()) {
                    Intent intent = new Intent(context, PlayGameActivity.class);
                    intent.putExtra("game", gameBean);
                    context.startActivity(intent);
                } else {
                    new LogInDialog(context, false).show();
                }
            }
        });
    }
}