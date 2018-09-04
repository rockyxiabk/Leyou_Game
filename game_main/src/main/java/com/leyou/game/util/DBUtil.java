package com.leyou.game.util;

import android.content.Context;
import android.text.TextUtils;

import com.leyou.game.dao.Crowd;
import com.leyou.game.dao.CrowdDao;
import com.leyou.game.dao.DaoMaster;
import com.leyou.game.dao.DaoSession;
import com.leyou.game.dao.Friend;
import com.leyou.game.dao.FriendDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Description : 数据库单例类
 *
 * @author : rocky
 * @Create Time : 2017/8/5 上午10:54
 * @Modified Time : 2017/8/5 上午10:54
 */
public class DBUtil {
    private static final String TAG = "DBUtil";
    private static Context mContext;
    private static DBUtil instance;
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;

    private DBUtil() {
    }

    public static DBUtil getInstance(Context context) {
        if (instance == null) {
            instance = new DBUtil();
            if (mContext == null) {
                mContext = context;
            }
            daoSession = getDaoSession(mContext);
        }
        return instance;
    }

    private static DaoMaster getDaoMaster(Context context) {
        if (daoMaster == null) {
            DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(context, "game.db", null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }

    private static DaoSession getDaoSession(Context context) {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster(context);
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

    public void insertFriend(Friend friend) {
        daoSession.clear();
        long insert = 0l;
        Friend unique = daoSession.getFriendDao().queryBuilder().where(FriendDao.Properties.Phone.eq(friend.getPhone())).limit(1).unique();
        if (null == unique) {
            friend.setId(null);
            insert = daoSession.getFriendDao().insert(friend);
        } else {
            insert = unique.getId();
        }
        LogUtil.d(TAG, "Friend insert:" + insert);
    }

    public void insertCrowd(Crowd crowd) {
        daoSession.clear();
        long insert = 0l;
        Crowd unique = daoSession.getCrowdDao().queryBuilder().where(CrowdDao.Properties.CrowdId.eq(crowd.getCrowdId())).limit(1).unique();
        if (null == unique) {
            insert = daoSession.getCrowdDao().insert(crowd);
        } else {
            insert = unique.getId();
        }
        LogUtil.d(TAG, "Crowd insert:" + insert);
    }

    public void deleteFriend(Friend friend) {
        daoSession.clear();
        Friend unique = daoSession.getFriendDao().queryBuilder().where(FriendDao.Properties.Phone.eq(friend.getPhone())).limit(1).unique();
        if (null != unique)
            daoSession.getFriendDao().delete(friend);
    }

    public void deleteCrowd(Crowd crowd) {
        daoSession.clear();
        Crowd unique = daoSession.getCrowdDao().queryBuilder().where(CrowdDao.Properties.CrowdId.eq(crowd.getCrowdId())).limit(1).unique();
        if (null != unique)
            daoSession.getCrowdDao().delete(crowd);
    }

    public void updateFriend(Friend friend) {
        daoSession.clear();
        Friend unique = daoSession.getFriendDao().queryBuilder().where(FriendDao.Properties.Phone.eq(friend.getPhone())).limit(1).unique();
        if (null != unique && friend.getPhone().equalsIgnoreCase(unique.getPhone())) {
            friend.setId(unique.getId());
            daoSession.getFriendDao().update(friend);
            LogUtil.d(TAG, "Friend update" + friend.toString());
        } else {
            insertFriend(friend);
        }
    }

    public void updateCrowd(Crowd crowd) {
        daoSession.clear();
        Crowd unique = daoSession.getCrowdDao().queryBuilder().where(CrowdDao.Properties.CrowdId.eq(crowd.getCrowdId())).limit(1).unique();
        if (null != unique && crowd.getCrowdId().equalsIgnoreCase(unique.getCrowdId())) {
            crowd.setId(unique.getId());
            daoSession.getCrowdDao().update(crowd);
            LogUtil.d(TAG, "Crowd update" + crowd.toString());
        } else {
            insertCrowd(crowd);
        }
    }

    public List<Friend> queryFriendList() {
        daoSession.clear();
        List<Friend> list = daoSession.getFriendDao().queryBuilder().whereOr(FriendDao.Properties.Status.eq(0), FriendDao.Properties.Status.eq(3), FriendDao.Properties.Status.eq(4)).orderDesc(FriendDao.Properties.Status).list();
        return list;
    }

    public List<Friend> queryMyFriendList() {
        daoSession.clear();
        List<Friend> list = daoSession.getFriendDao().queryBuilder().where(FriendDao.Properties.Status.eq(Friend.FRIEND)).orderDesc(FriendDao.Properties.Status).list();
        return list;
    }

    public List<Friend> queryNewFriendList() {
        daoSession.clear();
        List<Friend> list = new ArrayList<>();
        List<Friend> list1 = daoSession.getFriendDao().queryBuilder().where(FriendDao.Properties.Source.eq(Friend.FRIEND)).orderDesc(FriendDao.Properties.Status).list();
        list.addAll(list1);
        return list;
    }

    public void clearFriend() {
        daoSession.clear();
        daoSession.getFriendDao().deleteAll();
    }

    public void clearCrowd() {
        daoSession.clear();
        daoSession.getCrowdDao().deleteAll();
    }

    public void clearAllData() {
        clearCrowd();
        clearFriend();
    }

    public Friend queryFriendByUserId(String userId) {
        daoSession.clear();
        Friend friend = daoSession.getFriendDao().queryBuilder().where(FriendDao.Properties.UserId.eq(userId)).limit(1).unique();
        return friend;
    }

    public Crowd queryCrowdByCrowdId(String crowdId) {
        daoSession.clear();
        Crowd crowd = daoSession.getCrowdDao().queryBuilder().where(CrowdDao.Properties.CrowdId.eq(crowdId)).limit(1).unique();
        return crowd;
    }

    public void updateFriendList(List<Friend> friendList) {
        for (int i = 0; i < friendList.size(); i++) {
            updateFriend(friendList.get(i));
        }
    }
}
