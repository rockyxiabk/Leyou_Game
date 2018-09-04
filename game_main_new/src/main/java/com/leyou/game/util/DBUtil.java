package com.leyou.game.util;

import android.content.Context;
import android.text.TextUtils;

import com.google.zxing.common.StringUtils;
import com.leyou.game.dao.Crowd;
import com.leyou.game.dao.CrowdDao;
import com.leyou.game.dao.DaoMaster;
import com.leyou.game.dao.DaoSession;
import com.leyou.game.dao.Friend;
import com.leyou.game.dao.FriendDao;
import com.leyou.game.dao.PhoneContact;
import com.leyou.game.dao.PhoneContactDao;
import com.leyou.game.dao.SearchHistory;
import com.leyou.game.dao.SearchHistoryDao;

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
        Friend unique = daoSession.getFriendDao().queryBuilder()
                .where(FriendDao.Properties.Phone.eq(friend.getPhone())).limit(1).unique();
        if (null == unique) {
            friend.setId(System.currentTimeMillis());
            String pinyin = PinyinUtil.getPingYin(!TextUtils.isEmpty(friend.getComment()) ?
                    friend.getComment() : !TextUtils.isEmpty(friend.getName()) ?
                    friend.getName() : !TextUtils.isEmpty(friend.getNickname()) ?
                    friend.getNickname() : "*");
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {
                friend.setPhoneNameLetter(sortString.toUpperCase());
            } else if (sortString.matches("[0-9]")) {
                friend.setPhoneNameLetter("*");
            } else {
                friend.setPhoneNameLetter("#");
            }
            insert = daoSession.getFriendDao().insert(friend);
        } else {
            insert = unique.getId();
        }
        LogUtil.d(TAG, "Friend insert:" + insert);
    }

    public void insertPhoneContact(PhoneContact phoneContact) {
        daoSession.clear();
        long insert = 0l;
        PhoneContact contact = daoSession.getPhoneContactDao().queryBuilder()
                .where(PhoneContactDao.Properties.Phone.eq(phoneContact.getPhone())).limit(1).unique();
        if (null == contact) {
            phoneContact.setId(System.currentTimeMillis());
            String pinyin = PinyinUtil.getPingYin(phoneContact.getName());
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {
                phoneContact.setPhoneNameLetter(sortString.toUpperCase());
            } else if (sortString.matches("[0-9]")) {
                phoneContact.setPhoneNameLetter("*");
            } else {
                phoneContact.setPhoneNameLetter("#");
            }
            insert = daoSession.getPhoneContactDao().insert(phoneContact);
        } else {
            insert = contact.getId();
        }
        LogUtil.d(TAG, "PhoneContact insert:" + insert);
    }

    public void insertCrowd(Crowd crowd) {
        daoSession.clear();
        long insert = 0l;
        Crowd unique = daoSession.getCrowdDao().queryBuilder()
                .where(CrowdDao.Properties.GroupId.eq(crowd.getGroupId())).limit(1).unique();
        if (null == unique) {
            insert = daoSession.getCrowdDao().insert(crowd);
        } else {
            insert = unique.getId();
        }
        LogUtil.d(TAG, "Crowd insert:" + insert);
    }

    public void insertSearchHistory(SearchHistory searchHistory) {
        daoSession.clear();
        long insert = 0l;
        SearchHistory unique = daoSession.getSearchHistoryDao().queryBuilder()
                .where(SearchHistoryDao.Properties.KeyWord.eq(searchHistory.getKeyWord())).limit(1).unique();
        if (null == unique) {
            searchHistory.setTimeStamp(System.currentTimeMillis());
            insert = daoSession.getSearchHistoryDao().insert(searchHistory);
        } else {
            unique.setTimeStamp(System.currentTimeMillis());
            daoSession.update(unique);
            insert = 100;
        }
        LogUtil.d(TAG, "SearchHistory insert:" + insert);
    }

    public void deleteFriend(Friend friend) {
        daoSession.clear();
        Friend unique = daoSession.getFriendDao().queryBuilder().where(FriendDao.Properties.Phone.eq(friend.getPhone())).limit(1).unique();
        if (null != unique) {
            daoSession.getFriendDao().delete(friend);
        }
    }

    public void deletePhoneContact(String phone) {
        daoSession.clear();
        PhoneContact unique = daoSession.getPhoneContactDao().queryBuilder().where(PhoneContactDao.Properties.Phone.eq(phone)).limit(1).unique();
        if (null != unique) {
            daoSession.getPhoneContactDao().delete(unique);
        }
    }

    public void deleteCrowd(String crowdId) {
        daoSession.clear();
        Crowd unique = daoSession.getCrowdDao().queryBuilder().where(CrowdDao.Properties.GroupId.eq(crowdId)).limit(1).unique();
        if (null != unique)
            daoSession.getCrowdDao().delete(unique);
    }

    public void updateFriend(Friend friend) {
        daoSession.clear();
        Friend unique = daoSession.getFriendDao().queryBuilder().where(FriendDao.Properties.Phone.eq(friend.getPhone())).limit(1).unique();
        if (null != unique && friend.getPhone().equalsIgnoreCase(unique.getPhone())) {
            friend.setId(unique.getId());
            String pinyin = PinyinUtil.getPingYin(!TextUtils.isEmpty(friend.getComment()) ?
                    friend.getComment() : !TextUtils.isEmpty(friend.getName()) ?
                    friend.getName() : !TextUtils.isEmpty(friend.getNickname()) ?
                    friend.getNickname() : "*");
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {
                friend.setPhoneNameLetter(sortString.toUpperCase());
            } else if (sortString.matches("[0-9]")) {
                friend.setPhoneNameLetter("*");
            } else {
                friend.setPhoneNameLetter("#");
            }
            daoSession.getFriendDao().update(friend);
            LogUtil.d(TAG, "Friend update" + friend.toString());
        } else {
            insertFriend(friend);
        }
    }

    public void updatePhoneContact(PhoneContact phoneContact) {
        daoSession.clear();
        PhoneContact unique = daoSession.getPhoneContactDao().queryBuilder()
                .where(PhoneContactDao.Properties.Phone.eq(phoneContact.getPhone())).limit(1).unique();
        if (null != unique && phoneContact.getPhone().equalsIgnoreCase(unique.getPhone())) {
            phoneContact.setId(unique.getId());
            phoneContact.setName(unique.getName());
            String pinyin = PinyinUtil.getPingYin(!TextUtils.isEmpty(phoneContact.getName()) ?
                    phoneContact.getName() : !TextUtils.isEmpty(phoneContact.getNickname()) ? phoneContact.getNickname() : "P");
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {
                phoneContact.setPhoneNameLetter(sortString.toUpperCase());
            } else if (sortString.matches("[0-9]")) {
                phoneContact.setPhoneNameLetter("*");
            } else {
                phoneContact.setPhoneNameLetter("#");
            }
            daoSession.getPhoneContactDao().update(phoneContact);
            LogUtil.d(TAG, "PhoneContact update" + phoneContact.toString());
        } else {
            insertPhoneContact(phoneContact);
        }
    }

    public void updateMyFriend(Friend friend) {
        daoSession.clear();
        Friend unique = daoSession.getFriendDao().queryBuilder().where(FriendDao.Properties.Phone.eq(friend.getPhone())).limit(1).unique();
        if (null != unique && friend.getPhone().equalsIgnoreCase(unique.getPhone())) {
            friend.setId(unique.getId());
            friend.setStatus(Friend.FRIEND);
            String pinyin = PinyinUtil.getPingYin(!TextUtils.isEmpty(friend.getComment()) ?
                    friend.getComment() : !TextUtils.isEmpty(friend.getName()) ?
                    friend.getName() : !TextUtils.isEmpty(friend.getNickname()) ?
                    friend.getNickname() : "*");
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {
                friend.setPhoneNameLetter(sortString.toUpperCase());
            } else if (sortString.matches("[0-9]")) {
                friend.setPhoneNameLetter("*");
            } else {
                friend.setPhoneNameLetter("#");
            }
            daoSession.getFriendDao().update(friend);
            LogUtil.d(TAG, "Friend update" + friend.toString());
        } else {
            insertFriend(friend);
        }
    }

    public void updateCrowd(Crowd crowd) {
        daoSession.clear();
        Crowd unique = daoSession.getCrowdDao().queryBuilder()
                .where(CrowdDao.Properties.GroupId.eq(crowd.getGroupId())).limit(1).unique();
        if (null != unique && crowd.getGroupId().equalsIgnoreCase(unique.getGroupId())) {
            crowd.setId(unique.getId());
            daoSession.getCrowdDao().update(crowd);
            LogUtil.d(TAG, "Crowd update" + crowd.toString());
        } else {
            insertCrowd(crowd);
        }
    }

    public List<PhoneContact> queryPhoneContactList() {
        daoSession.clear();
        List<PhoneContact> list = daoSession.getPhoneContactDao().queryBuilder().
                whereOr(PhoneContactDao.Properties.Status.eq(PhoneContact.NO_SYSTEM)
                        , PhoneContactDao.Properties.Status.eq(PhoneContact.SYSTEM_NO_FRIEND)
                        , PhoneContactDao.Properties.Status.eq(PhoneContact.ADDING_WAITING_CONFIRM))
                .orderDesc(PhoneContactDao.Properties.Status).list();
        return list;
    }

    public List<Friend> queryMyFriendList() {
        daoSession.clear();
        List<Friend> list = daoSession.getFriendDao().queryBuilder()
                .where(FriendDao.Properties.Status.eq(Friend.FRIEND)).orderDesc(FriendDao.Properties.Status).list();
        return list;
    }

    public List<PhoneContact> queryNewFriendList() {
        daoSession.clear();
        List<PhoneContact> list = daoSession.getPhoneContactDao().queryBuilder()
                .whereOr(PhoneContactDao.Properties.Status.eq(PhoneContact.ADDING_WAITING_CONFIRM),
                        PhoneContactDao.Properties.Status.eq(PhoneContact.ADDING_FRIEND_PASS_VERIFY))
                .orderDesc(PhoneContactDao.Properties.Status).list();
        return list;
    }

    public List<SearchHistory> queryHistorySearchAuto() {
        daoSession.clear();
        List<SearchHistory> list = daoSession.getSearchHistoryDao().queryBuilder()
                .orderDesc(SearchHistoryDao.Properties.TimeStamp).limit(15).list();
        return list;
    }

    public List<SearchHistory> queryHistorySearch(String keyWord) {
        daoSession.clear();
        List<SearchHistory> list = daoSession.getSearchHistoryDao().queryBuilder()
                .where(SearchHistoryDao.Properties.KeyWord.like("%" + keyWord + "%")).list();
        return list;
    }

    public Friend queryFriendByUserId(String userId) {
        daoSession.clear();
        Friend friend = daoSession.getFriendDao().queryBuilder().where(FriendDao.Properties.UserId.eq(userId)).limit(1).unique();
        return friend;
    }

    public PhoneContact queryPhoneContactByUserId(String userId) {
        daoSession.clear();
        PhoneContact phoneContact = daoSession.getPhoneContactDao().queryBuilder().where(PhoneContactDao.Properties.UserId.eq(userId)).limit(1).unique();
        return phoneContact;
    }

    public Crowd queryCrowdByCrowdId(String crowdId) {
        daoSession.clear();
        Crowd crowd = daoSession.getCrowdDao().queryBuilder().where(CrowdDao.Properties.GroupId.eq(crowdId)).limit(1).unique();
        return crowd;
    }

    public SearchHistory querySearchHistoryByKeyWord(String keyWord) {
        daoSession.clear();
        SearchHistory crowd = daoSession.getSearchHistoryDao().queryBuilder()
                .where(SearchHistoryDao.Properties.KeyWord.eq(keyWord))
                .orderDesc(SearchHistoryDao.Properties.TimeStamp).limit(1).unique();
        return crowd;
    }

    public void updateFriendList(List<Friend> friendList) {
        for (int i = 0; i < friendList.size(); i++) {
            updateFriend(friendList.get(i));
        }
    }

    public void updatePhoneContactList(List<PhoneContact> friendList) {
        for (int i = 0; i < friendList.size(); i++) {
            updatePhoneContact(friendList.get(i));
        }
    }

    public void clearFriend() {
        daoSession.clear();
        daoSession.getFriendDao().deleteAll();
    }

    public void clearPhoneContact() {
        daoSession.clear();
        daoSession.getPhoneContactDao().deleteAll();
    }

    public void clearCrowd() {
        daoSession.clear();
        daoSession.getCrowdDao().deleteAll();
    }

    public void clearSearch() {
        daoSession.clear();
        daoSession.getSearchHistoryDao().deleteAll();
    }

    public void clearAllData() {
        clearCrowd();
        clearPhoneContact();
        clearFriend();
        clearSearch();
    }
}
