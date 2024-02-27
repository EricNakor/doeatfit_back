package com.Duo960118.fitow.controller;

import com.Duo960118.fitow.entity.ReportDto;
import com.Duo960118.fitow.entity.ReportEntity;
import com.Duo960118.fitow.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @GetMapping("reports/all")
    public String getAllReports(@RequestParam(value = "status", required = false) ReportEntity.ReportStatusEnum status,
                                @RequestParam(value = "category", required = false) ReportEntity.ReportCategoryEnum category,
                                @RequestParam(value = "email", required = false) String email,
                                Model model) {
        PageRequest pageRequest = PageRequest.of(0, 10);

        List<ReportDto.ReportInfoDto> reportInfoDtoList = reportService.getAllReport(pageRequest);

        model.addAttribute("reportCategoryEnums", ReportEntity.ReportCategoryEnum.values());
        model.addAttribute("reportStatusEnums", ReportEntity.ReportStatusEnum.values());
        model.addAttribute("reports", reportInfoDtoList);
        model.addAttribute("searchReport", reportService.searchReport(status, category, email, pageRequest));

        return "report/reportsAll";
    }

    // 신고 답변
    @GetMapping("reports/reply/{uuid}")
    public String replyReport(@PathVariable("uuid") UUID uuid, Model model, @ModelAttribute("reply")ReportDto.ReplyReportDto replyReport) {
        model.addAttribute("reportDetail", reportService.getReportDetail(uuid));
        ReportDto.ReportDetailDto reportDetail = reportService.getReportDetail(uuid);
        model.addAttribute("report", reportDetail);
        model.addAttribute("reportStatusEnums", ReportEntity.ReportStatusEnum.values());

        return "report/reportReply";
    }
}
