package com.Duo960118.fitow.service;

import com.Duo960118.fitow.entity.WorkoutEntity;
import com.Duo960118.fitow.entity.WorkoutDto;
import com.Duo960118.fitow.mapper.WorkoutMapper;
import com.Duo960118.fitow.repository.WorkoutRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkoutServiceImpl implements WorkoutService {
    @Value("${spring.workout_media_dir}")
    private String workoutMediaDir;

    private final WorkoutRepository workoutRepository;

    @Override
    public List<WorkoutDto.WorkoutDetailDto> getAllWorkout(Pageable pageable) {
        return workoutRepository.findAll(pageable).stream().map(WorkoutMapper::entityToWorkoutDetailDto).collect(Collectors.toList());
    }

    @Override
    public WorkoutDto.WorkoutDetailDto getWorkoutDetail(UUID uuid) {
        return workoutRepository.findByUuidEntityUuid(uuid).map(WorkoutMapper::entityToWorkoutDetailDto).orElseThrow(() -> new RuntimeException("해당 운동이 존재하지 않습니다."));
    }

    @Override
    public WorkoutDto.WorkoutDetailDto  editWorkout(WorkoutDto.EditWorkoutRequestDto editWorkoutRequest) {
        WorkoutEntity workoutEntity = workoutRepository.findByUuidEntityUuid(editWorkoutRequest.getUuid()).orElseThrow(() -> new RuntimeException("해당 운동이 존재하지 않습니다."));

        // 파일명 추출
        if (editWorkoutRequest.getWorkoutFile() != null) {
            String fileName = editWorkoutRequest.getWorkoutFile().getOriginalFilename();
            // 파일 확장자 추출
            String fileExt = Objects.requireNonNull(fileName).substring(fileName.lastIndexOf("."));
            String workoutMediaFileName = UUID.randomUUID() + fileExt;

            // 지정된 경로에 저장
            File file = new File(workoutMediaDir + "\\" + workoutMediaFileName);
            try {
                editWorkoutRequest.getWorkoutFile().transferTo(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            editWorkoutRequest.setMediaFileName(workoutMediaFileName);
        } else {
            // 업로드된 파일 없으면 원래 파일명 유지
            editWorkoutRequest.setMediaFileName(workoutEntity.getMediaFileName());
        }
        workoutEntity.updateWorkoutEntity(editWorkoutRequest);

        return WorkoutMapper.entityToWorkoutDetailDto(workoutEntity);
    }

    @Override
    public WorkoutDto.WorkoutDetailDto postWorkout(WorkoutDto.PostWorkoutRequestDto postWorkoutRequest) {

        String workoutMediaFileName="";
        // 파일명 추출
        if (postWorkoutRequest.getMediaFile() != null) {
            String fileName = postWorkoutRequest.getMediaFile() .getOriginalFilename();
            // 확장자 추출
            String fileExt = Objects.requireNonNull(fileName).substring(fileName.lastIndexOf("."));
            workoutMediaFileName = UUID.randomUUID()+ fileExt;

            // 지정된 경로에 저장
            File file = new File(workoutMediaDir + File.separator + workoutMediaFileName);
            try {
                postWorkoutRequest.getMediaFile() .transferTo(file);
            } catch (IOException e) {
                throw new RuntimeException("Failed to save media file", e);
            }
        }

        WorkoutEntity workoutEntity = WorkoutEntity.builder()
                .workoutName(postWorkoutRequest.getWorkoutName())
                .workoutDifficulty(WorkoutEntity.DifficultyEnum.valueOf(postWorkoutRequest.getWorkoutDifficulty()))
                .agonistMuscle(postWorkoutRequest.getAgonistMuscles().stream().map(WorkoutEntity.MuscleEnum::fromString).collect(Collectors.toSet()))
                .antagonistMuscle(postWorkoutRequest.getAntagonistMuscles().stream().map(WorkoutEntity.MuscleEnum::fromString).collect(Collectors.toSet()))
                .synergistMuscle(postWorkoutRequest.getSynergistMuscles().stream().map(WorkoutEntity.MuscleEnum::fromString).collect(Collectors.toSet()))
                .descriptions(postWorkoutRequest.getDescriptions())
                .mediaFileName(workoutMediaFileName)
                .build();
        workoutRepository.save(workoutEntity);

        return WorkoutMapper.entityToWorkoutDetailDto(workoutEntity);
    }

    @Override
    public void deleteWorkout(UUID uuid) {
        WorkoutEntity workoutEntity = workoutRepository.findByUuidEntityUuid(uuid).orElseThrow(() -> new RuntimeException("운동이 존재하지 않습니다."));

        // 영상 이름
        String workoutMediaName = workoutEntity.getMediaFileName();
        workoutRepository.delete(workoutEntity);

        // 영상도 삭제
        File file = new File(workoutMediaDir + workoutMediaName);
        if (file.delete()){
            log.info("Profile video deleted");
        } else {
            log.warn("Profile video not deleted");
        }

//        return !workoutRepository.existsByUuidEntityUuid(uuid);
    }

    @Override
    public Resource loadWorkoutMedia(String filename) {
        return new FileSystemResource(workoutMediaDir + filename);
    }

    @Override
    public Page<WorkoutDto.WorkoutDetailDto> searchWorkout(WorkoutDto.SearchWorkoutRequestDto searchWorkoutRequest, Pageable pageable) {
        List<WorkoutDto.WorkoutDetailDto> workoutDetails = workoutRepository.findBySearchWorkoutRequest(searchWorkoutRequest, pageable).stream().map(WorkoutMapper::entityToWorkoutDetailDto).collect(Collectors.toList());
        return new PageImpl<>(workoutDetails);
    }
}
