package com.example.rapidboard.web.dto.webinar;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class ParticipantDto{
    private List<Long> participants;
    private Long webinarId;
}
