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
import com.example.rapidboard.web.dto.webinar.WebinarResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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
    private final ObjectMapper objectMapper;

    //방 생성
    @Transactional
    public void createRoom(WebinarDto webinarDto) throws ParseException, IOException {
        // -------GMT 포멧으로 시간 변경-------
        String gmtStartDateStr = parseGMT(webinarDto.getStartDate());
        String gmtEndDateStr = parseGMT(webinarDto.getEndDate());

        HttpHeaders headers = getHeaders();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("roomTitle", webinarDto.getRoomTitle());
        params.add("passwd", webinarDto.getPasswd());
        params.add("startDate", gmtStartDateStr);
        params.add("endDate", gmtEndDateStr);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = apiRequest("https://biz-dev-api.gooroomee.com/api/v1/room", HttpMethod.POST, entity);

        log.info("room create response : {}", response.getBody());
        WebinarResponseDto.CreateResponse createResponse = objectMapper.readValue(response.getBody(), WebinarResponseDto.CreateResponse.class);

        if(createResponse.getResultCode().equals("GRM_200")){
            String roomId = createResponse.getData().get("room").getRoomId();
            webinarRepository.save(Webinar.createWebinar(webinarDto, roomId));
        }else{
            throw new CustomException(createResponse.getDescription());
        }

        //{"data":{"room":{"endDate":"Tue Oct 19 2021 18:10:00 +0900","maxJoinCount":4,"roomId":"c847fd61197e466daebd9a9eac77e9af"}},"resultCode":"GRM_200","description":"success"}
    }

    // 전체 웨비나 리스트
    // 종료 시간 지난 것은 종료상태로 변경
    @Transactional
    public Page<Webinar> getRoomList(Pageable pageable) throws ParseException {
        List<Webinar> webinars = webinarRepository.findAllByIsDeleted(0);
        SimpleDateFormat dateFormatCur = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date now = new Date();
        Date parsedNow = dateFormatCur.parse(dateFormatCur.format(now));

        for (Webinar webinar : webinars) {
            Date endDate = dateFormatCur.parse(webinar.getEndDate());
            if(endDate.before(parsedNow)){ // 종료 시간 지남
                webinar.endRoom();
            }
        }
        Page<Webinar> ws = webinarRepository.findAllByIsDeletedOrderByStartDateAsc(0, pageable);
        return ws;
    }

    // OTP 발급 후 입장
    @Transactional
    public String enterWebinar(Long webinarId, Long principalId, String username) throws JsonProcessingException {
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

        HttpHeaders headers = getHeaders();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("roomId", webinar.getRoomId());
        params.add("username", username);
        params.add("roleId", roleId); //speaker, emcee
        params.add("apiUserId", principalId.toString());


        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = apiRequest("https://biz-dev-api.gooroomee.com/api/v1/room/user/otp", HttpMethod.POST, entity);

        log.info(response.getBody());
        //{"resultCode":"GRM_200","data":{"roomUserOtp":{"otp":"4e15093d347a42738c2311443cc6971a70e0d2e3db154d87","expiresIn":30}},"description":"success"}

        WebinarResponseDto.OtpResponse otpResponse = objectMapper.readValue(response.getBody(), WebinarResponseDto.OtpResponse.class);
        if(otpResponse.getResultCode().equals("GRM_200")){
            return otpResponse.getData().get("roomUserOtp").getOtp();
        }
        throw new CustomException("방 입장 오류");
    }


    @Transactional
    public Webinar findById(Long webinarId){
        Webinar webinarEntity = webinarRepository.findById(webinarId).orElseThrow(()->{
            throw new CustomException("Webinar does not exist.");
        });
        if (webinarEntity.getIsDeleted() == 1) throw new CustomException("Webinar does not exist.");
        return webinarEntity;
    }


    //메인 화면에 등록
    @Transactional
    public void register(Long webinarId){
        Webinar currentMainWebinar = webinarRepository.findWebinarByIsMain(1);
        if(currentMainWebinar != null) currentMainWebinar.otherRoom();
        Webinar newMainWebinar = findById(webinarId);
        newMainWebinar.mainRoom();
    }

    // 메인 화면에 등록된 웨비나 찾기
    @Transactional
    public Webinar mainWebinar(){
        return webinarRepository.findWebinarByIsMain(1);
    }

    //웨비나 삭제
    @Transactional
    public void delete(Long webinarId) throws JsonProcessingException {
        Webinar webinarEntity = findById(webinarId);

        // 아직 종료 안됐으면
        if(webinarEntity.getIsStreaming() != 2){
            webinarEntity.endRoom();
            WebinarResponseDto.DeleteResponse deleteResponse = deleteAPI(webinarEntity.getRoomId());
            if(!deleteResponse.getResultCode().equals("GRM_200")) throw new CustomException(deleteResponse.getDescription());
        }

        webinarEntity.deleteWebinar();
        webinarEntity.otherRoom();
    }

    //웨비나 수정
    @Transactional
    public void update(WebinarDto webinarDto, Long webinarId) throws IOException {
        Webinar webinarEntity = findById(webinarId);
        webinarEntity.update(webinarDto);

        //종료됐으면
        if(webinarEntity.getIsStreaming() == 2){
            WebinarResponseDto.DeleteResponse deleteResponse = deleteAPI(webinarEntity.getRoomId());
            if(!deleteResponse.getResultCode().equals("GRM_200")) throw new CustomException(deleteResponse.getDescription());
        }
    }

    //방 종료 API
    public WebinarResponseDto.DeleteResponse deleteAPI(String roomId) throws JsonProcessingException {
        HttpHeaders headers = getHeaders();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("roomId", roomId);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = apiRequest("https://biz-dev-api.gooroomee.com/api/v1/room/" + roomId, HttpMethod.DELETE, entity);

        return objectMapper.readValue(response.getBody(), WebinarResponseDto.DeleteResponse.class);
    }

    //참가자 추가
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

    //참가자 삭제
    @Transactional
    public void deleteParticipant(ParticipantDto participantDto) {
        List<Long> ids = participantDto.getParticipants();
        Webinar webinarEntity = findById(participantDto.getWebinarId());

        for (long id : ids) {
            Participate participate = participateRepository.findByMemberIdAndWebinar(id, webinarEntity);
            participateRepository.delete(participate);
        }
    }

    //참가자 리스트
    @Transactional
    public List<Member> findAllParticipants(Long webinarId){
        Webinar webinar = findById(webinarId);
        List<Member> participants = new ArrayList<>();
        List<Participate> participates = webinar.getParticipates();
        for (Participate p : participates) {
            Member member = memberRepository.findById(p.getMemberId()).get();
            if (member.getIsDeleted() == 1) {
            }
            else participants.add(member);

        }
        return participants;
    }

    //유저 로그
    @Transactional
    public List<ParticipantLog> userLog(String roomId) throws JsonProcessingException {
        List<ParticipantLog> plogs = new ArrayList<>();
        WebinarResponseDto.UserLogResponse userLogResponse = userLogAPI(roomId);
        List<WebinarResponseDto.LogInfo> logs = userLogResponse.getData().getLogList().get(0).getLogs();

        for(int i=0; i< logs.size(); i++){
            WebinarResponseDto.LogInfo logInfo = logs.get(i);
            ParticipantLog pl = new ParticipantLog();
            pl.setUsername(logInfo.getUsername());
            Long userId = Long.parseLong(logInfo.getApiUserId());
            pl.setName(memberService.findById(userId).getName());
            pl.setEmail(memberService.findById(userId).getEmail());
            pl.setType(logInfo.getLogType());
            pl.setLogDate(logInfo.getRegDate());
            pl.setDeviceInfo(logInfo.getDeviceInfo().substring(0,18));
            pl.setOsInfo(logInfo.getOsInfo());
            plogs.add(pl);
        }
        return plogs;
    }

    // 유저로그 API
    public WebinarResponseDto.UserLogResponse userLogAPI(String roomId) throws JsonProcessingException {
        HttpHeaders headers = getHeaders();
        HttpEntity<?> entity = new HttpEntity<>(headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://biz-dev-api.gooroomee.com/api/v1/log/room/roomUser")
                .queryParam("type", "roomId")
                .queryParam("roomId", roomId);

        ResponseEntity<String> response = apiRequest(builder.toUriString(), HttpMethod.GET, entity);

        log.info(response.getBody());
        return objectMapper.readValue(response.getBody(), WebinarResponseDto.UserLogResponse.class);
    }

    //공통 헤더
    public HttpHeaders getHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type",  "application/x-www-form-urlencoded; charset=utf-8");
        headers.add("X-GRM-AuthToken", "1aa271b4af192d114c55199a81cc211093b170481d15119584");
        return headers;
    }

    //공통 API 요청
    public ResponseEntity<String>  apiRequest(String url, HttpMethod method, HttpEntity entity){
        return restTemplate.exchange(
                url,
                method, //{요청할 방식}
                entity, // {요청할 때 보낼 데이터}
                String.class //{요청시 반환되는 데이터 타입}
        );
    }

    //GMT 시간 형식으로 변경
    public String parseGMT(String date) throws ParseException {
        SimpleDateFormat dateFormatCur = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("E MMM dd yyyy HH:mm:ss Z", Locale.US);

        Date curDate = dateFormatCur.parse(date);
        return dateFormatGmt.format(curDate);
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

//    스케줄링 한 번 써봤다 정도
//    @Scheduled(cron = "0/1 * * * * *")
//    public void checkEndDate() throws ParseException {
//        log.info("Current Thread : {}", Thread.currentThread().getName());
//        List<Webinar> webinars = webinarRepository.findAllByIsDeleted(0);
//        SimpleDateFormat dateFormatCur = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        Date now = new Date();
//        Date parsedNow = dateFormatCur.parse(dateFormatCur.format(now));
//        log.info("current time : {}", parsedNow.toString());
//        for (Webinar webinar : webinars) {
//            Date endDate = dateFormatCur.parse(webinar.getEndDate());
//            if(endDate.before(parsedNow)){ // 종료 시간 지남
//                webinar.endRoom();
//            }
//        }
//    }
}
