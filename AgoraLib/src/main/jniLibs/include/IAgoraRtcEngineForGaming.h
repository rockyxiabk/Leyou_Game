//
//  Agora Rtc Engine SDK
//
//  Created by Sting Feng in 2015-02.
//  Copyright (c) 2015 Agora IO. All rights reserved.
//

#ifndef AGORA_GAMING_RTC_ENGINE_H
#define AGORA_GAMING_RTC_ENGINE_H

#include <stddef.h>
#include <stdio.h>
#include <stdarg.h>

#if defined(_WIN32)
#define WIN32_LEAN_AND_MEAN
#include <windows.h>
#define AGORA_CALL __cdecl
#if defined(AGORARTC_EXPORT)
#define AGORA_API extern "C" __declspec(dllexport)
#else
#define AGORA_API extern "C" __declspec(dllimport)
#endif
#elif defined(__APPLE__)
#define AGORA_API __attribute__((visibility("default"))) extern "C"
#define AGORA_CALL
#elif defined(__ANDROID__) || defined(__linux__) || defined(__linux)
#define AGORA_API extern "C" __attribute__((visibility("default")))
#define AGORA_CALL
#else
#define AGORA_API extern "C"
#define AGORA_CALL
#endif

namespace agora {
namespace rtc {

/**
* the event callback interface
*/
class IGamingRtcEngineEventHandler
{
public:
    virtual ~IGamingRtcEngineEventHandler() {}

    /**
     * when join channel success, the function will be called
     *
     * @param [in] channel
     *        the channel name you have joined
     * @param [in] uid
     *        the UID of you in this channel
     * @param [in] elapsed
     *        the time elapsed in ms from the joinChannel been called to joining completed
     */
    virtual void onJoinChannelSuccess(const char* channel, uid_t uid, int elapsed) {
        (void)channel;
        (void)uid;
        (void)elapsed;
    }

    /**
     * when join channel success, the function will be called
     *
     * @param [in] channel
     *        the channel name you have joined
     * @param [in] uid
     *        the UID of you in this channel
     * @param [in] elapsed
     *        the time elapsed in ms elapsed
     */
    virtual void onRejoinChannelSuccess(const char* channel, uid_t uid, int elapsed) {
        (void)channel;
        (void)uid;
        (void)elapsed;
    }

    /**
     * when channel key is enabled, and specified channel key is invalid or expired, this function will be called.
     * APP should generate a new channel key and call renewChannelKey() to refresh the key.
     * NOTE: to be compatible with previous version, ERR_CHANNEL_KEY_EXPIRED and ERR_INVALID_CHANNEL_KEY are also reported via onError() callback.
     * You should move renew of channel key logic into this callback.
     */
    virtual void onRequestChannelKey() {
    }

    /**
     * when warning message coming, the function will be called
     *
     * @param [in] warn
     *        warning code
     * @param [in] msg
     *        the warning message
     */
    virtual void onWarning(int warn, const char* msg) {
        (void)warn;
        (void)msg;
    }

    /**
     * when error message come, the function will be called
     *
     * @param [in] err
     *        error code
     * @param [in] msg
     *        the error message
     */
    virtual void onError(int err, const char* msg) {
        (void)err;
        (void)msg;
    }

    /**
     * when audio quality message come, the function will be called
     *
     * @param [in] uid
     *        the uid of the peer
     * @param [in] quality
     *        the quality of the user 0~5 the higher the better
     * @param [in] delay
     *        the average time of the audio packages delayed
     * @param [in] lost
     *        the rate of the audio packages lost
     */
    virtual void onAudioQuality(uid_t uid, int quality, unsigned short delay, unsigned short lost) {
        (void)uid;
        (void)quality;
        (void)delay;
        (void)lost;
    }

    /**
     * when the audio volume information come, the function will be called
     *
     * @param [in] speakers
     *        the array of the speakers' audio volume information
     * @param [in] speakerNumber
     *        the count of speakers in this array
     * @param [in] totalVolume
     *        the total volume of all users
     */
    virtual void onAudioVolumeIndication(const AudioVolumeInfo* speakers, unsigned int speakerNumber, int totalVolume) {
        (void)speakers;
        (void)speakerNumber;
        (void)totalVolume;
    }

    /**
     * when the audio volume information come, the function will be called
     * @param [in] stats
     *        the statistics of the call
     */
    virtual void onLeaveChannel(const RtcStats& stats) {
        (void)stats;
    }

    /**
     * when the information of the RTC engine stats come, the function will be called
     *
     * @param [in] stats
     *        the RTC engine stats
     */
    virtual void onRtcStats(const RtcStats& stats) {
        (void)stats;
    }

    /**
     * When audio mixing file playback finished, this function will be called
     */
    virtual void onAudioMixingFinished() {
    }

    /**
     * report the network quality
     *
	 * @param [in] uid
	 *        the UID of the remote user
	 * @param [in] txQuality
     *        the score of the send network quality 0~5 the higher the better
	 * @param [in] rxQuality
	 *        the score of the recv network quality 0~5 the higher the better
	 */
    virtual void onNetworkQuality(uid_t uid, int txQuality, int rxQuality) {
		(void)uid;
		(void)txQuality;
		(void)rxQuality;
    }

    /**
     * when any other user joined in the same channel, the function will be called
     *
     * @param [in] uid
     *        the UID of the remote user
     * @param [in] elapsed
     *        the time elapsed from remote used called joinChannel to joining completed in ms
     */
    virtual void onUserJoined(uid_t uid, int elapsed) {
        (void)uid;
        (void)elapsed;
    }

    /**
     * when user offline(exit channel or offline by accident), the function will be called
     *
     * @param [in] uid
     *        the UID of the remote user
     */
    virtual void onUserOffline(uid_t uid, USER_OFFLINE_REASON_TYPE reason) {
        (void)uid;
        (void)reason;
    }

    /**
     * when remote user muted the audio stream, the function will be called
     *
     * @param [in] uid
     *        the UID of the remote user
     * @param [in] muted
     *        true: the remote user muted the audio stream, false: the remote user unmuted the audio stream
     */
    virtual void onUserMuteAudio(uid_t uid, bool muted) {
        (void)uid;
        (void)muted;
    }

    /**
     * audio route changing detected by sdk, this function will be called
     *
     * @param [in] routing
     *        the audio routing
     */
    virtual void onAudioRouteChanged(AUDIO_ROUTE_TYPE routing) {
        (void)routing;
    }

	/**
     * when api call executed completely, the function will be called
     *
     * @param [in] api
     *        the api name
     * @param [in] error
     *        error code while 0 means OK
     */
    virtual void onApiCallExecuted(const char* api, int error) {
        (void)api;
        (void)error;
    }

    /**
     * when the network can not worked well, the function will be called
     */
    virtual void onConnectionLost() {}

    /**
     * when local user disconnected by accident, the function will be called(then SDK will try to reconnect itself)
     */
    virtual void onConnectionInterrupted() {}
};

class IAudioEffectManager
{
public:
    /**
     * get audio effect volume in the range of [0.0..100.0]
     *
     * @return return effect volume
     */
    virtual double getEffectsVolume() = 0;

    /**
     * set audio effect volume
     *
     * @param [in] volume
     *        in the range of [0..100]
     * @return return 0 if success or an error code
     */
    virtual int setEffectsVolume(double volume) = 0;

    /**
	 * start playing local audio effect specified by file path and other parameters
     *
     * @param [in] soundId
	 *        specify the unique sound id
	 * @param [in] filePath
	 *        specify the path and file name of the effect audio file to be played
	 * @param [in] loop
	 *        whether to loop the effect playing or not, default value is false
	 * @param [in] pitch
	 *        frequency, in the range of [0.5..2.0], default value is 1.0
	 * @param [in] pan
	 *        stereo effect, in the range of [-1..1] where -1 enables only left channel, default value is 0.0
	 * @param [in] gain
	 *        volume, in the range of [0..100], default value is 100
	 * @return return 0 if success or an error code
	 */
    virtual int playEffect (int soundId, const char* filePath,
               bool loop = false,
               double pitch = 1.0,
               double pan = 0.0,
               double gain = 100.0) = 0;

    /**
     * stop playing specified audio effect
     *
     * @param [in] soundId
     *        specify the unique sound id
     * @return return 0 if success or an error code
     */
    virtual int stopEffect(int soundId) = 0;

    /**
     * stop all playing audio effects
     *
     * @return return 0 if success or an error code
     */
    virtual int stopAllEffects() = 0;

    /**
     * preload a compressed audio effect file specified by file path for later playing
     *
     * @param [in] soundId
     *        specify the unique sound id
     * @param [in] filePath
     *        specify the path and file name of the effect audio file to be preloaded
     * @return return 0 if success or an error code
     */
    virtual int preloadEffect(int soundId, const char* filePath) = 0;

    /**
     * unload specified audio effect file from SDK
     *
     * @return return 0 if success or an error code
     */
    virtual int unloadEffect(int soundId) = 0;

    /**
     * pause playing specified audio effect
     *
     * @param [in] soundId
     *        specify the unique sound id
     * @return return 0 if success or an error code
     */
    virtual int pauseEffect(int soundId) = 0;

    /**
     * pausing all playing audio effects
     *
     * @return return 0 if success or an error code
     */
    virtual int pauseAllEffects() = 0;

    /**
     * resume playing specified audio effect
     *
     * @param [in] soundId
     *        specify the unique sound id
     * @return return 0 if success or an error code
     */
    virtual int resumeEffect(int soundId) = 0;

    /**
     * resume all playing audio effects
     *
     * @return return 0 if success or an error code
     */
    virtual int resumeAllEffects() = 0;

    /**
     * set voice only mode(e.g. keyboard strokes sound will be eliminated)
     *
     * @param [in] enable
     *        true for enable, false for disable
     * @return return 0 if success or an error code
     */
    virtual int setVoiceOnlyMode(bool enable) = 0;

    /**
     * place specified speaker's voice with pan and gain
     *
     * @param [in] uid
     *        speaker's uid
     * @param [in] pan
     *        stereo effect, in the range of [-1..1] where -1 enables only left channel, default value is 0.0
     * @param [in] gain
     *        volume, in the range of [0..100], default value is 100
     * @return return 0 if success or an error code
     */
    virtual int setRemoteVoicePosition(uid_t uid, double pan, double gain) = 0;

    /**
     * change the pitch of local speaker's voice
     *
     * @param [in] pitch
     *        frequency, in the range of [0.5..2.0], default value is 1.0
     * @return return 0 if success or an error code
     */
    virtual int setLocalVoicePitch(double pitch) = 0;
};

class IRtcEngineForGaming
{
public:
    /**
     * get the version information of the SDK
     *
     * @param [in, out] build
     *        the build number
     * @return return the version string(in char format)
     */
    virtual const char* getVersion(int* build) = 0;

    /**
    * get the error description of the SDK
    * @param [in] code
    *        the error code
    * @return return the error description string in char format
    */
    virtual const char* getErrorDescription(int code) = 0;

    /**
     * set customized callback for SDK, you can set once and only after SDK intialized
     *
     * @return return void
     */
    virtual void setEventHandler(IGamingRtcEngineEventHandler* handler) = 0;

    /**
     * Set the channel profile: such as communication, live broadcasting, game free mode, game command mode
     *
     * @param profile the channel profile
     * @return return 0 if success or an error code
     */
    virtual int setChannelProfile(CHANNEL_PROFILE_TYPE profile) = 0;

    /**
     * Set the role of user: such as broadcaster, audience
     *
     * @param role the role of client
     * @param permissionKey the permission key to apply the role
     * @return return 0 if success or an error code
     */
    virtual int setClientRole(CLIENT_ROLE_TYPE role, const char* permissionKey) = 0;

    /**
     * set the log information filter level
     *
     * @param [in] filter
     *        the filter level
     * @return return 0 if success or an error code
     */
    virtual int setLogFilter(unsigned int filter) = 0;

    /**
     * set path to save the log file
     *
     * @param [in] filePath
     *        the .log file path you want to saved
     * @return return 0 if success or an error code
     */
    virtual int setLogFile(const char* filePath) = 0;

    /**
     * get IAudioEffectManager object
     *
     * @return return IAudioEffectManager object associated with current rtc engine
     */
    virtual IAudioEffectManager* getAudioEffectManager() = 0;

    /**
     * set parameters of the SDK
     *
     * @param [in] parameters
     *        the parameters(in json format)
     * @return return 0 if success or an error code
     */
    virtual int setParameters(const char* parameters) = 0;

    /**
     * enable audio function, which is enabled by deault.
     *
     * @return return 0 if success or an error code
     */
    virtual int enableAudio() = 0;

    /**
     * disable audio function
     *
     * @return return 0 if success or an error code
     */
    virtual int disableAudio() = 0;

    /**
     * mute/unmute the local audio stream capturing
     *
     * @param [in] mute
     *       true: mute
     *       false: unmute
     * @return return 0 if success or an error code
     */
    virtual int muteLocalAudioStream(bool mute) = 0;

    /**
     * mute/unmute all the remote audio stream receiving
     *
     * @param [in] mute
     *       true: mute
     *       false: unmute
     * @return return 0 if success or an error code
     */
    virtual int muteAllRemoteAudioStreams(bool mute) = 0;

    /**
     * mute/unmute specified remote audio stream receiving
     *
     * @param [in] uid
     *        the uid of the remote user you want to mute/unmute
     * @param [in] mute
     *       true: mute
     *       false: unmute
     * @return return 0 if success or an error code
     */
    virtual int muteRemoteAudioStream(uid_t uid, bool mute) = 0;

    /**
     * enable / disable speakerphone of device
     *
     * @param speakerphone true: switches to speakerphone. false: switches to headset.
     * @return return 0 if success or an error code
     */
    virtual int setEnableSpeakerphone(bool speakerphone) = 0;

    /**
     * set default audio route to speakerphone
     *
     * @param speakerphone true: default to speakerphone. false: default to earpiece for voice chat, speakerphone for video chat
     * @return return 0 if success or an error code
     */
    virtual int setDefaultAudioRouteToSpeakerphone(bool speakerphone) = 0;

    /**
     * enable or disable the audio volume indication
     *
     * @param [in] interval
     *        the period of the callback cycle, in ms
     *        interval <= 0: disable
     *        interval >  0: enable
     * @param [in] smooth
     *        the smooth parameter
     * @return return 0 if success or an error code
     */
    virtual int enableAudioVolumeIndication(int interval, int smooth) = 0; // in ms: <= 0: disable, > 0: enable, interval in ms

    /**
     * adjust recording signal volume
     *
     * @param [in] volume range from 0 to 400
     * @return return 0 if success or an error code
     */
    virtual int adjustRecordingSignalVolume(int volume) = 0; // [0, 400]: e.g. 50~0.5x 100~1x 400~4x

    /**
     * adjust playback signal volume
     *
     * @param [in] volume range from 0 to 400
     * @return return 0 if success or an error code
     */
    virtual int adjustPlaybackSignalVolume(int volume) = 0; // [0, 400]

    /**
	 * mix microphone and local audio file into the audio stream
     *
	 * @param [in] filePath
	 *        specify the path and file name of the audio file to be played
	 * @param [in] loopback
	 *        specify if local and remote participant can hear the audio file.
	 *        false (default): both local and remote party can hear the the audio file
	 *        true: only the local party can hear the audio file
	 * @param [in] replace
	 *        false (default): mix the local microphone captured voice with the audio file
	 *        true: replace the microphone captured voice with the audio file
	 * @param [in] cycle
	 *        specify the number of cycles to play
	 *        -1, infinite loop playback
     * @param [in] playTime
     *        specify the start time(ms) of the audio file to play
     *        0, from the start
	 * @return return 0 if success or an error code
	 */
    virtual int startAudioMixing(const char* filePath, bool loopback, bool replace, int cycle, int playTime) = 0;

    /**
     * stop mixing the local audio stream
     *
     * @return return 0 if success or an error code
     */
    virtual int stopAudioMixing() = 0;

    /**
     * pause mixing the local audio stream
     *
     * @return return 0 if success or an error code
     */
    virtual int pauseAudioMixing() = 0;

    /**
     * resume mixing the local audio stream
     *
     * @return return 0 if success or an error code
     */
    virtual int resumeAudioMixing() = 0;

    /**
     * adjust mixing audio file volume
     *
     * @param [in] volume range from 0 to 100
     * @return return 0 if success or an error code
     */
    virtual int adjustAudioMixingVolume(int volume) = 0;

    /**
     * get the duration of the specified mixing audio file
     *
     * @return return duration(ms)
     */
    virtual int getAudioMixingDuration() = 0;

    /**
     * get the current playing position of the specified mixing audio file
     *
     * @return return the current playing(ms)
     */
    virtual int getAudioMixingCurrentPosition() = 0;

    /**
     * start recording audio streaming to file specified by the file path
     *
     * @param filePath file path to save recorded audio streaming
     * @return return 0 if success or an error code
     */
    virtual int startAudioRecording(const char* filePath) = 0;

    /**
     * stop audio streaming recording
     *
     * @return return 0 if success or an error code
     */
    virtual int stopAudioRecording() = 0;

    /**
     * join the channel, if the channel have not been created, it will been created automatically
     *
     * @param [in] channelName
     *        the channel name
     * @param [in] info
     *        the additional information, it can be null here
     * @param [in] uid
     *        the uid of you, if 0 the system will automatically allocate one for you
     * @return return 0 if success or an error code
     */
    virtual int joinChannel(const char* channelName, const char* info, uid_t uid) = 0;

    virtual int joinChannel(const char* channelKey, const char* channelName, const char* info, uid_t uid) = 0;

    virtual int renewChannelKey(const char* channelKey) = 0;

    /**
     * leave the current channel
     *
     * @return return 0 if success or an error code
     */
    virtual int leaveChannel() = 0;

    virtual void pause() = 0;

    virtual void resume() = 0;

    /**
     * trigger the SDK event working according to vertical synchronization such as onUpdate(Cocos2d)
     *
     * @return return void
     */
    virtual void poll() = 0;
};

} // namespace rtc
} // namespace agora

/**
* create the Gaming RTC engine object and return the pointer
*/
AGORA_API agora::rtc::IRtcEngineForGaming* AGORA_CALL AgoraRtcEngineForGaming_getInstance();

AGORA_API const char* AGORA_CALL AgoraRtcEngineForGaming_getVersion();

AGORA_API const char* AGORA_CALL AgoraRtcEngineForGaming_getErrorDescription(int code);

AGORA_API void AGORA_CALL AgoraRtcEngineForGaming_enableCustomizedPoll();

AGORA_API int AGORA_CALL AgoraRtcEngineForGaming_joinChannel(const char* channelName, const char* info, uid_t uid);
AGORA_API int AGORA_CALL AgoraRtcEngineForGaming_joinChannelByKey(const char* channelKey, const char* channelName, const char* info, uid_t uid);

AGORA_API int AGORA_CALL AgoraRtcEngineForGaming_renewChannelKey(const char* channelKey);

AGORA_API int AGORA_CALL AgoraRtcEngineForGaming_leaveChannel();

AGORA_API int AGORA_CALL AgoraRtcEngineForGaming_poll(uint8_t* buffer, int bufferLength);

AGORA_API int AGORA_CALL AgoraRtcEngineForGaming_setParameters(const char* parameters);

AGORA_API int AGORA_CALL AgoraRtcEngineForGaming_getIntParameter(const char* parameter, const char* args);

AGORA_API int AGORA_CALL AgoraRtcEngineForGaming_setChannelProfile(int profile);

AGORA_API int AGORA_CALL AgoraRtcEngineForGaming_setClientRole(int role, const char* permissionKey);

AGORA_API int AGORA_CALL AgoraRtcEngineForGaming_enableAudio();

AGORA_API int AGORA_CALL AgoraRtcEngineForGaming_disableAudio();

AGORA_API void AGORA_CALL AgoraRtcEngineForGaming_pause();

AGORA_API void AGORA_CALL AgoraRtcEngineForGaming_resume();

AGORA_API int AGORA_CALL AgoraRtcEngineForGaming_startAudioMixing(const char* filePath, int loopback, int replace, int cycle, int playTime);
AGORA_API int AGORA_CALL AgoraRtcEngineForGaming_stopAudioMixing();
AGORA_API int AGORA_CALL AgoraRtcEngineForGaming_muteLocalAudioStream(int mute);

#endif
