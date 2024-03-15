package com.Duo960118.fitow.service;

import com.Duo960118.fitow.entity.ReportDto;
import com.Duo960118.fitow.entity.ReportEntity;
import com.Duo960118.fitow.entity.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminFacade {
    private final UserService userService;
    private final NoticeService noticeService;
    private final ReportService reportService;
    private final WorkoutService workoutService;

//
//    public List<UserDto.UserInfoDto> getAllUser(){
//        return userService.getAllUser();
//    }
//
//    public List<ReportDto.ReportInfoDto> getAllReport(Pageable pageable){
//
//        return reportService.getAllReport(pageRequest);
//    }
//
//    public Page<ReportDto.ReportInfoDto> searchReport(ReportEntity.ReportStatusEnum reportStatusEnum, ReportEntity.ReportCategoryEnum reportCategoryEnum, String email, Pageable pageable) {
//        return reportService.searchReport(reportStatusEnum, reportCategoryEnum, email, pageable);
//    }

}
