package com.Duo960118.fitow.service;

import com.Duo960118.fitow.entity.HomeContentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface HomeContentService {
    @Transactional
    public Page<HomeContentDto.HomeContentInfoDto> getAllHomeContent(Pageable pageable);

    @Transactional
    public HomeContentDto.HomeContentInfoDto getHomeContentInfo(UUID uuid);

    @Transactional
    public HomeContentDto.HomeContentInfoDto postHomeContent(HomeContentDto.PostHomeContentRequestDto postHomeContentRequest);

    @Transactional
    public HomeContentDto.HomeContentInfoDto editHomeContent(UUID uuid, HomeContentDto.EditHomeContentRequestDto editHomeContentRequest);

    @Transactional
    public void deleteHomeContent(UUID uuid);

}
