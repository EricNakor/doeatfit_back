package com.Duo960118.fitow.controller;

import com.Duo960118.fitow.entity.CustomUserDetails;
import com.Duo960118.fitow.entity.ReportDto;
import com.Duo960118.fitow.entity.ReportEntity;
import com.Duo960118.fitow.entity.StatusResponseDto;
import com.Duo960118.fitow.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReportApiController {
    private final ReportService reportService;

    // 신고 및 문의 리스트 개인용
    @GetMapping("reports/my-reports")
    public ResponseEntity<List<ReportDto.ReportInfoDto>> getReports(@RequestParam("pageNumber") int pageNumber, @RequestParam("pageSize") int pageSize, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String email = customUserDetails.getUsername();
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        return ResponseEntity.ok().body(reportService.getReports(email, pageRequest));
    }

    // 신고 및 문의 자세히 보기
    @GetMapping("reports/{uuid}")
    public ReportDto.ReportDetailDto getReportDetail(@PathVariable("uuid") UUID uuid) {
        return reportService.getReportDetail(uuid);
    }

    // 신고 및 문의 작성
    @PostMapping("reports")
    public ResponseEntity<ReportDto.PostReportResponseDto> postReport(@RequestPart(value = "reportFile", required = false) List<MultipartFile> multipartFile,
                                                                      @RequestPart(value = "postReportRequestDto") ReportDto.PostReportRequestDto postReportRequest,
                                                                      @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        postReportRequest.setEmail(customUserDetails.getUserInfo().getEmail());
        postReportRequest.setReportStatus(ReportEntity.ReportStatusEnum.TODO);
        postReportRequest.setReply("");
//        my sql에서 빈문자열로 저장 후 프론트 작업 시 js를 활용해 if(!String)해서 default 문구 지정
//        참고 oracle db는 빈문자열과 null 모두 null로 저장된다고 한다.
        return ResponseEntity.ok().body(new ReportDto.PostReportResponseDto(reportService.postReport(multipartFile, postReportRequest)));
    }

    // 신고 삭제
    @DeleteMapping("reports/{uuid}")
    public ResponseEntity<StatusResponseDto> deleteReport(@PathVariable("uuid") UUID uuid) {
        reportService.deleteReport(uuid);
        return ResponseEntity.ok().body(new StatusResponseDto(reportService.deleteReport(uuid)));
    }

    // 신고 답변
    @PutMapping("reports/reply/{uuid}")
    public ResponseEntity<StatusResponseDto> replyReport(@PathVariable("uuid") UUID uuid,
                                                         @RequestPart(value = "replyReportDto") ReportDto.ReplyReportDto replyReport,
                                                         @RequestPart(value = "replyFiles", required = false) List<MultipartFile> multipartFile) {
        return ResponseEntity.ok().body(new StatusResponseDto(reportService.replyReport(uuid, replyReport, multipartFile)));
    }

    // 신고 전체 문의 리스트
    @GetMapping("reports")
    public ResponseEntity<List<ReportDto.ReportInfoDto>> reports(@RequestParam("pageNumber") int pageNumber, @RequestParam("pageSize") int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        List<ReportDto.ReportInfoDto> result = reportService.getAllReport(pageRequest);

        return ResponseEntity.ok().body(result);
    }

    // 필터 검색
    @GetMapping("reports/search")
    public ResponseEntity<Page<ReportDto.ReportInfoDto>> searchReports(
            @RequestParam(value = "status", required = false) ReportEntity.ReportStatusEnum status,
            @RequestParam(value = "category", required = false) ReportEntity.ReportCategoryEnum category,
            @RequestParam(value = "email", required = false) String email,
            @PageableDefault(size = 10, sort = "reportDate", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<ReportDto.ReportInfoDto> result = reportService.searchReport(status, category, email, pageable);

        return ResponseEntity.ok().body(reportService.searchReport(status, category, email, pageable));
    }
}
