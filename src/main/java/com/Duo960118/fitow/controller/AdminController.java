package com.Duo960118.fitow.controller;

import com.Duo960118.fitow.entity.*;
import com.Duo960118.fitow.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("def-cms")
public class AdminController {
    private final AdminFacade adminFacade;
    // 여러 서비스 쓰는 경우에만 퍼사드 쓰는게 좋을거 같음.
    // 모든 user 조회 같은 경우 getAllUser 이 메서드 하나만 쓰는데 admin facade에 쓸데없이 재정의 하게 되는듯
    // (admin facade에 정의된 getAllUser와 user service에 정의된 getAllUser)
    // 예를 들면 유저 서비스랑 리포트 서비스를 동시에 써야하는 기능(method)를 구현해야 하는 경우
    // 혹은 신고글에 답변하자 마자 이메일 알림을 보내는 기능 ( report + email
    // 그런거 아니라면 그냥 controller에서 해당 서비스 바로 가져다 쓰는게 좋을 듯

    private final ReportService reportService;
    private final UserService userService;
    private final NoticeService noticeService;
    private final WorkoutService workoutService;
    private final HomeContentService homeContentService;

    // 어드민 인덱스
    @GetMapping( "")
    public String admin() {
        return "/layout/defCmsIndex";
    }
    // 유저 관리
    @GetMapping( "users")
    public String manageUser(Model model) {
        List<UserDto.UserInfoDto> users =  userService.getAllUser();

        model.addAttribute("users", users);

        return "/def-cms/manageUsers";
    }

    // 유저 자세히 보기
    @GetMapping("/users/{email}")
    public String managerUser(@PathVariable("email") String email, Model  model){
        UserDto.UserInfoDto userInfo = userService.getUserInfo(email);

        if (userInfo.getProfileImg() == null) {
            // 없으면 기본 이미지로 profileImg 설정
            userInfo.setProfileImg("default.png");
        }

        model.addAttribute("userInfo",userInfo);
        model.addAttribute("userRoles",UserEntity.UserRoleEnum.values());
        // 프로필 이미지 있는지 확인해

        return "/def-cms/manageUserDetail";
    }
    // 신고 관리
    @GetMapping( "reports")
    public String manageReport(
                               @PageableDefault(size = 10, sort = "reportDate", direction = Sort.Direction.DESC) Pageable pageable,
                               @RequestParam( value="status", required = false) ReportEntity.ReportStatusEnum status,
                               @RequestParam( value="category", required = false) ReportEntity.ReportCategoryEnum category,
                               @RequestParam( value="email", required = false) String email,
                               Model model) {
        PageRequest pageRequest = (PageRequest) pageable;
        List<ReportDto.ReportInfoDto> reportInfoDtoList = reportService.getAllReport(pageRequest);

        model.addAttribute("reportCategoryEnums", ReportEntity.ReportCategoryEnum.values());
        model.addAttribute("reportStatusEnums", ReportEntity.ReportStatusEnum.values());
        model.addAttribute("reports", reportInfoDtoList);
        model.addAttribute("searchReport", reportService.searchReport(status, category, email, pageable));

        return "/def-cms/manageReports";
    }
    
    // 공지 관리
    @GetMapping( "notices")
    public String manageNotice(@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                               Model model) {
        PageRequest pageRequest = (PageRequest) pageable;
        List<NoticeDto.NoticeInfoDto> noticeInfos = noticeService.getNoticePage(pageRequest);

        model.addAttribute("categoryEnums", NoticeEntity.NoticeCategoryEnum.values());
        model.addAttribute("notices",noticeInfos);

        return "/def-cms/manageNotices";
    }

    // 운동 관리
    @GetMapping( "workouts")
    public String manageWorkout(@PageableDefault(size = 10, sort = "workoutId", direction = Sort.Direction.DESC) Pageable pageable,
                                Model model) {
        PageRequest pageRequest = (PageRequest) pageable;
        List<WorkoutDto.WorkoutDetailDto> workouts = workoutService.getAllWorkout(pageRequest);

        model.addAttribute("muscleEnums", WorkoutEntity.MuscleEnum.values());
        model.addAttribute("workouts",workouts);

        return "/def-cms/manageWorkouts";
    }
    
    // 홈 컨텐츠 관리
    @GetMapping("home-contents")
    public String manageHomeContent(@PageableDefault(size = 10, sort = "homeContentId", direction = Sort.Direction.DESC) Pageable pageable,
                                    Model model){
        Page<HomeContentDto.HomeContentInfoDto> homeContents = homeContentService.getAllHomeContent(pageable);

        model.addAttribute("homeContents",homeContents);

        return "/def-cms/manageHomeContent";
    }
}
