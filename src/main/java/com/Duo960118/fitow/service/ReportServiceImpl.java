package com.Duo960118.fitow.service;

import com.Duo960118.fitow.config.UploadConfig;
import com.Duo960118.fitow.entity.CommonDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
//            try {
//
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
        }
        return filenameList;
    }

    // report 작성
    @Override
    public UUID postReport(List<MultipartFile> multipartFileList, ReportDto.PostReportRequestDto postReportRequest) throws IOException {

        if (multipartFileList != null) {
            List<String> reportFileNameList = extractFileNameList(multipartFileList, reportFileDir);
            postReportRequest.setReportFiles(reportFileNameList);
//            for (MultipartFile multipartFile : multipartFileList) {
//
//                String fileName = multipartFile.getOriginalFilename();
//                String fileExt = Objects.requireNonNull(fileName).substring(fileName.lastIndexOf("."));
//                String fileRename = UUID.randomUUID() + fileExt;
//                reportFileNameList.add(fileRename);
//
//                File file = new File(reportFileDir + "\\" + fileRename);
//                try {
//                    multipartFile.transferTo(file);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }

        }

        ReportEntity reportEntity = ReportEntity.builder()
                .reportCategory(postReportRequest.getReportCategory())
                .title(postReportRequest.getTitle())
                .userEntity(userService.findByEmail(postReportRequest.getEmail()))
                .content(postReportRequest.getContent())
                .reportStatus(postReportRequest.getReportStatus())
                .reply(postReportRequest.getReply())
                .reportFiles(postReportRequest.getReportFiles())
                .build();
        this.reportRepository.save(reportEntity);
        return reportEntity.getUuidEntity().getUuid();
    }

    // report 삭제
    @Override
    public void deleteReport(UUID uuid) {
        reportRepository.deleteByUuidEntityUuid(uuid);
//        return !reportRepository.existsByUuidEntityUuid(uuid);
    }

    // Report 상세내용
    @Override
    public ReportDto.ReportDetailDto getReportDetail(UUID uuid) {
        // 예외: 존재하지 않는 문의
        ReportEntity reportEntity = reportRepository.findByUuidEntityUuid(uuid).orElseThrow(() -> new RuntimeException("존재하지 않는 문의"));

        // 생성자는 파라미터 순서가 일치해야 함 > 순서수정
        return new ReportDto.ReportDetailDto(
                reportEntity.getUuidEntity().getUuid(),
                reportEntity.getReportCategory(),
                reportEntity.getTitle(),
                reportEntity.getUserEntity().getEmail(),
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
    public List<ReportDto.ReportInfoDto> getReports(String email, PageRequest pageRequest) {
        Long userId = userService.findByEmail(email).getUserId();
        List<ReportEntity> reportEntityList = reportRepository.findByUserEntityUserIdOrderByReportIdDesc(userId, pageRequest);

        return reportEntityList
                .stream()
                .map(ReportMapper::entityToReportInfoDto)
                .collect(Collectors.toList());

//        return reportEntityList
//                .stream()
//                .map((ReportEntity reportEntity)-> {
//                    return new ReportDto.ReportInfoDto(
//                            reportEntity.getUuidEntity().getUuid(),
//                            reportEntity.getReportCategory(),
//                            reportEntity.getTitle(),
//                            reportEntity.getUserEntity().getEmail(),
//                            reportEntity.getReportDate(),
//                            reportEntity.getReportStatus()
//                    );
//                })
//                .collect(Collectors.toList());
        // 람다 함수, stream 공부해보기
        // DB에서 가져올때 Entity로 반환이 되고
        // 구현한 기능은 Dto에 담아 전달을 해야하니
        // 형식을 바꾸기 위해 Mapper를 활용
        // 받은 entity형식의 db를 dto로 변환 후 이를 리스트 형태로 반환
    }

    // admin 을 위한 전체 Report 리스트
    @Override
    public List<ReportDto.ReportInfoDto> getAllReport(PageRequest pageRequest) {
        return reportRepository.findAll(pageRequest)
                .stream()
                .map(ReportMapper::entityToReportInfoDto)
                .collect(Collectors.toList());
    }

    // Report 답변
    @Override
    public void replyReport(ReportDto.ReplyReportDto replyReport, List<MultipartFile> multipartFileList) throws IOException {
        // 예외: 존재하지 않는 문의
        ReportEntity reportEntity = reportRepository.findByUuidEntityUuid(replyReport.getUuid()).orElseThrow(() -> new RuntimeException("존재하지 않는 문의"));
        if (multipartFileList != null) {
            List<String> replyFileNameList = extractFileNameList(multipartFileList, replyFileDir);
            replyReport.setReplyFiles(replyFileNameList);
//                for (MultipartFile multipartFile : multipartFileList) {
//
//                    String fileName = multipartFile.getOriginalFilename();
//                    String fileExt = Objects.requireNonNull(fileName).substring(fileName.lastIndexOf("."));
//                    String fileRename = UUID.randomUUID() + fileExt;
//                    replyFileNameList.add(fileRename);
//
//                    File file = new File(replyFileDir + "\\" + fileRename);
//                    try {
//                        multipartFile.transferTo(file);
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
        }
        reportEntity.replyReport(replyReport);

    }

    // Status 검색 (filter)
    // Category 검색 (filter)
    // email 검색 (String)
    @Override
    public Page<ReportDto.ReportInfoDto> searchReport(ReportDto.SearchReportDto searchReport) {

        List<ReportDto.ReportInfoDto> searchResults = reportRepository.findBySearchReportRequest(searchReport)
                .stream()
                .map(ReportMapper::entityToReportInfoDto)
                .toList();

        return new PageImpl<>(searchResults);
    }

    // 신고 및 문의 첨부파일
    @Override
    public Resource loadReportAttachmentImg(CommonDto.FileNameDto fileNameDto) {
        return new FileSystemResource(uploadConfig.getReportAttachmentImgDir() + fileNameDto.getFilename());
    }

    // 답변 첨부파일
    @Override
    public Resource loadReplyAttachmentImg(CommonDto.FileNameDto fileNameDto) {
        return new FileSystemResource(uploadConfig.getReplyAttachmentImgDir() + fileNameDto.getFilename());
    }

    @Override
    public void updateForeignKeysNull(Long userId) {
        Pageable pageable = Pageable.unpaged();
        List<ReportEntity> reportEntities = reportRepository.findByUserEntityUserIdOrderByReportIdDesc(userId, pageable);

        // 외래 키를 null로 설정
        for (ReportEntity reportEntity : reportEntities) {
            reportEntity.updateUserEntity(null);
        }
    }
}
