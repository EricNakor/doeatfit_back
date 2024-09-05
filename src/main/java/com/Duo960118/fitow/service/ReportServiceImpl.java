package com.Duo960118.fitow.service;

import com.Duo960118.fitow.config.UploadConfig;
import com.Duo960118.fitow.entity.ReportDto;
import com.Duo960118.fitow.entity.ReportEntity;
import com.Duo960118.fitow.mapper.ReportMapper;
import com.Duo960118.fitow.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReportServiceImpl implements ReportService {
    private static final Logger log = LoggerFactory.getLogger(ReportServiceImpl.class);
    private final UserService userService;
    private final ReportRepository reportRepository;
    private final UploadConfig uploadConfig;

    @Value("${spring.report_file_dir}")
    private String reportFileDir;

    @Value("${spring.reply_file_dir}")
    private String replyFileDir;

    // 파일명 추출 및 저장
    private List<String> extractFileNameList(List<MultipartFile> files, String savePath) throws IOException {
        List<String> filenameList = new ArrayList<>();
        for (MultipartFile multipartFile : files) {
            String fileName = multipartFile.getOriginalFilename();
            String fileExt = Objects.requireNonNull(fileName).substring(fileName.lastIndexOf("."));
            String fileRename = UUID.randomUUID() + fileExt;
            filenameList.add(fileRename);

            File file = new File(savePath + "\\" + fileRename);
            multipartFile.transferTo(file);
        }
        return filenameList;
    }

    // report 작성
    @Override
    public ReportDto.PostReportResponseDto  postReport(ReportDto.PostReportRequestDto postReportRequest) throws IOException {

        if (postReportRequest.getReportFiles() != null) {
            List<String> reportFileNameList = extractFileNameList(postReportRequest.getReportFiles(), reportFileDir);
            postReportRequest.setReportFileNames(reportFileNameList);
        }

        ReportEntity reportEntity = ReportEntity.builder()
                .reportCategory(ReportEntity.ReportCategoryEnum.fromString(postReportRequest.getReportCategory()))
                .title(postReportRequest.getTitle())
                .userEntity(userService.findByEmail(postReportRequest.getEmail()))
                .content(postReportRequest.getContent())
                .reportStatus(ReportEntity.ReportStatusEnum.fromString(postReportRequest.getReportStatus()))
                .reply(postReportRequest.getReply())
                .reportFiles(postReportRequest.getReportFileNames())
                .build();
        this.reportRepository.save(reportEntity);

        return ReportDto.PostReportResponseDto.builder()
                .reportCategory(reportEntity.getReportCategory())
                .title(reportEntity.getTitle())
                .content(reportEntity.getContent())
                .email(reportEntity.getUserEntity().getEmail())
                .reportFileNames(reportEntity.getReportFiles())
                .reportStatus(reportEntity.getReportStatus())
                .reply(reportEntity.getReply())
                .uuid(reportEntity.getUuidEntity().getUuid())
                .build();
    }

    // report 삭제
    @Override
    public void deleteReport(UUID uuid) {
        reportRepository.deleteByUuidEntityUuid(uuid);
    }

    // Report 상세내용
    @Override
    public ReportDto.ReportDetailDto getReportDetail(UUID uuid) {
        // 예외: 존재하지 않는 문의
        ReportEntity reportEntity = reportRepository.findByUuidEntityUuid(uuid).orElseThrow(() -> new NoSuchElementException("존재하지 않는 문의" + uuid));

        String email;
        if(reportEntity.getUserEntity()==null){
            email="탈퇴한 사용자";
        }
        email=reportEntity.getUserEntity().getEmail();
        // 생성자는 파라미터 순서가 일치해야 함 > 순서수정
        return new ReportDto.ReportDetailDto(
                reportEntity.getUuidEntity().getUuid(),
                reportEntity.getReportCategory(),
                reportEntity.getTitle(),
                email,
                reportEntity.getContent(),
                reportEntity.getReportDate(),
                reportEntity.getReportStatus(),
                reportEntity.getReply(),
                reportEntity.getReplyDate(),
                reportEntity.getReportFiles(),
                reportEntity.getReplyFiles());
    }

    // 조회한 회원의 Report 리스트, 최근 문의사항 정렬
    @Override
    public Page<ReportDto.ReportInfoDto> getReports(ReportDto.GetReportsRequestDto getReportsRequest) {
        Long userId = userService.findByEmail(getReportsRequest.getEmail()).getUserId();
        Page<ReportEntity> reportEntityList = reportRepository.findByUserEntityUserIdOrderByReportIdDesc(userId, getReportsRequest.getPageable());

        return reportEntityList
                .map(ReportMapper::entityToReportInfoDto);

        // 람다 함수, stream 공부해보기
        // DB에서 가져올때 Entity로 반환이 되고
        // 구현한 기능은 Dto에 담아 전달을 해야하니
        // 형식을 바꾸기 위해 Mapper를 활용
        // 받은 entity형식의 db를 dto로 변환 후 이를 리스트 형태로 반환
    }

    // admin 을 위한 전체 Report 리스트
    @Override
    public Page<ReportDto.ReportInfoDto> getAllReport(Pageable pageable) {
        return reportRepository.findAll(pageable)
                .map(ReportMapper::entityToReportInfoDto);
    }

    // Report 답변
    @Override
    public ReportDto.ReplyReportResponseDto replyReport(ReportDto.ReplyReportRequestDto replyReportRequest) throws IOException {
        // 예외: 존재하지 않는 문의
        ReportEntity reportEntity =
                reportRepository.findByUuidEntityUuid(replyReportRequest.getUuid())
                        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 문의"+ replyReportRequest.getUuid()));
        if (replyReportRequest.getReplyFiles() != null) {
            List<String> replyFileNameList = extractFileNameList(replyReportRequest.getReplyFiles(), replyFileDir);
            replyReportRequest.setReplyFileNames(replyFileNameList);
        }
        reportEntity.replyReport(replyReportRequest);
        return new ReportDto.ReplyReportResponseDto(ReportEntity.ReportStatusEnum.fromString(replyReportRequest.getReportStatus()), replyReportRequest.getReply(), replyReportRequest.getReplyFileNames());
    }

    // Status 검색 (filter)
    // Category 검색 (filter)
    // email 검색 (String)
    @Override
    public Page<ReportDto.ReportInfoDto> searchReport(ReportDto.SearchReportRequestDto searchReportRequest) {
        List<ReportDto.ReportInfoDto> searchResults = reportRepository.findBySearchReportRequest(searchReportRequest)
                .stream()
                .map(ReportMapper::entityToReportInfoDto)
                .toList();

        return new PageImpl<>(searchResults);
    }

    // 신고 및 문의 첨부파일
    @Override
    public Resource loadReportAttachmentImg(String filename) {
        return new FileSystemResource(uploadConfig.getReportAttachmentImgDir() + filename);
    }

    // 답변 첨부파일
    @Override
    public Resource loadReplyAttachmentImg(String filename) {
        return new FileSystemResource(uploadConfig.getReplyAttachmentImgDir() + filename);
    }

    // 사용자 삭제 시 외부키 null로 설정
    @Override
    public void updateForeignKeysNull(Long userId) {
        Pageable pageable = Pageable.unpaged();
        Page<ReportEntity> reportEntities = reportRepository.findByUserEntityUserIdOrderByReportIdDesc(userId, pageable);

        // 외래 키를 null로 설정
        for (ReportEntity reportEntity : reportEntities) {
            reportEntity.updateUserEntity(null);
        }
    }
}
