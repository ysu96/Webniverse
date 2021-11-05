package com.example.rapidboard.web.api;

import com.example.rapidboard.domain.webinar.Webinar;
import com.example.rapidboard.service.WebinarService;
import com.example.rapidboard.web.dto.webinar.ParticipantDto;
import com.example.rapidboard.web.dto.webinar.WebinarDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WebinarApiController {
    private final WebinarService webinarService;

    @PostMapping("/api/webinar/create")
    public ResponseEntity<?> createRoom(@ModelAttribute @Valid WebinarDto webinarDto) throws ParseException, org.json.simple.parser.ParseException, IOException {
        log.info("{}", webinarDto.getStartDate());
        webinarService.createRoom(webinarDto);

        return ResponseEntity.noContent().build();
        //"resultCode":"GRM_200","data":{"room":{"endDate":"Sat Oct 16 2021 09:45:11 +0900","maxJoinCount":4,"roomId":"8e2b53f0b12646fe82a11cbdb5cd5489"}
    }

    @GetMapping("/showImage/{webinarId}")
    public ResponseEntity<Resource> showImage(@PathVariable Long webinarId) throws IOException {
        log.info("------------------------------------------------------");
        String filePath = webinarService.findById(webinarId).getImageFilepath();
        Resource resource = new FileSystemResource(filePath);
        HttpHeaders header = new HttpHeaders();
        Path fp = Paths.get(filePath);
        header.add("Content-Type", Files.probeContentType(fp));
        return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
    }

    @PutMapping("/api/webinar/main/{webinarId}")
    public ResponseEntity<?> register(@PathVariable Long webinarId){
        log.info("register main webinar ID {}", webinarId);
        webinarService.register(webinarId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/api/webinar/delete/{webinarId}")
    public ResponseEntity<?> delete(@PathVariable Long webinarId) throws JsonProcessingException {
        webinarService.delete(webinarId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/webinar/get/{webinarId}")
    public ResponseEntity<?> get(@PathVariable Long webinarId){
        Webinar webinarEntity = webinarService.findById(webinarId);
        return new ResponseEntity<>(webinarEntity, HttpStatus.OK);
    }

    @PutMapping("/api/webinar/update/{webinarId}")
    public ResponseEntity<?> update(@ModelAttribute @Valid WebinarDto webinarDto, @PathVariable Long webinarId) throws IOException, org.json.simple.parser.ParseException {
        webinarService.update(webinarDto, webinarId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/webinar/addParticipant")
    public ResponseEntity<?> addParticipant(@RequestBody ParticipantDto participantDto) {
        log.info(participantDto.toString());
        webinarService.addParticipant(participantDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/api/webinar/deleteParticipant")
    public ResponseEntity<?> deleteParticipant(@RequestBody ParticipantDto participantDto) {
        webinarService.deleteParticipant(participantDto);
        return ResponseEntity.noContent().build();
    }
}
