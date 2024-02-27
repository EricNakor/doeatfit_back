package com.Duo960118.fitow.service;

import com.Duo960118.fitow.entity.WorkoutEntity;
import com.Duo960118.fitow.entity.WorkoutDto;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface WorkoutService {
    // 운동 전체 조회
    @Transactional
    public List<WorkoutDto.WorkoutDetailDto> getAllWorkout( PageRequest pageRequest);
    
    // 해당하는 근육 운동 조회
    @Transactional
    public List<WorkoutDto.WorkoutDetailDto> getWorkoutsByAgonistMuscleEnum(WorkoutEntity.MuscleEnum muscleEnum);

    // 운동 한개 세부사항 조회
    @Transactional
    public WorkoutDto.WorkoutDetailDto getWorkoutDetail(UUID uuid);

    // 운동 수정
    @Transactional
    public UUID editWorkout(UUID uuid, MultipartFile multipartFile, WorkoutDto.PostWorkoutRequestDto postWorkoutRequest);
    
    // 운동 추가
    @Transactional
    public UUID postWorkout(MultipartFile multipartFile, WorkoutDto.PostWorkoutRequestDto postWorkoutRequest);
    
    // 운동 삭제
    @Transactional
    public boolean deleteWorkout(UUID uuid);

    // 운동 영상 불러오기
    public Resource loadWorkoutMedia(String filename);

    // 운동 검색
    @Transactional
    public Page<WorkoutDto.WorkoutDetailDto> searchWorkout(WorkoutDto.SearchWorkoutRequestDto searchWorkoutRequest, Pageable pageable);
}
