package com.Duo960118.fitow.controller;

import com.Duo960118.fitow.entity.CustomUserDetails;
import com.Duo960118.fitow.entity.NoticeDto;
import com.Duo960118.fitow.entity.NoticeEntity;
import com.Duo960118.fitow.response.ApiResponse;
import com.Duo960118.fitow.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class NoticeApiController {
    private final NoticeService noticeService;

    // ResponseEntity = ResponseHeader + ResponseBody
    // REST API로 만든다면 클라이언트와 서버 간의 통신에 필요한 정보를 제공해야 합니다.
    // It represents an HTTP response, allowing you to customize the response status, headers, and body that your API returns to the client
    // ex. HTTP 200 ok + data{ ... }

    // 기본 정렬은 noticeId 기준 내림차순으로
    //        Sort sort = Sort.by("noticeId").descending();
    //        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize).withSort(sort);

    // 공지 전체 조회
    @GetMapping("notices")
    public ApiResponse<List<NoticeDto.NoticeInfoDto>> notices(
            @PageableDefault(page = 0, size = 10, sort = "noticeId", direction = Sort.Direction.DESC) Pageable pageable) {

        return ApiResponse.success(noticeService.getNoticePage(pageable));
    }

    // 공지 검색
    @GetMapping("notices/search")
    public ApiResponse<List<NoticeDto.NoticeInfoDto>> searchNotice(@RequestParam("noticeCategory") NoticeEntity.NoticeCategoryEnum noticeCategory, @RequestParam("searchString") String searchString) {
        NoticeDto.SearchNoticeRequestDto searchNoticeRequestDto = NoticeDto.SearchNoticeRequestDto.builder()
                .searchString(searchString)
                .category(noticeCategory).build();
        return ApiResponse.success(noticeService.searchNotice(searchNoticeRequestDto));
    }

    // 공지 작성
    @PostMapping("def-cms/notices")
    public ApiResponse<NoticeDto.PostNoticeResponseDto> postNotice(@RequestBody NoticeDto.PostNoticeRequestDto postNoticeRequest, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        postNoticeRequest.setNickName(customUserDetails.getUserInfo().getNickName());
        return ApiResponse.success(new NoticeDto.PostNoticeResponseDto(noticeService.postNotice(postNoticeRequest)));
    }

    // 공지 삭제
    @DeleteMapping("def-cms/notices/{uuid}")
    public ApiResponse<Object> deleteNotice(@PathVariable("uuid") UUID uuid) {
        noticeService.deleteNotice(uuid);
        return ApiResponse.success(null);
    }

    // 공지 수정
    // todo: security config에 admin 권한 확인 필요
    @PutMapping("def-cms/notices/{uuid}")
    public ApiResponse<Object> editNotice(@PathVariable("uuid") UUID uuid, @RequestBody NoticeDto.PostNoticeRequestDto editNoticeRequest) {
        editNoticeRequest.setUuid(uuid);
        noticeService.editNotice(editNoticeRequest);
        return ApiResponse.success(null);
    }

    // 공지 자세히 보기
    // @PathVariable 이란?
    //
    // 경로 변수를 표시하기 위해 메서드에 매개변수에 사용된다.
    // 경로 변수는 중괄호 {id}로 둘러싸인 값을 나타낸다.
    // URL 경로에서 변수 값을 추출하여 매개변수에 할당한다.
    // 기본적으로 경로 변수는 반드시 값을 가져야 하며, 값이 없는 경우 404 오류가 발생한다.
    // 주로 상세 조회, 수정, 삭제와 같은 작업에서 리소스 식별자로 사용된다.
    @GetMapping("notices/{uuid}")
    public ApiResponse<NoticeDto.NoticeDetailDto> getNoticeDetail(@PathVariable("uuid") UUID uuid) {
        return ApiResponse.success(noticeService.getNoticeDetail(uuid));
    }

    // 공지 카테고리 Enum 반환
//    @GetMapping("notices/category-enum")
//    public ApiResponse<NoticeEntity.NoticeCategoryEnum[]> getNoticeCategoryEnums() {
//        return ApiResponse.success(NoticeEntity.NoticeCategoryEnum.values());
//    }
}
