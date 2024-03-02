package com.Duo960118.fitow.service;

import com.Duo960118.fitow.entity.ReportDto;
import com.Duo960118.fitow.entity.ReportEntity;
import com.Duo960118.fitow.mapper.ReportMapper;
import com.Duo960118.fitow.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    private final UserService userService;
    private final ReportRepository reportRepository;

    @Value("${spring.report_file_dir}")
    private String reportFileDir;

    @Value("${spring.reply_file_dir}")
    private String replyFileDir;

    // report 작성
    @Override
    public UUID postReport(List<MultipartFile> multipartFileList, ReportDto.PostReportRequestDto postReportRequest) {

        List<String> reportFileNameList = new ArrayList<>();

        if (multipartFileList != null) {
            for (MultipartFile multipartFile : multipartFileList) {

                String fileName = multipartFile.getOriginalFilename();
                String fileExt = Objects.requireNonNull(fileName).substring(fileName.lastIndexOf("."));
                String fileRename = UUID.randomUUID() + fileExt;
                reportFileNameList.add(fileRename);

                File file = new File(reportFileDir + "\\" + fileRename);
                try {
                    multipartFile.transferTo(file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            postReportRequest.setReportFiles(reportFileNameList);
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
    public boolean deleteReport(UUID uuid) {
        reportRepository.deleteByUuidEntityUuid(uuid);
        return !reportRepository.existsByUuidEntityUuid(uuid);
    }

    // Report 상세내용
    @Override
    public ReportDto.ReportDetailDto getReportDetail(UUID uuid) {
        ReportEntity reportEntity = reportRepository.findByUuidEntityUuid(uuid).orElseThrow(() -> new RuntimeException("존재하지 않는 문의"));

        String email = reportEntity.getUserEntity().getEmail();

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
    public boolean replyReport(UUID uuid, ReportDto.ReplyReportDto replyReport, List<MultipartFile> multipartFileList) {
        List<String> replyFileNameList = new ArrayList<>();
        try {
            ReportEntity reportEntity = reportRepository.findByUuidEntityUuid(uuid).orElseThrow(() -> new RuntimeException("존재하지 않는 문의"));

            if (multipartFileList != null) {
                for (MultipartFile multipartFile : multipartFileList) {

                    String fileName = multipartFile.getOriginalFilename();
                    String fileExt = Objects.requireNonNull(fileName).substring(fileName.lastIndexOf("."));
                    String fileRename = UUID.randomUUID() + fileExt;
                    replyFileNameList.add(fileRename);

                    File file = new File(replyFileDir + "\\" + fileRename);
                    try {
                        multipartFile.transferTo(file);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                replyReport.setReplyFiles(replyFileNameList);
            }
            reportEntity.replyReport(replyReport);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // Status 검색 (filter)
    // Category 검색 (filter)
    // email 검색 (String)
    @Override
    public Page<ReportDto.ReportInfoDto> searchReport(ReportEntity.ReportStatusEnum reportStatusEnum, ReportEntity.ReportCategoryEnum reportCategoryEnum, String email, Pageable pageable) {

        List<ReportDto.ReportInfoDto> searchResults = reportRepository.findBySearchReportRequest(reportStatusEnum, reportCategoryEnum, email, pageable)
                .stream()
                .map(ReportMapper::entityToReportInfoDto)
                .toList();

        return new PageImpl<>(searchResults);
    }

}
