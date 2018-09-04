package com.leyou.game.rong.message;

import android.os.Parcel;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;

/**
 * Description : 自定义邀请好友完狼人杀游戏消息
 *
 * @author : rocky
 * @Create Time : 2017/7/28 下午3:20
 * @Modified Time : 2017/7/28 下午3:20
 */
@MessageTag(value = "app:inviteJoinWolfKill", flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED)
public class InviteJoinWolfKillMessage extends MessageContent {
    private static final String TAG = "InviteJoinWolfKillMessage";
    private String gameName;
    private String gameDes;
    private long gameRoomId;

    protected InviteJoinWolfKillMessage() {
    }

    public static InviteJoinWolfKillMessage obtain(String gameName, String gameDes, long gameRoomId) {
        InviteJoinWolfKillMessage model = new InviteJoinWolfKillMessage();
        model.setGameRoomId(gameRoomId);
        model.setGameDes(gameDes);
        model.setGameName(gameName);
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
            jsonObj.put("gameRoomId", this.gameRoomId);

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
        dest.writeLong(this.gameRoomId);
    }

    public InviteJoinWolfKillMessage(byte[] data) {
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
            if (e.has("gameRoomId")) {
                this.setGameRoomId(e.optLong("gameRoomId"));
            }

            if (e.has("user")) {
                this.setUserInfo(this.parseJsonToUserInfo(e.getJSONObject("user")));
            }
        } catch (JSONException var5) {
            Log.e("JSONException", var5.getMessage());
        }
    }

    protected InviteJoinWolfKillMessage(Parcel in) {
        this.gameName = in.readString();
        this.gameDes = in.readString();
        this.gameRoomId = in.readLong();
    }

    public static final Creator<InviteJoinWolfKillMessage> CREATOR = new Creator<InviteJoinWolfKillMessage>() {
        @Override
        public InviteJoinWolfKillMessage createFromParcel(Parcel source) {
            return new InviteJoinWolfKillMessage(source);
        }

        @Override
        public InviteJoinWolfKillMessage[] newArray(int size) {
            return new InviteJoinWolfKillMessage[size];
        }
    };

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameDes() {
        return gameDes;
    }

    public void setGameDes(String gameDes) {
        this.gameDes = gameDes;
    }

    public long getGameRoomId() {
        return gameRoomId;
    }

    public void setGameRoomId(long gameRoomId) {
        this.gameRoomId = gameRoomId;
    }
}
