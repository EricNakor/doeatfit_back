package com.Duo960118.fitow.controller;

import com.Duo960118.fitow.entity.HomeContentDto;
import com.Duo960118.fitow.entity.StatusResponseDto;
import com.Duo960118.fitow.service.HomeContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/def-cms/home-contents")
public class HomeContentApiController {
    private final HomeContentService homeContentService;

    // 홈 컨텐츠 작성
    @PostMapping("")
    public ResponseEntity<HomeContentDto.HomeContentInfoDto> postHomeContent(@RequestBody HomeContentDto.PostHomeContentRequestDto postHomeContentRequest){
        return ResponseEntity.ok().body(homeContentService.postHomeContent(postHomeContentRequest));
    }

    // 홈 컨텐츠 수정
    @PutMapping("{uuid}")
    public ResponseEntity<HomeContentDto.HomeContentInfoDto> editHomeContent(@PathVariable("uuid") UUID uuid, @RequestBody HomeContentDto.EditHomeContentRequestDto editHomeContentRequest){
        return ResponseEntity.ok().body(homeContentService.editHomeContent(uuid,editHomeContentRequest));
    }

    // 홈 컨텐츠 삭제
    @DeleteMapping("{uuid}")
    public ResponseEntity<StatusResponseDto> deleteHomeContent(@PathVariable("uuid") UUID uuid){
        homeContentService.deleteHomeContent(uuid);
        return ResponseEntity.accepted().build();
    }

    // 홈 컨텐츠 전체 조회
    @GetMapping("")
    public ResponseEntity<Page<HomeContentDto.HomeContentInfoDto>> homeContents(@PageableDefault(size = 10,sort = "homeContentId",direction = Sort.Direction.DESC) Pageable pageable){
        return ResponseEntity
                .ok()
                .body(homeContentService.getAllHomeContent(pageable));
    }

    // 홈 컨텐츠 하나 조회
    @GetMapping("{uuid}")
    public ResponseEntity<HomeContentDto.HomeContentInfoDto> getNoticeDetail(@PathVariable("uuid") UUID uuid){
        return ResponseEntity
                .ok()
                .body(homeContentService.getHomeContentInfo(uuid));
    }
}
