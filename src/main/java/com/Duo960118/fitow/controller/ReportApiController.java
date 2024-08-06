package com.Duo960118.fitow.controller;

import com.Duo960118.fitow.entity.*;
import com.Duo960118.fitow.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReportApiController {
    private final ReportService reportService;

    // 신고 및 문의 리스트 - 유저
    @GetMapping("reports/my-reports")
    public ResponseEntity<List<ReportDto.ReportInfoDto>> getReports(@RequestParam("pageNumber") int pageNumber, @RequestParam("pageSize") int pageSize, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Sort sort = Sort.by("reportId").descending();
        String email = customUserDetails.getUsername();
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize).withSort(sort);

        return ResponseEntity.ok().body(reportService.getReports(email, pageRequest));
    }

    // 신고 및 문의 자세히 보기
    @GetMapping("reports/{uuid}")
    public ReportDto.ReportDetailDto getReportDetail(@PathVariable("uuid") UUID uuid) {
        return reportService.getReportDetail(uuid);
    }

    // 신고 및 문의 이미지 가져오기
    @GetMapping("reports/report-img/{filename}")
    public Resource getReportAttachmentImg(@PathVariable("filename") CommonDto.FileNameDto fileNameDto) {
        return reportService.loadReportAttachmentImg(fileNameDto);
    }

    // 답변 이미지 가져오기
    @GetMapping("reports/reply-img/{filename}")
    public Resource getReplyAttachmentImg(@PathVariable("filename") CommonDto.FileNameDto fileNameDto) {
        return reportService.loadReplyAttachmentImg(fileNameDto);
    }

    // 신고 및 문의 작성
    @PostMapping("reports")
    public ResponseEntity<ReportDto.PostReportResponseDto> postReport(@RequestPart(value = "reportFile", required = false) List<MultipartFile> multipartFile,
                                                                      @RequestPart(value = "postReportRequestDto") ReportDto.PostReportRequestDto postReportRequest,
                                                                      @AuthenticationPrincipal CustomUserDetails customUserDetails) throws IOException {
        postReportRequest.setEmail(customUserDetails.getUserInfo().getEmail());
        postReportRequest.setReportStatus(ReportEntity.ReportStatusEnum.TODO);
        postReportRequest.setReply("");
//        my sql에서 빈문자열로 저장 후 프론트 작업 시 js를 활용해 if(!String)해서 default 문구 지정
//        참고 oracle db는 빈문자열과 null 모두 null로 저장된다고 한다.
        return ResponseEntity.ok().body(new ReportDto.PostReportResponseDto(reportService.postReport(multipartFile, postReportRequest)));
    }

    // 신고 삭제
    @DeleteMapping("def-cms/reports/{uuid}")
    public ResponseEntity<StatusResponseDto> deleteReport(@PathVariable("uuid") UUID uuid) {
        reportService.deleteReport(uuid);
        return ResponseEntity.ok().body(new StatusResponseDto(reportService.deleteReport(uuid)));
    }

    // 신고 답변
    @PutMapping("def-cms/reports/reply/{uuid}")
    public ResponseEntity<StatusResponseDto> replyReport(@PathVariable("uuid") UUID uuid,
                                                         @RequestPart(value = "replyReportDto") ReportDto.ReplyReportDto replyReport,
                                                         @RequestPart(value = "replyFiles", required = false) List<MultipartFile> multipartFile) throws IOException {
        replyReport.setUuid(uuid);
        return ResponseEntity.ok().body(new StatusResponseDto(reportService.replyReport(replyReport, multipartFile)));
    }

    // 신고 문의 리스트 - 어드민
    @GetMapping("def-cms/reports")
    public ResponseEntity<List<ReportDto.ReportInfoDto>> reports(@RequestParam("pageNumber") int pageNumber, @RequestParam("pageSize") int pageSize) {
        Sort sort = Sort.by("reportId").descending();
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize).withSort(sort);
        List<ReportDto.ReportInfoDto> result = reportService.getAllReport(pageRequest);

        return ResponseEntity.ok().body(result);
    }

    // 필터 검색
    @GetMapping("def-cms/reports/search")
    public ResponseEntity<Page<ReportDto.ReportInfoDto>> searchReports(
            @RequestParam(value = "status", required = false) ReportEntity.ReportStatusEnum status,
                                                                       @RequestParam(value = "category", required = false) ReportEntity.ReportCategoryEnum category,
                                                                       @RequestParam(value = "email", required = false) String email,
//            @PageableDefault(page = 0, size = 10, sort = "reportId", direction = Sort.Direction.DESC) Pageable pageable) {
            @PageableDefault(page = 0, size = 10, sort = "reportId") Pageable pageable) {

        ReportDto.SearchReportDto searchReportDto = new ReportDto.SearchReportDto(status,category,email,pageable);
        Page<ReportDto.ReportInfoDto> result = reportService.searchReport(searchReportDto);

//        for (ReportDto.ReportInfoDto reportInfoDto : result) {
//            log.info(reportInfoDto.getTitle());
//        }

        return ResponseEntity.ok().body(result);
    }

    // 신고 및 문의 카테고리 Enum 반환
    @GetMapping("reports/category-enum")
    public ResponseEntity<ReportEntity.ReportCategoryEnum[]> getReportCategoryEnum() {
        return ResponseEntity.ok().body(ReportEntity.ReportCategoryEnum.values());
    }

    // 신고 및 문의 상태 Enum 반환
    @GetMapping("reports/status-enum")
    public ResponseEntity<ReportEntity.ReportStatusEnum[]> getReportStatusEnum() {
        return ResponseEntity.ok().body(ReportEntity.ReportStatusEnum.values());
    }

}
