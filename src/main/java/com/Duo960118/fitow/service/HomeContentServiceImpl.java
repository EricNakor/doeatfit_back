package com.Duo960118.fitow.service;

import com.Duo960118.fitow.entity.HomeContentEntity;
import com.Duo960118.fitow.entity.HomeContentDto;
import com.Duo960118.fitow.mapper.HomeContentMapper;
import com.Duo960118.fitow.repository.HomeContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class HomeContentServiceImpl implements HomeContentService {
    private final HomeContentRepository homeContentRepository;

    @Override
    public Page<HomeContentDto.HomeContentInfoDto> getAllHomeContent(Pageable pageable) {
        return homeContentRepository.findAll(pageable).map(HomeContentMapper::entityToHomeContentInfoDto);
    }

    @Override
    public HomeContentDto.HomeContentInfoDto getHomeContentInfo(UUID uuid) {
        return HomeContentMapper.entityToHomeContentInfoDto(homeContentRepository.findByUuidEntityUuid(uuid).orElseThrow(() -> new RuntimeException("존재하지 않는 home content")));
    }

    @Override
    public HomeContentDto.HomeContentInfoDto postHomeContent(HomeContentDto.PostHomeContentRequestDto postHomeContentRequest) {
        List<HomeContentEntity> homeContentEntities = homeContentRepository.findByCategory(postHomeContentRequest.getCategory());

        if (postHomeContentRequest.getIsBeingUsed()) {
            // category 같은 모든 엔티티`isBeingUsed` 값을 `false`로
            for (HomeContentEntity h : homeContentEntities) {
                h.updateIsBeingUsed(false);
            }
        }

        HomeContentEntity homeContentEntity = HomeContentEntity.builder()
                .category(postHomeContentRequest.getCategory())
                .isBeingUsed(postHomeContentRequest.getIsBeingUsed())
                .content(postHomeContentRequest.getContent())
                .build();
        homeContentRepository.save(homeContentEntity);

        return HomeContentMapper.entityToHomeContentInfoDto(homeContentEntity);
    }

    @Override
    public HomeContentDto.HomeContentInfoDto editHomeContent(UUID uuid, HomeContentDto.EditHomeContentRequestDto editHomeContentRequest) {
        if (editHomeContentRequest.getIsBeingUsed()) {
            List<HomeContentEntity> homeContentEntities = homeContentRepository.findByCategory(editHomeContentRequest.getCategory());

            // 만약 빈 리스트라면 아무것도 안함
            // 모든 엔티티`isBeingUsed` 값을 `false`로
            for (HomeContentEntity h : homeContentEntities) {
                h.updateIsBeingUsed(false);
            }
        }

        HomeContentEntity homeContentEntity = homeContentRepository.findByUuidEntityUuid(uuid).orElseThrow(() -> new RuntimeException("존재하지 않는 home content"));
        homeContentEntity.updateHomeContent(editHomeContentRequest);

        return HomeContentMapper.entityToHomeContentInfoDto(homeContentEntity);
    }

    @Override
    public void deleteHomeContent(UUID uuid) {
        homeContentRepository.deleteByUuidEntityUuid(uuid);
    }

    @Override
    public List<HomeContentDto.HomeContentInfoDto> getActiveHomeContent() {
        List<HomeContentEntity> activeHomeContents = homeContentRepository.findByIsBeingUsedTrue();

        return activeHomeContents.stream()
                .map(HomeContentMapper::entityToHomeContentInfoDto)
                .collect(Collectors.toList());
    }

//    private HomeContentDto.HomeContentInfoDto convertEntityToDto(HomeContentEntity entity) {
//        return HomeContentDto.HomeContentInfoDto.builder()
//                .uuid(entity.getUuidEntity().getUuid())
//                .category(entity.getCategory())
//                .isBeingUsed(entity.getIsBeingUsed())
//                .content(entity.getContent())
//                .createdAt(entity.getCreatedAt())
//                .editedAt(entity.getEditedAt())
//                .build();
//    }

}
