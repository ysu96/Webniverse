package com.example.rapidboard.web.dto.webinar;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
public class WebinarDto {
    @NotBlank(message = "제목을 입력하세요.")
    @Size(max = 100)
    private String roomTitle;

    @NotBlank(message = "개설자를 입력하세요.")
    @Size(max = 20)
    private String roomOwner;

    private String passwd;

    //@DateTimeFormat(pattern = "E MMM dd yyyy HH:mm:ss Z") //2021-10-15T10:18
    @NotBlank(message = "시작 시간을 입력하세요.")
    private String startDate;
    @NotBlank(message = "종료 시간을 입력하세요.")
    private String endDate;

    MultipartFile roomImage;

    private int isStreaming;
}
