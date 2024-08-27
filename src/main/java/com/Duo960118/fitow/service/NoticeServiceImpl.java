package com.Duo960118.fitow.service;

import com.Duo960118.fitow.entity.NoticeDto;
import com.Duo960118.fitow.entity.NoticeEntity;
import com.Duo960118.fitow.mapper.NoticeMapper;
import com.Duo960118.fitow.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class NoticeServiceImpl implements NoticeService {
    private static final Logger log = LoggerFactory.getLogger(NoticeServiceImpl.class);
    private final NoticeRepository noticeRepository;
    private final UserService userService;

    // 공지 작성
    @Override
    public UUID postNotice(NoticeDto.PostNoticeRequestDto postNoticeRequest) {
        NoticeEntity noticeEntity = NoticeEntity.builder()
                .userEntity(userService.findByNickName(postNoticeRequest.getNickName()))
                .title(postNoticeRequest.getTitle())
                .content(postNoticeRequest.getContent())
                .noticeCategory(NoticeEntity.NoticeCategoryEnum.fromValue(postNoticeRequest.getCategory()))
                .build();
        this.noticeRepository.save(noticeEntity);
        return noticeEntity.getUuidEntity().getUuid();
    }

    @Override
    public void deleteNotice(UUID uuid) {
        noticeRepository.deleteByUuidEntityUuid(uuid);
//        return !noticeRepository.existsByUuidEntityUuid(uuid);
    }

    @Override
    public void editNotice(NoticeDto.EditNoticeRequestDto editNoticeRequest) {
        // 예외: 존재하지 않는 게시물
        NoticeEntity noticeEntity = noticeRepository.findByUuidEntityUuid(editNoticeRequest.getUuid()).orElseThrow(() -> new NoSuchElementException("존재하지 않는 게시물"+editNoticeRequest.getUuid()));
        noticeEntity.updateNotice(editNoticeRequest);
    }

    @Override
    public NoticeDto.NoticeDetailDto getNoticeDetail(UUID uuid) {
        // 예외: 존재하지 않는 게시물
        NoticeEntity noticeEntity = noticeRepository.findByUuidEntityUuid(uuid).orElseThrow(() -> new NoSuchElementException("존재하지 않는 게시물" + uuid));

        String nickName ;
        if(noticeEntity.getUserEntity()==null) {
            nickName = "탈퇴한 사용자";
        }else{
            nickName = noticeEntity.getUserEntity().getNickName();
        }

        return new NoticeDto.NoticeDetailDto(noticeEntity.getUuidEntity().getUuid(),noticeEntity.getTitle(),
                noticeEntity.getContent(), noticeEntity.getNoticeCategory(),
                nickName, noticeEntity.getCreatedAt(), noticeEntity.getEditedAt());
    }

    // stream?
    // Java 8부터 추가된 기술로 람다를 활용해 배열과 컬렉션을 함수형으로 간단하게 처리할 수 있는 기술이다.
    // 기존의 for문과 Iterator를 사용하면 코드가 길어져서 가독성과 재사용성이 떨어지며 데이터 타입마다 다른 방식으로 다뤄야 하는 불편함이 있다.
    // 스트림은 데이터 소스를 추상화하고, 데이터를 다루는데 자주 사용되는 메소드를 정의해 놓아서 데이터 소스에 상관없이 모두 같은 방식으로 다룰 수 있으므로 코드의 재사용성이 높아진다.
    @Override
    public List<NoticeDto.NoticeInfoDto>searchNotice(NoticeDto.SearchNoticeRequestDto searchNoticeRequest) {
        return noticeRepository.findByNoticeCategoryAndTitleContaining(
                NoticeEntity.NoticeCategoryEnum.fromValue(searchNoticeRequest.getCategory()),
                searchNoticeRequest.getSearchString()).stream().map(NoticeMapper::entityToNoticeInfoDto).collect(Collectors.toList());
    }

//    @Override
//    public List<NoticeDto.NoticeInfoDto> getNotices(Sort sort) {
//        return noticeRepository.findAll(sort).stream().map(NoticeMapper::entityToNoticeInfoDto).collect(Collectors.toList());
//    }

    @Override
    public List<NoticeDto.NoticeInfoDto> getNoticePage(Pageable pageable) {
        return noticeRepository.findAll(pageable).stream().map(NoticeMapper::entityToNoticeInfoDto).collect(Collectors.toList());
    }

    @Override
    public void updateForeignKeysNull(Long userId) {
        List<NoticeEntity> noticeEntities = noticeRepository.findByUserEntityUserId(userId);
        // 외래 키를 null로 설정
        for (NoticeEntity noticeEntity : noticeEntities) {
            noticeEntity.updateUserEntity(null);
        }
    }
}
