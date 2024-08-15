package com.Duo960118.fitow.controller;

import com.Duo960118.fitow.entity.HomeContentDto;
import com.Duo960118.fitow.entity.HomeContentEntity;
import com.Duo960118.fitow.entity.StatusResponseDto;
import com.Duo960118.fitow.response.ApiResponse;
import com.Duo960118.fitow.service.HomeContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class HomeContentApiController {
    private final HomeContentService homeContentService;

    // 홈 컨텐츠 작성
    @PostMapping("/def-cms/home-contents")
    public ApiResponse<HomeContentDto.HomeContentInfoDto> postHomeContent(@RequestBody HomeContentDto.PostHomeContentRequestDto postHomeContentRequest) {
        return ApiResponse.success(homeContentService.postHomeContent(postHomeContentRequest));
    }

    // 홈 컨텐츠 수정
    @PutMapping("/def-cms/home-contents/{uuid}")
    public ApiResponse<HomeContentDto.HomeContentInfoDto> editHomeContent(@PathVariable("uuid") UUID uuid, @RequestBody HomeContentDto.EditHomeContentRequestDto editHomeContentRequest) {
        editHomeContentRequest.setUuid(uuid);
        return ApiResponse.success(homeContentService.editHomeContent( editHomeContentRequest));
    }

    // 홈 컨텐츠 삭제
    @DeleteMapping("/def-cms/home-contents/{uuid}")
    public ApiResponse<Object> deleteHomeContent(@PathVariable("uuid") UUID uuid) {
        homeContentService.deleteHomeContent(uuid);
        return ApiResponse.success(null);
    }

    // 홈 컨텐츠 전체 조회
    @GetMapping("/def-cms/home-contents")
    public ApiResponse<Page<HomeContentDto.HomeContentInfoDto>> homeContents(@PageableDefault(page = 0, size = 10, sort = "homeContentId", direction = Sort.Direction.DESC) Pageable pageable) {
        return ApiResponse.success(homeContentService.getAllHomeContent(pageable));
    }

    // 홈 컨텐츠 자세히 보기
    @GetMapping("/def-cms/home-contents/{uuid}")
    public ApiResponse<HomeContentDto.HomeContentInfoDto> getContentDetail(@PathVariable("uuid") UUID uuid) {
        return ApiResponse.success(homeContentService.getHomeContentInfo(uuid));
    }

    // 홈 컨텐트 카테고리 enum 요청
    @GetMapping("/def-cms/home-contents/category-enum")
    public ApiResponse<HomeContentEntity.HomeContentCategoryEnum[]> getHomeContentCategoryEnums() {
        return ApiResponse.success(HomeContentEntity.HomeContentCategoryEnum.values());
    }

    // 활성화 된 홈 컨텐츠 조회
    @GetMapping("home-contents")
    public ApiResponse<List<HomeContentDto.HomeContentInfoDto>> getActiveHomeContents() {
        return ApiResponse.success(homeContentService.getActiveHomeContent());
    }
}
