package com.example.rapidboard.service;

import com.example.rapidboard.domain.member.Member;
import com.example.rapidboard.domain.member.MemberRepository;
import com.example.rapidboard.domain.participate.Participate;
import com.example.rapidboard.domain.participate.ParticipateRepository;
import com.example.rapidboard.domain.webinar.Webinar;
import com.example.rapidboard.domain.webinar.WebinarRepository;
import com.example.rapidboard.handler.exception.CustomException;
import com.example.rapidboard.web.dto.webinar.ParticipantDto;
import com.example.rapidboard.web.dto.webinar.ParticipantLog;
import com.example.rapidboard.web.dto.webinar.WebinarDto;

import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class WebinarService {
    private final WebinarRepository webinarRepository;
    private final RestTemplate restTemplate;
    private final MemberRepository memberRepository;
    private final ParticipateRepository participateRepository;
    private final MemberService memberService;


    @Transactional
    public void createRoom(WebinarDto webinarDto) throws ParseException, org.json.simple.parser.ParseException, IOException {
        log.info("web service");
        // -------GMT 포멧으로 시간 변경-------
        SimpleDateFormat dateFormatCur = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        SimpleDateFormat dateFormatNew = new SimpleDateFormat("E MMM dd yyyy HH:mm:ss Z", Locale.US);

        String curStartDateStr = webinarDto.getStartDate();
        log.info(curStartDateStr);
        Date curStartDate = dateFormatCur.parse(curStartDateStr);
        String newStartDateStr = dateFormatNew.format(curStartDate);
        log.info("start date : {}",newStartDateStr);

        String curEndDateStr = webinarDto.getEndDate();
        log.info(curEndDateStr);
        Date curEndDate = dateFormatCur.parse(curEndDateStr);
        String newEndDateStr = dateFormatNew.format(curEndDate);
        log.info("end date : {}",newEndDateStr);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("roomTitle", webinarDto.getRoomTitle());
        params.add("passwd", webinarDto.getPasswd());
        params.add("startDate", newStartDateStr);
        params.add("endDate", newEndDateStr);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type",  "application/x-www-form-urlencoded; charset=utf-8");
        headers.add("X-GRM-AuthToken", "1aa271b4af192d114c55199a81cc211093b170481d15119584");
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

//        RestTemplate rt = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(
                "https://biz-dev-api.gooroomee.com/api/v1/room", //{요청할 서버 주소}: https://biz-dev-api.gooroomee.com//api/v1/room
                HttpMethod.POST, //{요청할 방식}
                entity, // {요청할 때 보낼 데이터}
                String.class //{요청시 반환되는 데이터 타입}
        );

        JSONParser jsonParser = new JSONParser();
        //JSON데이터를 넣어 JSON Object 로 만들어 준다.
        JSONObject jsonObject = (JSONObject) jsonParser.parse(response.getBody());
        //books의 배열을 추출
        String resultCode = (String) jsonObject.get("resultCode");
        String description = (String) jsonObject.get("description");
        log.info("room create response : {}", response.getBody());

        if(resultCode.equals("GRM_200")){
            JSONObject data = (JSONObject) jsonObject.get("data");
            JSONObject room = (JSONObject) data.get("room");
            String roomId = (String) room.get("roomId");
            webinarRepository.save(Webinar.createWebinar(webinarDto, roomId));
        }else{
            throw new CustomException(description);
        }

        log.info("{}",jsonObject);
        //{"data":{"room":{"endDate":"Tue Oct 19 2021 18:10:00 +0900","maxJoinCount":4,"roomId":"c847fd61197e466daebd9a9eac77e9af"}},"resultCode":"GRM_200","description":"success"}
    }

    @Transactional
    public Page<Webinar> getRoomList(Pageable pageable) throws ParseException {
        List<Webinar> webinars = webinarRepository.findAllByIsDeleted(0);
        SimpleDateFormat dateFormatCur = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date now = new Date();
        Date parsedNow = dateFormatCur.parse(dateFormatCur.format(now));
        log.info("current time : {}", parsedNow.toString());
        for (Webinar webinar : webinars) {
            Date endDate = dateFormatCur.parse(webinar.getEndDate());
            if(endDate.before(parsedNow)){ // 종료 시간 지남
                webinar.endRoom();
            }
        }
        Page<Webinar> ws = webinarRepository.findAllByIsDeletedOrderByStartDateAsc(0, pageable);
        return ws;
    }

    @Scheduled(cron = "0/1 * * * * *")
    public void checkEndDate() throws ParseException {
        log.info("Current Thread : {}", Thread.currentThread().getName());
        List<Webinar> webinars = webinarRepository.findAllByIsDeleted(0);
        SimpleDateFormat dateFormatCur = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date now = new Date();
        Date parsedNow = dateFormatCur.parse(dateFormatCur.format(now));
        log.info("current time : {}", parsedNow.toString());
        for (Webinar webinar : webinars) {
            Date endDate = dateFormatCur.parse(webinar.getEndDate());
            if(endDate.before(parsedNow)){ // 종료 시간 지남
                webinar.endRoom();
            }
        }

    }

    @Transactional
    public String enterWebinar(Long webinarId, Long principalId, String username) throws org.json.simple.parser.ParseException {
        Member member = memberService.findById(principalId);
        Webinar webinar = findById(webinarId);

        String roleId;


        List<Member> allParticipants = findAllParticipants(webinarId);
        if (allParticipants.contains(member)) {
            roleId = "participant";
            if(member.getRole().equals("ROLE_ADMIN")) roleId = "speaker";
        }
        else{
            throw new CustomException("입장 권한이 없습니다.");
        }


        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("roomId", webinar.getRoomId());
        params.add("username", username);
        params.add("roleId", roleId); //speaker, emcee
        params.add("apiUserId", principalId.toString());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type",  "application/x-www-form-urlencoded; charset=utf-8");
        headers.add("X-GRM-AuthToken", "1aa271b4af192d114c55199a81cc211093b170481d15119584");
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

//        RestTemplate rt = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(
                "https://biz-dev-api.gooroomee.com/api/v1/room/user/otp ", //{요청할 서버 주소}: https://biz-dev-api.gooroomee.com//api/v1/room
                HttpMethod.POST, //{요청할 방식}
                entity, // {요청할 때 보낼 데이터}
                String.class //{요청시 반환되는 데이터 타입}
        );

        log.info(response.getBody()); //{"resultCode":"GRM_200","data":{"roomUserOtp":{"otp":"4e15093d347a42738c2311443cc6971a70e0d2e3db154d87","expiresIn":30}},"description":"success"}
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(response.getBody());
        String resultCode = (String) jsonObject.get("resultCode");

        if(resultCode.equals("GRM_200")){
            JSONObject data = (JSONObject) jsonObject.get("data");
            JSONObject roomUserOtp = (JSONObject) data.get("roomUserOtp");
            String otp = (String) roomUserOtp.get("otp");
//            발급된 OTP 는 유효시간 안에 한번만 사용 가능하다.
//            발급된 OTP 를 이용하여 아래 URL 로 접속한다.
//            {ServiceServer}/room/otp/{발급받은 otp}
            return otp;
        }
        throw new CustomException("방 입장 오류");
    }
//
//    @Transactional
//    public void enterWebinarTest(Long webinarId, Long principalId, String username) throws org.json.simple.parser.ParseException {
//        Member member = memberService.findById(principalId);
//        Webinar webinar = findById(webinarId);
//
//        String roleId;
//
//
//        List<Member> allParticipants = findAllParticipants(webinarId);
//        if (allParticipants.contains(member)) {
//            roleId = "participant";
//            if(member.getRole().equals("ROLE_ADMIN")) roleId = "speaker";
//        }
//        else{
//            throw new CustomException("입장 권한이 없습니다.");
//        }
//
//        long beforeTime = System.currentTimeMillis(); //코드 실행 전에 시간 받아오기
//        for(int i=0; i<1500; i++){
//            log.info("-------------{}--------",i);
//            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//            params.add("roomId", webinar.getRoomId());
//            params.add("username", username);
//            params.add("roleId", roleId); //speaker, emcee
//            params.add("apiUserId", principalId.toString());
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.add("Content-Type",  "application/x-www-form-urlencoded; charset=utf-8");
//            headers.add("X-GRM-AuthToken", "1aa271b4af192d114c55199a81cc211093b170481d15119584");
//            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
//
////            RestTemplate rt = new RestTemplate();
//
//            ResponseEntity<String> response = restTemplate.exchange(
//                    "https://biz-dev-api.gooroomee.com/api/v1/room/user/otp ", //{요청할 서버 주소}: https://biz-dev-api.gooroomee.com//api/v1/room
//                    HttpMethod.POST, //{요청할 방식}
//                    entity, // {요청할 때 보낼 데이터}
//                    String.class //{요청시 반환되는 데이터 타입}
//            );
//
//            log.info(response.getBody()); //{"resultCode":"GRM_200","data":{"roomUserOtp":{"otp":"4e15093d347a42738c2311443cc6971a70e0d2e3db154d87","expiresIn":30}},"description":"success"}
//            JSONParser jsonParser = new JSONParser();
//            JSONObject jsonObject = (JSONObject) jsonParser.parse(response.getBody());
//            String resultCode = (String) jsonObject.get("resultCode");
//
//            if(resultCode.equals("GRM_200")){
//                JSONObject data = (JSONObject) jsonObject.get("data");
//                JSONObject roomUserOtp = (JSONObject) data.get("roomUserOtp");
//                String otp = (String) roomUserOtp.get("otp");
////            발급된 OTP 는 유효시간 안에 한번만 사용 가능하다.
////            발급된 OTP 를 이용하여 아래 URL 로 접속한다.
////            {ServiceServer}/room/otp/{발급받은 otp}
////                return otp;
//            }
////            throw new CustomException("방 입장 오류");
//        }
//
//        long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
//        long secDiffTime = (afterTime - beforeTime)/1000; //두 시간에 차 계산
//        System.out.println("시간차이(s) : "+secDiffTime);
//    }

    @Transactional
    public Webinar findById(Long webinarId){
        Webinar webinarEntity = webinarRepository.findById(webinarId).orElseThrow(()->{
            throw new CustomException("Webinar does not exist.");
        });
        if (webinarEntity.getIsDeleted() == 1) throw new CustomException("Webinar does not exist.");
        return webinarEntity;
    }

    @Transactional
    public void register(Long webinarId){
        Webinar currentMainWebinar = webinarRepository.findWebinarByIsMain(1);
        if(currentMainWebinar != null) currentMainWebinar.otherRoom();
        Webinar newMainWebinar = findById(webinarId);
        newMainWebinar.mainRoom();
    }

    @Transactional
    public Webinar mainWebinar(){
        return webinarRepository.findWebinarByIsMain(1);
    }

    @Transactional
    public void delete(Long webinarId) throws org.json.simple.parser.ParseException {
        Webinar webinarEntity = findById(webinarId);

        // 아직 종료 안됐으면
        if(webinarEntity.getIsStreaming() != 2){
            webinarEntity.endRoom();
            JSONObject jsonObject = deleteAPI(webinarEntity.getRoomId());
            String resultCode = (String) jsonObject.get("resultCode");
            String description = (String) jsonObject.get("description");
            if(!resultCode.equals("GRM_200")) throw new CustomException(description);
        }

        webinarEntity.deleteWebinar();
        webinarEntity.otherRoom();

    }

    @Transactional
    public void update(WebinarDto webinarDto, Long webinarId) throws IOException, org.json.simple.parser.ParseException {
        Webinar webinarEntity = findById(webinarId);
        webinarEntity.update(webinarDto);

        //종료됐으면
        if(webinarEntity.getIsStreaming() == 2){
            JSONObject jsonObject = deleteAPI(webinarEntity.getRoomId());
            String resultCode = (String) jsonObject.get("resultCode");
            String description = (String) jsonObject.get("description");
            if(!resultCode.equals("GRM_200")) throw new CustomException(description);
        }
    }

    public JSONObject deleteAPI(String roomId) throws org.json.simple.parser.ParseException {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("roomId", roomId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type",  "application/x-www-form-urlencoded; charset=utf-8");
        headers.add("X-GRM-AuthToken", "1aa271b4af192d114c55199a81cc211093b170481d15119584");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

//        RestTemplate rt = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(
                "https://biz-dev-api.gooroomee.com/api/v1/room/" + roomId, //{요청할 서버 주소}: https://biz-dev-api.gooroomee.com//api/v1/room
                HttpMethod.DELETE, //{요청할 방식}
                entity, // {요청할 때 보낼 데이터}
                String.class //{요청시 반환되는 데이터 타입}
        );

        log.info(response.getBody()); //{"resultCode":"GRM_200","data":{"roomUserOtp":{"otp":"4e15093d347a42738c2311443cc6971a70e0d2e3db154d87","expiresIn":30}},"description":"success"}
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(response.getBody());
        //String resultCode = (String) jsonObject.get("resultCode");
        //{"resultCode":"GRM_200","description":"success"}
        //{"resultCode":"GRM_700","description":"invalid roomId"}
        return jsonObject;
    }

    @Transactional
    public void addParticipant(ParticipantDto participantDto) {
        List<Long> ids = participantDto.getParticipants();
        Webinar webinarEntity = findById(participantDto.getWebinarId());

        for (long id : ids) {
            Participate participate = new Participate();
            participate.setWebinar(webinarEntity);
            participate.setMemberId(id);
            participateRepository.save(participate);
        }
    }

    @Transactional
    public void deleteParticipant(ParticipantDto participantDto) {
        List<Long> ids = participantDto.getParticipants();
        Webinar webinarEntity = findById(participantDto.getWebinarId());
//        List<Participate> participates = webinarEntity.getParticipates();
        for (long id : ids) {
            Participate participate = participateRepository.findByMemberIdAndWebinar(id, webinarEntity);
//            participates.remove(participate);
            participateRepository.delete(participate);
        }
    }

    @Transactional
    public List<Member> findAllParticipants(Long webinarId){
        Webinar webinar = findById(webinarId);
        List<Member> participants = new ArrayList<>();
        List<Participate> participates = webinar.getParticipates();
        for (Participate p : participates) {
            Member member = memberRepository.findById(p.getMemberId()).get();
            if (member.getIsDeleted() == 1) continue;
            else participants.add(member);

        }
        return participants;
    }

    @Transactional
    public List<ParticipantLog> getLogs(String roomId) throws org.json.simple.parser.ParseException {
        List<ParticipantLog> plogs = new ArrayList<>();

        JSONObject jsonObject = userLogAPI(roomId);
        String resultCode = (String) jsonObject.get("resultCode");
        String description = (String) jsonObject.get("description");

        JSONObject data = (JSONObject) jsonObject.get("data");
        JSONArray logList = (JSONArray) data.get("logList");
        JSONObject ll = (JSONObject) logList.get(0);
        JSONArray logs = (JSONArray) ll.get("logs");

        log.info(logs.toString());
        for(int i=0; i< logs.size(); i++){
            JSONObject userLog = (JSONObject) logs.get(i);
            ParticipantLog pl = new ParticipantLog();
            pl.setUsername(userLog.get("USERNAME").toString());
            Long userId = Long.parseLong(userLog.get("apiUserId").toString());
            String name = memberService.findById(userId).getName();
            String email = memberService.findById(userId).getEmail();
            pl.setName(name);
            pl.setEmail(email);
            pl.setType(userLog.get("logType").toString());
            pl.setLogDate(userLog.get("regDate").toString());
            pl.setDeviceInfo(userLog.get("deviceType").toString().substring(0,18));
            pl.setOsInfo(userLog.get("osInfo").toString());

            plogs.add(pl);
        }
        return plogs;
    }

    public JSONObject userLogAPI(String roomId) throws org.json.simple.parser.ParseException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://biz-dev-api.gooroomee.com/api/v1/log/room/roomUser")
                .queryParam("type", "roomId")
                .queryParam("roomId", roomId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type",  "application/x-www-form-urlencoded; charset=utf-8");
        headers.add("X-GRM-AuthToken", "1aa271b4af192d114c55199a81cc211093b170481d15119584");

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET, //{요청할 방식}
                entity, // {요청할 때 보낼 데이터}
                String.class //{요청시 반환되는 데이터 타입}
        );

        log.info(response.getBody());
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(response.getBody());

        return jsonObject;
    }
}
