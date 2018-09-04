package com.leyou.game.bean;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.leyou.game.Constants;
import com.leyou.game.GameApplication;
import com.leyou.game.bean.user.BankCardBean;
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

    private boolean isLogIn = false;//用户是否登陆状态
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
        userInfo.setUserId(sp.getString("userId", ""));
        userInfo.idNo = sp.getString("idNo", "");
        userInfo.phone = sp.getString("phone", "");
        userInfo.nickname = sp.getString("nickname", "");
        userInfo.headImgUrl = sp.getString("pictureUrl", "");
        userInfo.deviceID = sp.getString("deviceID", "");
        userInfo.sex = sp.getInt("sex", 0);
        userInfo.type = sp.getInt("type", 1);
        userInfo.birthday = sp.getLong("birthday", 0);
        userInfo.token = sp.getString("token", "");
        userInfo.rongyunToken = sp.getString("rongyunToken", "");
        userInfo.hasBaoku = sp.getBoolean("hasBaoku", false);
        userInfo.newUser = sp.getBoolean("newUser", false);
        userInfo.hasBindCard = sp.getBoolean("hasBindCard", false);
        if (TextUtils.isEmpty(userInfo.userId)) {
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
        userInfo.setDeviceID(imei);
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
        String str1 = "android";
        for (int i = 0; i < 15; i++) {//自定义随机生成15位字符
            int index = (int) (Math.random() * str.length);
            str1 += str[index];
        }
        userInfo.setUserId(str1);

        SharedPreferences userData = context.getSharedPreferences("userData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userData.edit();
        editor.putString("id", userInfo.userId);
        editor.apply();
    }

    public void saveUserNickNameAndPicture(String nickName, String url, String idNo) {
        if (!isMainProcess) {
            return;
        }
        this.userInfo.headImgUrl = url;
        this.userInfo.nickname = nickName;
        this.userInfo.idNo = idNo;
        SharedPreferences sp = context.getSharedPreferences("userData", Context.MODE_PRIVATE);
        sp.edit().putString("pictureUrl", userInfo.headImgUrl)
                .putString("nickname", userInfo.nickname)
                .putString("idNo", userInfo.idNo)
                .apply();
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
        sp.edit().putString("userId", userInfo.userId)
                .putString("idNo", userInfo.idNo)
                .putString("pictureUrl", userInfo.headImgUrl)
                .putString("nickname", userInfo.nickname)
                .putLong("birthday", userInfo.birthday)
                .putString("phone", userInfo.phone)
                .putInt("sex", userInfo.sex)
                .putInt("type", userInfo.type)
                .putString("deviceID", userInfo.deviceID)
                .putString("token", userInfo.token)
                .putBoolean("hasBindCard", userInfo.hasBindCard)
                .putBoolean("newUser", userInfo.newUser)
                .putBoolean("hasBaoku", userInfo.hasBaoku)
                .putString("rongyunToken", userInfo.rongyunToken)
                .apply();
        this.userInfo.userId = userInfo.userId;
        this.userInfo.idNo = userInfo.idNo;
        this.userInfo.headImgUrl = userInfo.headImgUrl;
        this.userInfo.nickname = userInfo.nickname;
        this.userInfo.birthday = userInfo.birthday;
        this.userInfo.hasBindCard = userInfo.hasBindCard;
        this.userInfo.sex = userInfo.sex;
        this.userInfo.newUser = userInfo.newUser;
        this.userInfo.hasBaoku = userInfo.hasBaoku;
        this.userInfo.phone = userInfo.phone;
        this.userInfo.token = userInfo.token;
        this.userInfo.type = userInfo.type;
        this.userInfo.rongyunToken = userInfo.rongyunToken;
        this.setLogIn(true);
        GameApplication.bindAlias(userInfo.userId);

        updateUserDB();

        GameApplication.getMyFriends();
    }

    private void updateUserDB() {
        Friend friend = new Friend();
        friend.setId(System.currentTimeMillis());
        friend.setStatus(Friend.FRIEND);
        String pinyin = PinyinUtil.getPingYin(userInfo.nickname);
        String sortString = pinyin.substring(0, 1).toUpperCase();
        if (sortString.matches("[A-Z]")) {
            friend.setPhoneNameLetter(sortString);
        } else if (sortString.matches("[0-9]")) {
            friend.setPhoneNameLetter("*");
        } else {
            friend.setPhoneNameLetter("#");
        }
        friend.setNickname(userInfo.nickname);
        friend.setName(userInfo.nickname);
        friend.setBirthday(userInfo.birthday);
        friend.setSex(userInfo.sex);
        friend.setPhone(userInfo.phone);
        friend.setHeadImgUrl(userInfo.headImgUrl);
        friend.setUserId(userInfo.userId);
        friend.setIdNo(userInfo.idNo);

        DBUtil.getInstance(context).updateFriend(friend);
    }

    /**
     * 清空用户本地登陆数据 退出登陆
     */
    public boolean clearUserInfo() {

        GameApplication.unBindAlias(userInfo.userId);

        if (!isMainProcess) {
            return false;
        }
        setLogIn(false);
        SharedPreferences sp = context.getSharedPreferences("userData", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.clear();
        setLogIn(false);
        this.userInfo.userId = "";
        this.userInfo.idNo = "";
        this.userInfo.headImgUrl = "";
        this.userInfo.nickname = "点击登陆";
        this.userInfo.birthday = System.currentTimeMillis();
        this.userInfo.sex = 1;
        this.userInfo.hasBindCard = false;
        this.userInfo.hasBaoku = false;
        this.userInfo.newUser = false;
        this.userInfo.type = 1;
        this.userInfo.phone = "";
        this.userInfo.rongyunToken = "";
        this.userInfo.token = "";

        generateUserID();
        generateDeviceId();
        GameApplication.disconnect();
        DBUtil.getInstance(context).clearAllData();
        SPUtil.clearAllSP(context);

        return edit.commit();
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public String getId() {
        if (!isMainProcess) {
            loadLocalUserInfo();
        }
        return null != userInfo.userId ? userInfo.userId : "";
    }

    public String getIDNo() {
        return null != userInfo.idNo ? userInfo.idNo : "";
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
        return null != userInfo.headImgUrl ? userInfo.headImgUrl : "";
    }

    public int getDiamonds() {
        return userInfo.diamondsNum;
    }

    public void setDiamonds(int diamonds) {
        userInfo.diamondsNum = diamonds;
    }

    public double getMoney() {
        return userInfo.money;
    }

    public void setMoney(double money) {
        userInfo.money = money;
    }

    public boolean isLogIn() {
        return isLogIn;
    }

    public boolean isNewUser() {
        return userInfo.newUser;
    }

    public boolean isBindCard() {
        return userInfo.hasBindCard;
    }

    public boolean hasBaoku() {
        return userInfo.hasBaoku;
    }

    public void setHasBaoku(boolean hasBaoku) {
        this.userInfo.hasBaoku = hasBaoku;
        SharedPreferences sp = context.getSharedPreferences("userData", Context.MODE_PRIVATE);
        sp.edit().putBoolean("hasBaoku", userInfo.hasBaoku).apply();
    }

    public void setIsBindCard(boolean isBindCard) {
        userInfo.hasBindCard = isBindCard;
    }

    public int getIsDeveloper() {
        return userInfo.type;
    }

    public String getCardNum() {
        return userInfo.cardNum;
    }

    public String getBankName() {
        return userInfo.bankName;
    }

    public void setBankInfo(BankCardBean cardBean) {
        this.userInfo.bankName = cardBean.bankName;
        this.userInfo.cardNum = cardBean.cardNum;
    }

    public void setLogIn(boolean logIn) {
        isLogIn = logIn;
    }

    public int getSex() {
        return userInfo.sex;
    }

    public int getType() {
        return userInfo.type;
    }

    public long getBirthday() {
        return userInfo.birthday;
    }

    public String getToken() {
        return userInfo.token;
    }

    public String getRongToken() {
        return userInfo.rongyunToken;
    }

    public boolean isRcIsConnected() {
        return rcIsConnected;
    }

    public void setRcIsConnected(boolean rcIsConnected) {
        this.rcIsConnected = rcIsConnected;
    }


    public static class UserInfo {

        public String userId;//用户userId
        public String idNo;//用户ID
        public String phone;//登陆手机号
        public String nickname;//昵称
        public String headImgUrl;//头像地址
        public long birthday; //出生年月日（时间戳形式
        public int sex;//性别（1男or2女）

        public int diamondsNum;//钻石
        public double money;//余额现金

        public boolean hasBindCard;//是否已经绑卡
        public String cardNum;//银行简介（尾号4983储蓄卡）
        public String bankName;//银行名称

        public int type;//1普通用户2开发者

        public String deviceID;//手机设备码
        public String rongyunToken;//融云 tokenId
        public String token;//身份令牌，验证使用

        public boolean newUser;//是否是新用户，是否展示新手引导页面
        public boolean hasBaoku;//是否已经激活宝库

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getIdNo() {
            return idNo;
        }

        public void setIdNo(String idNo) {
            this.idNo = idNo;
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

        public String getHeadImgUrl() {
            return headImgUrl;
        }

        public void setHeadImgUrl(String headImgUrl) {
            this.headImgUrl = headImgUrl;
        }

        public long getBirthday() {
            return birthday;
        }

        public void setBirthday(long birthday) {
            this.birthday = birthday;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getDiamondsNum() {
            return diamondsNum;
        }

        public void setDiamondsNum(int diamondsNum) {
            this.diamondsNum = diamondsNum;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public boolean isHasBindCard() {
            return hasBindCard;
        }

        public void setHasBindCard(boolean hasBindCard) {
            this.hasBindCard = hasBindCard;
        }

        public String getCardNum() {
            return cardNum;
        }

        public void setCardNum(String cardNum) {
            this.cardNum = cardNum;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getDeviceID() {
            return deviceID;
        }

        public void setDeviceID(String deviceID) {
            this.deviceID = deviceID;
        }

        public String getRongyunToken() {
            return rongyunToken;
        }

        public void setRongyunToken(String rongyunToken) {
            this.rongyunToken = rongyunToken;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public boolean isNewUser() {
            return newUser;
        }

        public void setNewUser(boolean newUser) {
            this.newUser = newUser;
        }

        public boolean isHasBaoku() {
            return hasBaoku;
        }

        public void setHasBaoku(boolean hasBaoku) {
            this.hasBaoku = hasBaoku;
        }

        @Override
        public String toString() {
            return "UserInfo{" +
                    "userId='" + userId + '\'' +
                    ", phone='" + phone + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", headImgUrl='" + headImgUrl + '\'' +
                    ", birthday=" + birthday +
                    ", sex=" + sex +
                    ", diamondsNum=" + diamondsNum +
                    ", money=" + money +
                    ", hasBindCard=" + hasBindCard +
                    ", cardNum='" + cardNum + '\'' +
                    ", bankName='" + bankName + '\'' +
                    ", type=" + type +
                    ", deviceID='" + deviceID + '\'' +
                    ", rongyunToken='" + rongyunToken + '\'' +
                    ", token='" + token + '\'' +
                    ", newUser=" + newUser +
                    ", hasBaoku=" + hasBaoku +
                    '}';
        }
    }
}
