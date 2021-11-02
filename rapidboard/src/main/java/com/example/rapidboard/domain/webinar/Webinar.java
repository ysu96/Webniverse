package com.example.rapidboard.domain.webinar;

import com.example.rapidboard.domain.comment.Comment;
import com.example.rapidboard.domain.member.Member;
import com.example.rapidboard.domain.participate.Participate;
import com.example.rapidboard.web.dto.webinar.WebinarDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Slf4j
public class Webinar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "webinar_id")
    private Long webinarId;

    @Column(nullable = false, name = "room_title")
    private String roomTitle;

    @Column(nullable = false, name = "room_owner")
    private String roomOwner;

    @Column
    private String password;

    @Column(nullable = false, name = "start_date")
    private String startDate;

    @Column(nullable = false, name = "end_date")
    private String endDate;

    @Column(name = "image_filepath")
    private String imageFilepath;

    // 0: 대기 , 1: 시작, 2: 종료
    @Column(nullable = false, name = "is_streaming")
    private int isStreaming = 0;

    @Column(nullable = false, name = "is_deleted")
    private int isDeleted = 0;

    @Column(nullable = false, name = "is_main")
    private int isMain = 0;

    @Column(nullable = false, name = "room_id")
    private String roomId;

    @JsonIgnore
    @OneToMany(mappedBy = "webinar", cascade = CascadeType.ALL)
    private List<Participate> participates;

    public static Webinar createWebinar(WebinarDto webinarDto, String roomId) throws IOException {
        String serverFilepath;
        if(webinarDto.getRoomImage() != null){
            MultipartFile img = webinarDto.getRoomImage();
            String originalFilename = img.getOriginalFilename();
            UUID uuid = UUID.randomUUID();
            String serverFilename = uuid+"_"+originalFilename;
            String savePath = System.getProperty("user.dir") + File.separator +"files"; //
            /* 파일이 저장되는 폴더가 없으면 폴더를 생성합니다. */
            if (!new File(savePath).exists()) {
                try{
                    new File(savePath).mkdir();
                }
                catch(Exception e){
                    e.getStackTrace();
                }
            }

            serverFilepath = savePath + File.separator + serverFilename;
            log.info(serverFilepath);
            img.transferTo(new File(serverFilepath));
        }else{
            serverFilepath = null;
        }
        return Webinar.builder()
                .roomTitle(webinarDto.getRoomTitle())
                .roomOwner(webinarDto.getRoomOwner())
                .password(webinarDto.getPasswd())
                .startDate(webinarDto.getStartDate())
                .endDate(webinarDto.getEndDate())
                .roomId(roomId)
                .imageFilepath(serverFilepath)
                .build();
    }

    public void update(WebinarDto webinarDto) throws IOException {
        String serverFilepath;
        if(webinarDto.getRoomImage() != null){
            MultipartFile img = webinarDto.getRoomImage();
            String originalFilename = img.getOriginalFilename();
            UUID uuid = UUID.randomUUID();
            String serverFilename = uuid+"_"+originalFilename;
            String savePath = System.getProperty("user.dir") + File.separator +"files"; //
            /* 파일이 저장되는 폴더가 없으면 폴더를 생성합니다. */
            if (!new File(savePath).exists()) {
                try{
                    new File(savePath).mkdir();
                }
                catch(Exception e){
                    e.getStackTrace();
                }
            }

            serverFilepath = savePath + File.separator + serverFilename;
            log.info(serverFilepath);
            img.transferTo(new File(serverFilepath));
            this.imageFilepath = serverFilepath;
        }
        this.roomTitle = webinarDto.getRoomTitle();
        this.roomOwner = webinarDto.getRoomOwner();
        this.password = webinarDto.getPasswd();
        this.startDate = webinarDto.getStartDate();
        this.endDate = webinarDto.getEndDate();
        this.isStreaming = webinarDto.getIsStreaming();
    }

    public void deleteWebinar(){
        this.isDeleted = 1;
    }
    public void mainRoom() {this.isMain=1;}
    public void otherRoom(){this.isMain=0;}
    public void endRoom(){this.isStreaming=2;}
    public void startRoom(){this.isStreaming=1;}
    public void waitRoom(){this.isStreaming=0;}
}
