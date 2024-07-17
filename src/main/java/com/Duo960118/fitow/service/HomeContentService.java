package com.Duo960118.fitow.service;

import com.Duo960118.fitow.entity.HomeContentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface HomeContentService {
    @Transactional
    Page<HomeContentDto.HomeContentInfoDto> getAllHomeContent(Pageable pageable);

    @Transactional
    HomeContentDto.HomeContentInfoDto getHomeContentInfo(UUID uuid);

    @Transactional
    HomeContentDto.HomeContentInfoDto postHomeContent(HomeContentDto.PostHomeContentRequestDto postHomeContentRequest);

    @Transactional
    HomeContentDto.HomeContentInfoDto editHomeContent(UUID uuid, HomeContentDto.EditHomeContentRequestDto editHomeContentRequest);

    @Transactional
    void deleteHomeContent(UUID uuid);

    @Transactional
    List<HomeContentDto.HomeContentInfoDto> getActiveHomeContent();
}
