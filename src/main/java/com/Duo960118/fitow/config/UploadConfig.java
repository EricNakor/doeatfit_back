package com.Duo960118.fitow.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class UploadConfig {
    @Value("${spring.profile_img_dir}")
    private String profileImgDir;

    @Value("${spring.report_file_dir}")
    private String reportAttachmentImgDir;

    @Value("${spring.reply_file_dir}")
    private String replyAttachmentImgDir;
}
