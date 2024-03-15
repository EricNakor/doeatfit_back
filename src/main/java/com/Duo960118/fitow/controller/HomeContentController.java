package com.Duo960118.fitow.controller;

import com.Duo960118.fitow.entity.HomeContentEntity;
import com.Duo960118.fitow.entity.HomeContentDto;
import com.Duo960118.fitow.service.HomeContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
public class HomeContentController {
    private final HomeContentService homeContentService;
    @GetMapping("def-cms/home-contents/post")
    public String postHomeContent(Model model){
        model.addAttribute("homeContentCategoryEnums", HomeContentEntity.HomeContentCategoryEnum.values());
        return "/def-cms/postHomeContent";
    }

    @GetMapping("def-cms/home-contents/{uuid}")
    public String editHomeContent(@PathVariable("uuid") UUID uuid,
                                  Model model){
        HomeContentDto.HomeContentInfoDto homeContentInfo = homeContentService.getHomeContentInfo(uuid);
        model.addAttribute("homeContent",homeContentInfo);
        model.addAttribute("homeContentCategoryEnums", HomeContentEntity.HomeContentCategoryEnum.values());

        return "/def-cms/editHomeContent";
    }
}