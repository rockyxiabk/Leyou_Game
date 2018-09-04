package com.leyou.game.ipresenter.friend;

import com.leyou.game.dao.Crowd;
import com.leyou.game.dao.Friend;

import java.util.List;

/**
 * Description : com.leyou.game.ipresenter
 *
 * @author : rocky
 * @Create Time : 2017/8/3 上午11:40
 * @Modified Time : 2017/8/3 上午11:40
 */
public interface ICrowdDetailActivity {

    void showCrowdMemberAdapter(List<Friend> friends);

    void setCrowdInfo(Crowd crowd);

    void showMessageToast(String msg);

}
