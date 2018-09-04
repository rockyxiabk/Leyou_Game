package com.leyou.game.widget.dialog.treasury;

import android.content.Context;
import android.os.Handler;

import com.leyou.game.R;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.bean.treasure.TreasureBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifImageView;

/**
 * Description : com.leyou.game.widget.dialog
 *
 * @author : rocky
 * @Create Time : 2017/6/21 下午6:52
 * @Modified Time : 2017/6/21 下午6:52
 */
public class FightDialog extends BaseDialog {
    private TreasureBean treasure;
    private int harvest;
    private Context context;
    private boolean result;
    @BindView(R.id.iv_fight)
    GifImageView ivFight;
    private Handler handler = new Handler();
    private FightListener fightListener;

    public void setFightListener(FightListener fightListener) {
        this.fightListener = fightListener;
    }

    public FightDialog(Context context, boolean flag, TreasureBean treasure, int harvest) {
        super(context);
        this.context = context;
        this.result = flag;
        this.treasure = treasure;
        this.harvest = harvest;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_fight;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        if (harvest == 1) {
            if (result) {
                ivFight.setImageResource(R.mipmap.icon_treasure_fight_person_success);
            } else {
                ivFight.setImageResource(R.mipmap.icon_treasure_fight_person_failed);
            }
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    fightListener.sendState(result);
                    dismiss();
                }
            }, 4000);
        } else {
            switch (treasure.level) {
                case 1:
                    if (result) {
                        ivFight.setImageResource(R.mipmap.icon_treasure_fight_level_one_success);
                    } else {
                        ivFight.setImageResource(R.mipmap.icon_treasure_fight_level_one_failed);
                    }
                    break;
                case 2:
                    if (result) {
                        ivFight.setImageResource(R.mipmap.icon_treasure_fight_level_two_success);
                    } else {
                        ivFight.setImageResource(R.mipmap.icon_treasure_fight_level_two_failed);
                    }
                    break;
                case 3:
                    if (result) {
                        ivFight.setImageResource(R.mipmap.icon_treasure_fight_level_three_success);
                    } else {
                        ivFight.setImageResource(R.mipmap.icon_treasure_fight_level_three_failed);
                    }
                    break;
                case 4:
                    if (result) {
                        ivFight.setImageResource(R.mipmap.icon_treasure_fight_level_four_success);
                    } else {
                        ivFight.setImageResource(R.mipmap.icon_treasure_fight_level_four_failed);
                    }
                    break;
                case 5:
                    if (result) {
                        ivFight.setImageResource(R.mipmap.icon_treasure_fight_level_five_success);
                    } else {
                        ivFight.setImageResource(R.mipmap.icon_treasure_fight_level_five_failed);
                    }
                    break;
            }
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    fightListener.sendState(result);
                    dismiss();
                }
            }, 4000);
        }
    }

    public interface FightListener {
        void sendState(boolean flag);
    }
}
