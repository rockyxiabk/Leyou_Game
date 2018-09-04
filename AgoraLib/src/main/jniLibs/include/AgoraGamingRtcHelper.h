//
//  Agora Rtc Engine SDK
//
//  Created by Sting Feng in 2015-02.
//  Copyright (c) 2015 Agora IO. All rights reserved.
//

#ifndef AGORA_GAMING_RTC_HELPER_H
#define AGORA_GAMING_RTC_HELPER_H

namespace agora { namespace rtc {

    typedef unsigned int uid_t;
    typedef void* view_t;

    enum CHANNEL_PROFILE_TYPE
    {
        CHANNEL_PROFILE_GAME_FREE_MODE = 2,
        CHANNEL_PROFILE_GAME_COMMAND_MODE = 3,
    };

    enum CLIENT_ROLE_TYPE
    {
        CLIENT_ROLE_BROADCASTER = 1,
        CLIENT_ROLE_AUDIENCE = 2,
    };

    enum AUDIO_ROUTE_TYPE
    {
        AUDIO_ROUTE_DEFAULT = -1,
        AUDIO_ROUTE_HEADSET = 0,
        AUDIO_ROUTE_EARPIECE = 1,
        AUDIO_ROUTE_SPEAKERPHONE = 3,
        AUDIO_ROUTE_BLUETOOTH = 5,
    };

    enum USER_OFFLINE_REASON_TYPE
    {
        USER_OFFLINE_QUIT = 0,
        USER_OFFLINE_DROPPED = 1,
        USER_OFFLINE_BECOME_AUDIENCE = 2,
    };

    enum WARN_CODE_TYPE
    {
        WARN_PENDING = 20,
        WARN_NO_AVAILABLE_CHANNEL = 103,
        WARN_LOOKUP_CHANNEL_TIMEOUT = 104,
        WARN_LOOKUP_CHANNEL_REJECTED = 105,
        WARN_OPEN_CHANNEL_TIMEOUT = 106,
        WARN_OPEN_CHANNEL_REJECTED = 107,

        WARN_AUDIO_MIXING_OPEN_ERROR = 701,
        WARN_ADM_RUNTIME_PLAYOUT_WARNING = 1014,
        WARN_ADM_RUNTIME_RECORDING_WARNING = 1016,
        WARN_ADM_RECORD_AUDIO_SILENCE = 1019,
        WARN_ADM_PLAYOUT_MALFUNCTION = 1020,
        WARN_ADM_RECORD_MALFUNCTION = 1021,
        WARN_ADM_RECORD_AUDIO_LOWLEVEL = 1031,
        WARN_APM_HOWLING = 1051,

        // sdk: 100~1000
        WARN_SET_CLIENT_ROLE_TIMEOUT = 118,
        WARN_SET_CLIENT_ROLE_NOT_AUTHORIZED = 119,
    };

    enum ERROR_CODE_TYPE
    {
        ERR_OK = 0,
        // 1~1000
        ERR_FAILED = 1,
        ERR_INVALID_ARGUMENT = 2,
        ERR_NOT_READY = 3,
        ERR_NOT_SUPPORTED = 4,
        ERR_REFUSED = 5,
        ERR_BUFFER_TOO_SMALL = 6,
        ERR_NOT_INITIALIZED = 7,
        ERR_NO_PERMISSION = 9,
        ERR_TIMEDOUT = 10,
        ERR_CANCELED = 11,
        ERR_TOO_OFTEN = 12,
        ERR_BIND_SOCKET = 13,
        ERR_NET_DOWN = 14,
        ERR_NET_NOBUFS = 15,
        ERR_JOIN_CHANNEL_REJECTED = 17,
        ERR_LEAVE_CHANNEL_REJECTED = 18,
        ERR_ALREADY_IN_USE = 19,
        ERR_ABORTED = 20,
        ERR_INIT_NET_ENGINE = 21,
        ERR_INVALID_APP_ID = 101,
        ERR_INVALID_CHANNEL_NAME = 102,
        ERR_CHANNEL_KEY_EXPIRED = 109,
        ERR_INVALID_CHANNEL_KEY = 110,
        ERR_CONNECTION_INTERRUPTED = 111,
        ERR_CONNECTION_LOST = 112,

        ERR_NOT_IN_CHANNEL = 113,
        ERR_SIZE_TOO_LARGE = 114,
        ERR_BITRATE_LIMIT = 115,

        // 1001~2000
        ERR_START_CALL = 1002,
        ERR_ADM_GENERAL_ERROR = 1005,
        ERR_ADM_JAVA_RESOURCE = 1006,
        ERR_ADM_SAMPLE_RATE = 1007,
        ERR_ADM_INIT_PLAYOUT = 1008,
        ERR_ADM_START_PLAYOUT = 1009,
        ERR_ADM_STOP_PLAYOUT = 1010,
        ERR_ADM_INIT_RECORDING = 1011,
        ERR_ADM_START_RECORDING = 1012,
        ERR_ADM_STOP_RECORDING = 1013,
        ERR_ADM_RUNTIME_PLAYOUT_ERROR = 1015,
        ERR_ADM_RUNTIME_RECORDING_ERROR = 1017,
        ERR_ADM_RECORD_AUDIO_FAILED = 1018,
        ERR_ADM_INIT_LOOPBACK  = 1022,
        ERR_ADM_START_LOOPBACK  = 1023,
    };

    struct AudioVolumeInfo
    {
        uid_t uid;
        unsigned int volume; // [0, 255]
    };

    struct RtcStats
    {
        unsigned int duration;
        unsigned int txBytes;
        unsigned int rxBytes;
        unsigned short txKBitRate;
        unsigned short rxKBitRate;

        unsigned short rxAudioKBitRate;
        unsigned short txAudioKBitRate;

        unsigned short rxVideoKBitRate;
        unsigned short txVideoKBitRate;
        unsigned int users;
        double cpuAppUsage;
        double cpuTotalUsage;
    };


}}

#endif
