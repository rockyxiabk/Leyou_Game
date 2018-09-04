package com.leyou.game.rong.message;

import android.os.Parcel;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;

/**
 * Description : 自定义分享好友游戏消息
 *
 * @author : rocky
 * @Create Time : 2017/11/9 下午4:56
 * @Modified Time : 2017/11/9 下午4:56
 */
@MessageTag(value = "app:ShareGame", flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED)
public class ShareGameMessage extends MessageContent {
    private static final String TAG = "InviteJoinWolfKillMessage";
    private String gameName;
    private String gameDes;
    private String iconUrl;
    private String gameUrl;
    private String uniqueMark;//游戏唯一标识
    private int screenDirection;// 1横屏，2竖屏
    private String slogan;//宣传语

    protected ShareGameMessage() {
    }

    public static ShareGameMessage obtain(String gameName, String iconUrl, String gameDes, String gameUrl, String uniqueMark, int screenDirection, String slogan) {
        ShareGameMessage model = new ShareGameMessage();
        model.setIconUrl(iconUrl);
        model.setGameDes(gameDes);
        model.setGameName(gameName);
        model.setGameUrl(gameUrl);
        model.setUniqueMark(uniqueMark);
        model.setScreenDirection(screenDirection);
        model.setSlogan(slogan);
        return model;
    }

    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            if (!TextUtils.isEmpty(this.gameName)) {
                jsonObj.put("gameName", this.gameName);
            } else {
                Log.d("gameName", "gameName is null");
            }
            if (!TextUtils.isEmpty(this.gameDes)) {
                jsonObj.put("gameDes", this.gameDes);
            } else {
                Log.d("gameDes", "gameDes is null");
            }
            if (!TextUtils.isEmpty(this.iconUrl)) {
                jsonObj.put("iconUrl", this.iconUrl);
            } else {
                Log.d("iconUrl", "iconUrl is null");
            }
            if (!TextUtils.isEmpty(this.gameUrl)) {
                jsonObj.put("gameUrl", this.gameUrl);
            } else {
                Log.d("gameUrl", "gameUrl is null");
            }
            if (!TextUtils.isEmpty(this.uniqueMark)) {
                jsonObj.put("uniqueMark", this.uniqueMark);
            } else {
                Log.d("uniqueMark", "uniqueMark is null");
            }
            if (this.screenDirection > 0) {
                jsonObj.put("screenDirection", this.uniqueMark);
            } else {
                Log.d("screenDirection", "screenDirection is null");
            }
            if (!TextUtils.isEmpty(this.slogan)) {
                jsonObj.put("slogan", this.slogan);
            } else {
                Log.d("slogan", "slogan is null");
            }

            if (this.getJSONUserInfo() != null) {
                jsonObj.putOpt("user", this.getJSONUserInfo());
            }
        } catch (JSONException var3) {
            Log.e("JSONException", var3.getMessage());
        }
        return jsonObj.toString().getBytes();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.gameName);
        dest.writeString(this.gameDes);
        dest.writeString(this.gameUrl);
        dest.writeString(this.iconUrl);
        dest.writeString(this.uniqueMark);
        dest.writeInt(this.screenDirection);
        dest.writeString(this.slogan);
    }

    public ShareGameMessage(byte[] data) {
        String jsonStr = new String(data);

        try {
            JSONObject e = new JSONObject(jsonStr);
            if (e.has("gameName")) {
                String gameName = e.optString("gameName");
                if (!TextUtils.isEmpty(gameName)) {
                    this.setGameName(gameName);
                }
            }

            if (e.has("gameDes")) {
                this.setGameDes(e.optString("gameDes"));
            }
            if (e.has("iconUrl")) {
                this.setIconUrl(e.optString("iconUrl"));
            }
            if (e.has("gameUrl")) {
                this.setGameUrl(e.optString("gameUrl"));
            }
            if (e.has("uniqueMark")) {
                this.setUniqueMark(e.optString("uniqueMark"));
            }
            if (e.has("screenDirection")) {
                this.setScreenDirection(e.optInt("screenDirection"));
            }
            if (e.has("slogan")) {
                this.setSlogan(e.optString("slogan"));
            }

            if (e.has("user")) {
                this.setUserInfo(this.parseJsonToUserInfo(e.getJSONObject("user")));
            }
        } catch (JSONException var5) {
            Log.e("JSONException", var5.getMessage());
        }
    }

    protected ShareGameMessage(Parcel in) {
        this.gameName = in.readString();
        this.gameDes = in.readString();
        this.iconUrl = in.readString();
        this.gameUrl = in.readString();
        this.uniqueMark = in.readString();
        this.screenDirection = in.readInt();
        this.slogan = in.readString();
    }

    public static final Creator<ShareGameMessage> CREATOR = new Creator<ShareGameMessage>() {
        @Override
        public ShareGameMessage createFromParcel(Parcel source) {
            return new ShareGameMessage(source);
        }

        @Override
        public ShareGameMessage[] newArray(int size) {
            return new ShareGameMessage[size];
        }
    };

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getGameDes() {
        return gameDes;
    }

    public void setGameDes(String gameDes) {
        this.gameDes = gameDes;
    }

    public String getGameUrl() {
        return gameUrl;
    }

    public void setGameUrl(String gameUrl) {
        this.gameUrl = gameUrl;
    }

    public String getUniqueMark() {
        return uniqueMark;
    }

    public void setUniqueMark(String uniqueMark) {
        this.uniqueMark = uniqueMark;
    }

    public int getScreenDirection() {
        return screenDirection;
    }

    public void setScreenDirection(int screenDirection) {
        this.screenDirection = screenDirection;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }
}
