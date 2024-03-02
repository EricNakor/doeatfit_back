package com.Duo960118.fitow.controller;

import com.Duo960118.fitow.entity.NoticeDto;
import com.Duo960118.fitow.entity.NoticeEntity;
import com.Duo960118.fitow.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
public class NoticeController {
    private final NoticeService noticeService;

    // 공지 전체 조회
    @GetMapping("notices")
    public String notices(Model model)
    {
        // 기본 정렬은 noticeId 기준 내림차순으로
        // todo: page navigator
        Sort sort = Sort.by("createdAt").descending();
        PageRequest pageRequest = PageRequest.of(0, 10).withSort(sort);
        List<NoticeDto.NoticeInfoDto> noticeInfos = noticeService.getNoticePage(pageRequest);

        model.addAttribute("categoryEnums", NoticeEntity.NoticeCategoryEnum.values());
        model.addAttribute("notices",noticeInfos);

        return "/notice/notices";
    }

    // 이 uuid를 가진 공지 조회
    @GetMapping("notices/{uuid}")
    public String getNoticeDetail(@PathVariable("uuid") UUID uuid,Model model){
        model.addAttribute("noticeDetail",noticeService.getNoticeDetail(uuid));

        return "/notice/noticeDetail";
    }

    // 공지 작성 페이지
    @GetMapping("notices/post")
    public String postNotice(@ModelAttribute("notice") NoticeDto.PostNoticeRequestDto notice, Model model)
    {
        model.addAttribute("categoryEnums", NoticeEntity.NoticeCategoryEnum.values());

        return "/notice/postNotice";
    }

    // 공지 수정 페이지
    @GetMapping("notices/edit/{uuid}")
    public String editNotice(@PathVariable("uuid") UUID uuid, Model model)
    {
        NoticeDto.NoticeDetailDto noticeDetail = noticeService.getNoticeDetail(uuid);
        model.addAttribute("notice",noticeDetail);

        model.addAttribute("categoryEnums", NoticeEntity.NoticeCategoryEnum.values());

        return "/notice/editNotice";
    }
}
