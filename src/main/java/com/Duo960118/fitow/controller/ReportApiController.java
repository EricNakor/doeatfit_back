package com.Duo960118.fitow.controller;

import com.Duo960118.fitow.annotaion.Enum;
import com.Duo960118.fitow.annotaion.File;
import com.Duo960118.fitow.entity.*;
import com.Duo960118.fitow.response.ApiResponse;
import com.Duo960118.fitow.service.ReportService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@Validated
@RequestMapping("/api")
@RequiredArgsConstructor
public class
ReportApiController {
    private final ReportService reportService;

    // 신고 및 문의 리스트 - 유저
    @GetMapping("reports/my-reports")
    public ApiResponse<List<ReportDto.ReportInfoDto>> getReports(
            @PageableDefault(page = 0, size = 10, sort = "reportId", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String email = customUserDetails.getUsername();

        ReportDto.GetReportsRequestDto getReportsRequest = new ReportDto.GetReportsRequestDto(email,pageable);
        return ApiResponse.success(reportService.getReports(getReportsRequest));
    }

    // 신고 및 문의 자세히 보기
    @GetMapping("reports/{uuid}")
    public ApiResponse<ReportDto.ReportDetailDto> getReportDetail(@PathVariable("uuid") UUID uuid) {
        return ApiResponse.success(reportService.getReportDetail(uuid));
    }

    // 신고 및 문의 이미지 가져오기
    @GetMapping("reports/report-img/{filename}")
    public ApiResponse<Resource> getReportAttachmentImg(@PathVariable("filename") String filename) {
        return ApiResponse.successResource(reportService.loadReportAttachmentImg(filename));
    }

    // 답변 이미지 가져오기
    @GetMapping("reports/reply-img/{filename}")
    public ApiResponse<Resource> getReplyAttachmentImg(@PathVariable("filename") String filename) {
        return ApiResponse.successResource(reportService.loadReplyAttachmentImg(filename));
    }

    // 신고 및 문의 작성
    @PostMapping("reports")
    public ApiResponse<ReportDto.PostReportResponseDto> postReport(@RequestPart(value = "reportFile", required = false) List<@File(allowedFileExt = {"jpg", "jpeg", "png"}, fileSizeLimit = 1024 * 1024 * 5)MultipartFile> multipartFile,
                                                                   @Valid @RequestPart(value = "postReportRequestDto") ReportDto.PostReportRequestDto postReportRequest,
                                                                      @AuthenticationPrincipal CustomUserDetails customUserDetails) throws IOException {
        postReportRequest.setEmail(customUserDetails.getUserInfo().getEmail());
        postReportRequest.setReportStatus(ReportEntity.ReportStatusEnum.TODO.toString());
        postReportRequest.setReply("");
        postReportRequest.setReportFiles(multipartFile);
//        my sql에서 빈문자열로 저장 후 프론트 작업 시 js를 활용해 if(!String)해서 default 문구 지정
//        참고 oracle db는 빈문자열과 null 모두 null로 저장된다고 한다.

        return ApiResponse.success(reportService.postReport(postReportRequest));
    }

    // 신고 및 문의 삭제
    @DeleteMapping("def-cms/reports/{uuid}")
    public ApiResponse<Object> deleteReport(@PathVariable("uuid") UUID uuid) {
        reportService.deleteReport(uuid);
        return ApiResponse.success(null);
    }

    // 신고 및 문의 답변
    @PutMapping("def-cms/reports/reply/{uuid}")
    public ApiResponse<ReportDto.ReplyReportResponseDto> replyReport(@PathVariable("uuid") UUID uuid,
                                                         @Valid @RequestPart(value = "replyReportDto") ReportDto.ReplyReportRequestDto replyReportRequest,
                                                         @RequestPart(value = "replyFiles", required = false) List<@File(allowedFileExt = {"jpg", "jpeg", "png"}, fileSizeLimit = 1024 * 1024 * 5)MultipartFile> multipartFile) throws IOException {
        replyReportRequest.setUuid(uuid);
        replyReportRequest.setReplyFiles(multipartFile);
        return ApiResponse.success(reportService.replyReport(replyReportRequest));
    }

    // 신고 및 문의 리스트 - 어드민
    @GetMapping("def-cms/reports")
    public ApiResponse<List<ReportDto.ReportInfoDto>> reports(
            @PageableDefault(page = 0, size = 10, sort = "reportId", direction = Sort.Direction.DESC) Pageable pageable) {
//            @RequestParam("pageNumber") int pageNumber,
//            @RequestParam("pageSize") int pageSize) {
//        Sort sort = Sort.by("reportId").descending();
//        PageRequest pageRequest = PageRequest.of(pageable).withSort(sort);

        return ApiResponse.success(reportService.getAllReport(pageable));
    }

    // 신고 및 문의 검색
    @GetMapping("def-cms/reports/search")
    public ApiResponse<Page<ReportDto.ReportInfoDto>> searchReports(
            @Enum(enumClass = ReportEntity.ReportStatusEnum.class, message = "{Enum.reportStatus}") @RequestParam(value = "status", required = false) String status,
            @Enum(enumClass = ReportEntity.ReportCategoryEnum.class, message = "{Enum.reportCategory}") @RequestParam(value = "category", required = false) String category,
            @Email(message = "{Email.email}") @RequestParam(value = "email", required = false) String email,
            @PageableDefault(page = 0, size = 10, sort = "reportId", direction = Sort.Direction.DESC) Pageable pageable) {
//            @PageableDefault(page = 0, size = 10, sort = "reportId") Pageable pageable) {

        ReportDto.SearchReportRequestDto searchReportRequestDto = ReportDto.SearchReportRequestDto.builder()
                .reportCategory(category.toString())
                .email(email)
                .reportStatus(status.toString())
                .pageable(pageable)
                .build();

        return ApiResponse.success(reportService.searchReport(searchReportRequestDto));
    }

    // 신고 및 문의 카테고리 Enum 반환
//    @GetMapping("reports/category-enum")
//    public ApiResponse<ReportEntity.ReportCategoryEnum[]> getReportCategoryEnum() {
//        return ApiResponse.success(ReportEntity.ReportCategoryEnum.values());
//    }

    // 신고 및 문의 상태 Enum 반환
//    @GetMapping("reports/status-enum")
//    public ApiResponse<ReportEntity.ReportStatusEnum[]> getReportStatusEnum() {
//        return ApiResponse.success(ReportEntity.ReportStatusEnum.values());
//    }
}
