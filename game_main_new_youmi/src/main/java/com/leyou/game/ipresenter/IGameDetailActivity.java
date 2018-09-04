package com.leyou.game.ipresenter;

import com.leyou.game.bean.game.GameBean;
import com.leyou.game.bean.game.GameCommentBean;

import java.util.List;

/**
 * Description : com.leyou.game.ipresenter
 *
 * @author : rocky
 * @Create Time : 2017/10/30 下午7:08
 * @Modified Time : 2017/10/30 下午7:08
 */
public interface IGameDetailActivity {

    void showGameDetail(GameBean gameBean);

    void showCommentListData(List<GameCommentBean> list);

    void showMessage(String msg);
}
