package com.leyou.game.bean;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.leyou.game.Constants;
import com.leyou.game.GameApplication;
import com.leyou.game.dao.Friend;
import com.leyou.game.util.DBUtil;
import com.leyou.game.util.PinyinUtil;
import com.leyou.game.util.SPUtil;

/**
 * Description : 用户相关信息
 *
 * @author : rocky
 * @Create Time : 2017/4/19 下午4:21
 * @Modified By: rocky
 * @Modified Time : 2017/4/19 下午4:21
 */
public class UserData {

    private static final String[] str = {
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
            "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
            "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
    };
    private static UserData instance;
    private Context context;
    private boolean isMainProcess = false;
    private UserInfo userInfo = new UserInfo();
    private boolean isLogIn;//用户是否登陆状态
    private boolean isTreasureActivated;//宝库页面时候激活
    private boolean rcIsConnected = false;

    public static UserData getInstance() {
        if (null == instance) {
            instance = new UserData();
        }
        return instance;
    }

    private UserData() {

    }

    public void init(Context context, boolean isMainProcess) {
        this.context = context;
        this.isMainProcess = isMainProcess;
        loadLocalUserInfo();
    }

    /**
     * 加载用户信息，先检查本地是否有userId和loginId如果有，直接读取本地userId和loginId，如果没有则生成userId
     */
    private void loadLocalUserInfo() {
        SharedPreferences sp = context.getSharedPreferences("userData", Context.MODE_PRIVATE);
        userInfo.setId(sp.getString("id", ""));
        userInfo.phone = sp.getString("phone", "");
        userInfo.nickname = sp.getString("nickname", "");
        userInfo.pictureUrl = sp.getString("pictureUrl", "");
        userInfo.deviceID = sp.getString("deviceID", "");
        userInfo.sex = sp.getInt("sex", 0);
        userInfo.birthday = sp.getLong("birthday", 0);
        userInfo.token = sp.getString("token", "");
        if (TextUtils.isEmpty(userInfo.id)) {
            generateUserID();
        }
        if (TextUtils.isEmpty(userInfo.deviceID)) {
            generateDeviceId();
        }
    }

    private void generateDeviceId() {
        if (!isMainProcess) {
            return;
        }
        String imei = Constants.getIMEI();
        if (TextUtils.isEmpty(imei)) {//imei为空
            imei = Constants.getIMSI();
            if (TextUtils.isEmpty(imei)) {//imsi为空
                for (int i = 0; i < 15; i++) {//自定义随机生成10位字符
                    int index = (int) (Math.random() * str.length);
                    imei += str[index];
                }
            }
        }
        userInfo.setDeviceId(imei);
        SharedPreferences userData = context.getSharedPreferences("userData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userData.edit();
        editor.putString("deviceID", userInfo.deviceID);
        editor.apply();
    }

    /**
     * 客户端生成userId
     */
    private void generateUserID() {
        if (!isMainProcess) {
            return;
        }
        String imei = Constants.getIMEI();
        if (TextUtils.isEmpty(imei)) {//imei为空
            imei = Constants.getIMSI();
            if (TextUtils.isEmpty(imei)) {//imsi为空
                for (int i = 0; i < 10; i++) {//自定义随机生成10位字符
                    int index = (int) (Math.random() * str.length);
                    imei += str[index];
                }
            }
        }
        long currentTimeMillis = System.currentTimeMillis();
        userInfo.setId(imei + currentTimeMillis);

        SharedPreferences userData = context.getSharedPreferences("userData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userData.edit();
        editor.putString("id", userInfo.id);
        editor.apply();
    }

    public void saveUserNickNameAndPicture(String nickName, String url) {
        if (!isMainProcess) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences("userData", Context.MODE_PRIVATE);
        sp.edit().putString("pictureUrl", userInfo.pictureUrl)
                .putString("nickname", userInfo.nickname)
                .apply();
        this.userInfo.pictureUrl = url;
        this.userInfo.nickname = nickName;
        this.setLogIn(true);

        updateUserDB();
    }

    public void saveModifyInfo(UserInfo info) {
        if (!isMainProcess) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences("userData", Context.MODE_PRIVATE);
        sp.edit().putInt("sex", info.sex)
                .putString("nickname", info.nickname)
                .putLong("birthday", info.birthday)
                .apply();
        this.userInfo.nickname = info.nickname;
        this.userInfo.birthday = info.birthday;
        this.userInfo.sex = info.sex;

        updateUserDB();
    }

    /**
     * 保存登录信息
     *
     * @param userInfo
     */
    public void saveUserInfo(UserInfo userInfo) {
        if (!isMainProcess) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences("userData", Context.MODE_PRIVATE);
        sp.edit().putString("id", userInfo.id)
                .putString("pictureUrl", userInfo.pictureUrl)
                .putString("nickname", userInfo.nickname)
                .putLong("birthday", userInfo.birthday)
                .putString("phone", userInfo.phone)
                .putInt("sex", userInfo.sex)
                .putString("deviceID", userInfo.deviceID)
                .putString("token", userInfo.token)
                .apply();
        this.userInfo.setId(userInfo.id);
        this.userInfo.pictureUrl = userInfo.pictureUrl;
        this.userInfo.nickname = userInfo.nickname;
        this.userInfo.birthday = userInfo.birthday;
        this.userInfo.sex = userInfo.sex;
        this.userInfo.phone = userInfo.phone;
        this.userInfo.token = userInfo.token;
        this.setLogIn(true);
        GameApplication.bindAlias(userInfo.id);

        updateUserDB();
    }

    private void updateUserDB() {
        Friend friend = new Friend();
        friend.setStatus(Friend.FRIEND);
        String pinyin = PinyinUtil.getPingYin(userInfo.getNickname());
        String sortString = pinyin.substring(0, 1).toUpperCase();
        if (sortString.matches("[A-Z]")) {
            friend.setPhoneNameLetter(sortString);
        } else if (sortString.matches("[0-9]")) {
            friend.setPhoneNameLetter("*");
        } else {
            friend.setPhoneNameLetter("#");
        }
        friend.setNickName(userInfo.nickname);
        friend.setBirthday(userInfo.birthday);
        friend.setSex(userInfo.sex);
        friend.setPhone(userInfo.phone);
        friend.setPictureUrl(userInfo.pictureUrl);
        friend.setUserId(userInfo.id);

        DBUtil.getInstance(context).updateFriend(friend);
    }

    /**
     * 清空用户本地登陆数据 退出登陆
     */
    public boolean clearUserInfo() {

        GameApplication.unBindAlias(userInfo.id);

        if (!isMainProcess) {
            return false;
        }
        setLogIn(false);
        setTreasureActivated(false);
        SharedPreferences sp = context.getSharedPreferences("userData", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.clear();
        setLogIn(false);
        this.userInfo.setId("");
        this.userInfo.pictureUrl = "";
        this.userInfo.nickname = "点击登陆";
        this.userInfo.birthday = System.currentTimeMillis();
        this.userInfo.sex = 0;
        this.userInfo.phone = "";
        this.userInfo.token = "";

        generateUserID();
        generateDeviceId();
        GameApplication.disconnect();
        DBUtil.getInstance(context).clearAllData();
        SPUtil.clearAllSP(context);

        return edit.commit();
    }

    public String getId() {
        if (!isMainProcess) {
            loadLocalUserInfo();
        }
        return null != userInfo.id ? userInfo.id : "";
    }

    public String getDeviceId() {
        if (!isMainProcess) {
            loadLocalUserInfo();
        }
        return null != userInfo.deviceID ? userInfo.deviceID : "";
    }

    public String getPhoneNum() {
        return null != userInfo.phone ? userInfo.phone : "";
    }

    public String getNickname() {
        return null != userInfo.nickname && "" != userInfo.nickname ? userInfo.nickname : "点击登陆";
    }

    public String getPictureUrl() {
        return null != userInfo.pictureUrl ? userInfo.pictureUrl : "";
    }

    public int getDiamonds() {
        return userInfo.virtualCoin;
    }

    public void setDiamonds(int diamonds) {
        userInfo.virtualCoin = diamonds;
    }

    public double getMoney() {
        return userInfo.getMoney();
    }

    public void setMoney(double money) {
        userInfo.money = money;
    }

    public boolean isLogIn() {
        return isLogIn;
    }

    public void setLogIn(boolean logIn) {
        isLogIn = logIn;
    }

    public boolean isTreasureActivated() {
        return isTreasureActivated;
    }

    public void setTreasureActivated(boolean treasureActivated) {
        isTreasureActivated = treasureActivated;
    }

    public int getSex() {
        return userInfo.sex;
    }

    public long getBirthday() {
        return userInfo.birthday;
    }

    public String getToken() {
        return userInfo.token;
    }

    public boolean isRcIsConnected() {
        return rcIsConnected;
    }

    public void setRcIsConnected(boolean rcIsConnected) {
        this.rcIsConnected = rcIsConnected;
    }

    public static class UserInfo {

        public String id;//用户id
        public String phone;//登陆Id 手机号
        public String nickname;//昵称
        public String pictureUrl;//头像地址
        public int sex;//性别（1男or 0女）
        public long birthday; //出生年月日（时间戳形式）

        public int virtualCoin;//钻石
        public double money;//余额现金

        public String deviceID;//手机设备码
        public String token;//融云 tokenId

        public String getId() {
            return id;
        }

        public void setId(String userId) {
            this.id = userId;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getPictureUrl() {
            return pictureUrl;
        }

        public void setPictureUrl(String pictureUrl) {
            this.pictureUrl = pictureUrl;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getVirtualCoin() {
            return virtualCoin;
        }

        public void setVirtualCoin(int virtualCoin) {
            this.virtualCoin = virtualCoin;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public String getDeviceId() {
            return deviceID;
        }

        public void setDeviceId(String deviceId) {
            this.deviceID = deviceId;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public long getBirthday() {
            return birthday;
        }

        public void setBirthday(long birthday) {
            this.birthday = birthday;
        }

        @Override
        public String toString() {
            return "UserInfo{" +
                    "id='" + id + '\'' +
                    ", phone='" + phone + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", pictureUrl='" + pictureUrl + '\'' +
                    ", sex=" + sex +
                    ", birthday=" + birthday +
                    ", virtualCoin=" + virtualCoin +
                    ", money=" + money +
                    ", deviceID='" + deviceID + '\'' +
                    ", token='" + token + '\'' +
                    '}';
        }
    }
}
