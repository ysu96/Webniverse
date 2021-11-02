package com.example.rapidboard.service;

import com.example.rapidboard.domain.post.Post;
import com.example.rapidboard.domain.uploadfile.Uploadfile;
import com.example.rapidboard.domain.uploadfile.UploadfileRepository;
import com.example.rapidboard.handler.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UploadfileService {
    private final UploadfileRepository uploadfileRepository;

    @Transactional
    public void saveFile(Post post, List<MultipartFile> uploadfiles) throws IOException {
        log.info("save file count : {}", uploadfiles.size());
        for (MultipartFile file : uploadfiles) {
            Uploadfile uploadfile = new Uploadfile();
            uploadfile.setPost(post);
            String originalFilename = file.getOriginalFilename();

            UUID uuid = UUID.randomUUID();
            String serverFilename = uuid+"_"+originalFilename;

            String savePath = System.getProperty("user.dir") +File.separator +"files"; //
            /* 파일이 저장되는 폴더가 없으면 폴더를 생성합니다. */
            if (!new File(savePath).exists()) {
                try{
                    new File(savePath).mkdir();
                }
                catch(Exception e){
                    e.getStackTrace();
                }
            }

            String serverFilepath = savePath + "\\" + serverFilename;
            file.transferTo(new File(serverFilepath));

            uploadfile.setOriginalFilename(originalFilename);
            uploadfile.setServerFilename(serverFilename);
            uploadfile.setServerFilepath(serverFilepath);

            uploadfileRepository.save(uploadfile);
        }
    }

    @Transactional
    public ResponseEntity<Resource> fileDownload(Long uploadfileId) throws IOException {
        Uploadfile uploadfile = findById(uploadfileId);
        Path path = Paths.get(uploadfile.getServerFilepath());
        Resource resource = new InputStreamResource(Files.newInputStream(path));
        String outputFileName = new String(uploadfile.getOriginalFilename().getBytes("UTF-8"), "ISO-8859-1");
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + outputFileName + "\"")
                .body(resource);
    }

    @Transactional
    public List<Uploadfile> getUploadfilesByPostAndIs_deleted(Post post){
        return uploadfileRepository.findAllByPostAndIsDeleted(post,0);
    }

    @Transactional
    public void deleteFile(Long uploadfileId){
        Uploadfile uploadfileEntity = findById(uploadfileId);
        uploadfileEntity.deleteFile();
    }

    @Transactional
    public Uploadfile findById(Long uploadfileId){
        Uploadfile uploadfileEntity =  uploadfileRepository.findById(uploadfileId).orElseThrow(()->{
            throw new CustomException("File does not exist.");
        });
        if (uploadfileEntity.getIsDeleted() == 1) throw new CustomException("File does not exist.");
        return uploadfileEntity;
    }
}
