package com.example.rapidboard.web;

import com.example.rapidboard.domain.member.Member;
import com.example.rapidboard.domain.webinar.Webinar;
import com.example.rapidboard.service.MemberService;
import com.example.rapidboard.service.PageService;
import com.example.rapidboard.service.WebinarService;
import com.example.rapidboard.web.dto.PagingDto;
import com.example.rapidboard.web.dto.webinar.ParticipantLog;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.parser.ParseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class AdminController {
    private final WebinarService webinarService;
    private final PageService pageService;
    private final MemberService memberService;

    @GetMapping("/admin/webinar")
    public String adminWebinar(Model model, @PageableDefault(size = 5) Pageable pageable) throws java.text.ParseException {
        Page<Webinar> rooms = webinarService.getRoomList(pageable);
        PagingDto pageDto = pageService.getPageInfo(rooms);
        model.addAttribute("rooms", rooms);
        model.addAttribute("pageDto", pageDto);
        return "admin/webinar";
    }

    @GetMapping("/webinar/participants/{webinarId}")
    public String participants(@PathVariable Long webinarId, Model model) {
        Webinar webinar = webinarService.findById(webinarId);
        List<Member> participants = webinarService.findAllParticipants(webinarId); // 참가자
        List<Member> members = memberService.findRestMember(participants); // 나머지 멤버

        model.addAttribute("room", webinar);
        model.addAttribute("members", members);
        model.addAttribute("participants", participants);
        return "webinar/participantList";
    }

    @GetMapping("/webinar/log/{webinarId}")
    public String log(@PathVariable Long webinarId, Model model) throws ParseException, JsonProcessingException {
        Webinar webinar = webinarService.findById(webinarId);
        List<ParticipantLog> logs = webinarService.userLog(webinar.getRoomId());
        model.addAttribute("logs", logs);
        model.addAttribute("room", webinar);
        return "/webinar/participantLog";
    }

    @GetMapping("/excel/download/{webinarId}")
    public void excelDownload(@PathVariable Long webinarId, HttpServletResponse response) throws IOException{
        Webinar webinar = webinarService.findById(webinarId);
        List<ParticipantLog> logs = webinarService.userLog(webinar.getRoomId());

        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet(webinar.getRoomTitle());
        sheet.setColumnWidth(0, 1500);
        sheet.setColumnWidth(1, 3000);
        sheet.setColumnWidth(2, 3000);
        sheet.setColumnWidth(3, 6000);
        sheet.setColumnWidth(4, 3000);
        sheet.setColumnWidth(5, 6000);
        sheet.setColumnWidth(6, 6000);
        sheet.setColumnWidth(7, 6000);
        Row row = null;
        Cell cell = null;
        int rowNum = 0;

        // Header
        row = sheet.createRow(rowNum++);
        cell = row.createCell(0);
        cell.setCellValue("번호");
        cell = row.createCell(1);
        cell.setCellValue("아이디");
        cell = row.createCell(2);
        cell.setCellValue("이름");

        cell = row.createCell(3);
        cell.setCellValue("이메일");
        cell = row.createCell(4);
        cell.setCellValue("입/퇴장");

        cell = row.createCell(5);
        cell.setCellValue("시간");
        cell = row.createCell(6);
        cell.setCellValue("디바이스 정보");
        cell = row.createCell(7);
        cell.setCellValue("OS");

        // Body
        for (int i=0; i<logs.size(); i++) {
            row = sheet.createRow(rowNum++);
            cell = row.createCell(0);
            cell.setCellValue(i+1);
            cell = row.createCell(1);
            cell.setCellValue(logs.get(i).getUsername());
            cell = row.createCell(2);
            cell.setCellValue(logs.get(i).getName());
            cell = row.createCell(3);
            cell.setCellValue(logs.get(i).getEmail());
            cell = row.createCell(4);
            cell.setCellValue(logs.get(i).getType());
            cell = row.createCell(5);
            cell.setCellValue(logs.get(i).getLogDate().substring(0,25));
            cell = row.createCell(6);
            cell.setCellValue(logs.get(i).getDeviceInfo());
            cell = row.createCell(7);
            cell.setCellValue(logs.get(i).getOsInfo());
        }

        // 컨텐츠 타입과 파일명 지정
        response.setContentType("ms-vnd/excel");
//        response.setHeader("Content-Disposition", "attachment;filename=example.xls");
        response.setHeader("Content-Disposition", "attachment;filename="+ "participantsLog_"+webinar.getWebinarId()+".xlsx");

        // Excel File Output
        wb.write(response.getOutputStream());
        wb.close();
    }
}
