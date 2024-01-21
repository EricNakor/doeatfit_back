package com.Duo960118.fitow.notice;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NoticeController {

    @GetMapping("notice")
    public String notice()
    {
        return "notice";
    }

    @GetMapping("noticeDetail")
    public String noticeDetail()
    {
        return "noticeDetail";
    }
}
