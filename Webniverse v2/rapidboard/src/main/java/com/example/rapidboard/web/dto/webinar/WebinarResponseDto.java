package com.example.rapidboard.web.dto.webinar;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class WebinarResponseDto {
    //------방 생성------
    @Data
    public static class CreateResponse{
        private Map<String, CreateRoom> data;
        private String resultCode;
        private String description;
    }

    @Data
    public static class CreateRoom{
        private String endDate;
        private String roomId;
        private int maxJoinCount;
    }

    //--------OTP 발급--------
    @Data
    public static class OtpResponse{
        private Map<String, RoomUserOtp> data;
        private String resultCode;
        private String description;
    }

    @Data
    public static class RoomUserOtp{
        private String otp;
        private int expiresIn;
    }

    //------방 종료------
    @Data
    public static class DeleteResponse{
        private String resultCode;
        private String description;
    }

    //------로그---------
    @Data
    public static class UserLogResponse{
        private LogData data;
        private String resultCode;
        private String description;
    }

    @Data
    public static class LogData{
        private List<LogList> logList;
    }
    @Data
    public static class LogList {
        private String roomId;
        private List<LogInfo> logs;
    }

    @Data
    public static class LogInfo{
        private String logType;
        private String roomId;
        @JsonProperty("USERNAME")
        private String username;
        private String leaveReason;
        private String deviceType;
        private String osInfo;
        private String deviceInfo;
        private String regDate;
        private String apiUserId;
        private String mappingKey01;
    }

}
