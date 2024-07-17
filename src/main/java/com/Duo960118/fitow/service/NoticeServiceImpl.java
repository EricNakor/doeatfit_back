package com.Duo960118.fitow.service;

import com.Duo960118.fitow.entity.NoticeDto;
import com.Duo960118.fitow.entity.NoticeEntity;
import com.Duo960118.fitow.mapper.NoticeMapper;
import com.Duo960118.fitow.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class NoticeServiceImpl implements NoticeService {
    private final NoticeRepository noticeRepository;
    private final UserService userService;

    @Override
    public UUID postNotice(NoticeDto.PostNoticeRequestDto postNoticeRequest) {
        NoticeEntity noticeEntity = NoticeEntity.builder()
                .userEntity(userService.findByNickName(postNoticeRequest.getNickName()))
                .title(postNoticeRequest.getTitle())
                .content(postNoticeRequest.getContent())
                .noticeCategory(postNoticeRequest.getCategory())
                .build();
        this.noticeRepository.save(noticeEntity);
        return noticeEntity.getUuidEntity().getUuid();
    }

    @Override
    public boolean deleteNotice(UUID uuid) {
        noticeRepository.deleteByUuidEntityUuid(uuid);
        return !noticeRepository.existsByUuidEntityUuid(uuid);
    }

    @Override
    public boolean editNotice(UUID uuid,NoticeDto.PostNoticeRequestDto editNoticeRequest) {
        try {
            NoticeEntity noticeEntity = noticeRepository.findByUuidEntityUuid(uuid).orElseThrow(() -> new RuntimeException("존재하지 않는 게시물"));

            noticeEntity.updateNotice(editNoticeRequest);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public NoticeDto.NoticeDetailDto getNoticeDetail(UUID uuid) {
        NoticeEntity noticeEntity = noticeRepository.findByUuidEntityUuid(uuid).orElseThrow(() -> new RuntimeException("존재하지 않는 게시물"));

        String nickName = noticeEntity.getUserEntity().getNickName();

        return new NoticeDto.NoticeDetailDto(noticeEntity.getUuidEntity().getUuid(),noticeEntity.getTitle(), noticeEntity.getContent(), noticeEntity.getNoticeCategory()
                ,nickName, noticeEntity.getCreatedAt(), noticeEntity.getEditedAt());
    }

    // stream?
    // Java 8부터 추가된 기술로 람다를 활용해 배열과 컬렉션을 함수형으로 간단하게 처리할 수 있는 기술이다.
    // 기존의 for문과 Iterator를 사용하면 코드가 길어져서 가독성과 재사용성이 떨어지며 데이터 타입마다 다른 방식으로 다뤄야 하는 불편함이 있다.
    // 스트림은 데이터 소스를 추상화하고, 데이터를 다루는데 자주 사용되는 메소드를 정의해 놓아서 데이터 소스에 상관없이 모두 같은 방식으로 다룰 수 있으므로 코드의 재사용성이 높아진다.
    @Override
    public List<NoticeDto.NoticeInfoDto>searchNotice(NoticeEntity.NoticeCategoryEnum category, String searchString) {
        return noticeRepository.findByNoticeCategoryAndTitleContaining(category,searchString).stream().map(NoticeMapper::entityToNoticeInfoDto).collect(Collectors.toList());
    }

    @Override
    public List<NoticeDto.NoticeInfoDto> getNotices(Sort sort) {
        return noticeRepository.findAll(sort).stream().map(NoticeMapper::entityToNoticeInfoDto).collect(Collectors.toList());
    }

    @Override
    public List<NoticeDto.NoticeInfoDto> getNoticePage(PageRequest pageRequest) {
        return noticeRepository.findAll(pageRequest).stream().map(NoticeMapper::entityToNoticeInfoDto).collect(Collectors.toList());
    }

    @Override
    public void updateForeinKeysNull(Long userId) {
        List<NoticeEntity> noticeEntities = noticeRepository.findByUserEntityUserId(userId);
        // 외래 키를 null로 설정
        for (NoticeEntity noticeEntity : noticeEntities) {
            noticeEntity.updateUserEntity(null);
        }
    }
}
