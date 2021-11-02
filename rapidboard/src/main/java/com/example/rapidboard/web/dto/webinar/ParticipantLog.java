package com.example.rapidboard.web.dto.webinar;

import lombok.Data;

@Data
public class ParticipantLog {
    private String username;
    private String name;
    private String email;
    private String type;
    private String logDate;
    private String deviceInfo;
    private String osInfo;
}
