package com.Duo960118.fitow.service;

import com.Duo960118.fitow.entity.WorkoutDto;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface WorkoutService {
    // 운동 전체 조회
    @Transactional
    List<WorkoutDto.WorkoutDetailDto> getAllWorkout(Pageable pageable);

    // 운동 한개 세부사항 조회
    @Transactional
    WorkoutDto.WorkoutDetailDto getWorkoutDetail(UUID uuid);

    // 운동 수정
    @Transactional
    WorkoutDto.WorkoutDetailDto editWorkout(WorkoutDto.EditWorkoutRequestDto postWorkoutRequest);
    
    // 운동 추가
    @Transactional
    WorkoutDto.WorkoutDetailDto postWorkout(WorkoutDto.PostWorkoutRequestDto postWorkoutRequest);
    
    // 운동 삭제
    @Transactional
    void deleteWorkout(UUID uuid);

    // 운동 영상 불러오기
    Resource loadWorkoutMedia(String filename);

    // 운동 검색
    @Transactional
    Slice<WorkoutDto.WorkoutDetailDto> searchWorkout(WorkoutDto.SearchWorkoutRequestDto searchWorkoutRequest, Pageable pageable);

    // 운동 키워드 검색
    @Transactional
    Page<WorkoutDto.WorkoutDetailDto> searchWorkout(WorkoutDto.SearchWorkoutKeywordRequestDto searchWorkoutKeywordRequest);

}
