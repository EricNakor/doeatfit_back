package com.Duo960118.fitow.controller;

import com.Duo960118.fitow.entity.StatusResponseDto;
import com.Duo960118.fitow.entity.WorkoutDto;
import com.Duo960118.fitow.entity.WorkoutEntity;
import com.Duo960118.fitow.service.WorkoutService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api")
public class WorkoutApiController {

    private final WorkoutService workoutService;

    // 운동 추가
    @PostMapping("def-cms/workouts")
    public ResponseEntity<WorkoutDto.PostWorkoutResponseDto> postWorkout(@RequestPart(value = "mediaFile", required = false) MultipartFile multipartFile,
                                                                         @RequestPart(value = "postWorkoutRequest") WorkoutDto.PostWorkoutRequestDto postWorkoutRequest) {
        return ResponseEntity
                .ok()
                .body(new WorkoutDto.PostWorkoutResponseDto(workoutService.postWorkout(multipartFile, postWorkoutRequest)));
    }

    // 운동 수정
    // 파일 업로드가 필요한 요청은
    @PutMapping("def-cms/workouts/{uuid}")
    public ResponseEntity<WorkoutDto.PostWorkoutResponseDto> editWorkout(@PathVariable("uuid") UUID uuid, @RequestPart(value = "mediaFile", required = false) MultipartFile multipartFile, @RequestPart(value = "editWorkoutRequest") WorkoutDto.PostWorkoutRequestDto editWorkoutRequest) {
        return ResponseEntity
                .ok()
                .body(new WorkoutDto.PostWorkoutResponseDto(workoutService.editWorkout(uuid, multipartFile, editWorkoutRequest)));
    }

    // 운동 삭제
    @DeleteMapping("def-cms/workouts/{uuid}")
    public ResponseEntity<StatusResponseDto> deleteWorkout(@PathVariable("uuid") UUID uuid) {
        return ResponseEntity.ok()
                .body(new StatusResponseDto(workoutService.deleteWorkout(uuid)));
    }

    // 운동 전체 조회
    @GetMapping("workouts")
    public ResponseEntity<List<WorkoutDto.WorkoutDetailDto>> workouts(@RequestParam("pageNumber") int pageNumber, @RequestParam("pageSize") int pageSize) {
        Sort sort = Sort.by("workoutId").descending();
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize).withSort(sort);

        List<WorkoutDto.WorkoutDetailDto> result = workoutService.getAllWorkout(pageRequest);

        return ResponseEntity.ok()
                .body(result);
    }

    // 운동 영상 조회
    @GetMapping("workouts/media/{filename}")
    public ResponseEntity<Resource> loadWorkoutMedia(@PathVariable("filename") String filename) {
        return ResponseEntity.ok().body(workoutService.loadWorkoutMedia(filename));
    }

    // 운동 검색
    @GetMapping("workouts/search")
    public ResponseEntity<Page<WorkoutDto.WorkoutDetailDto>> searchWorkout(@PageableDefault(size = 10, sort = "workoutId", direction = Sort.Direction.DESC) Pageable pageable,
                                                                           @RequestParam(value = "workoutDifficulties", required = false, defaultValue = "") List<WorkoutEntity.DifficultyEnum> workoutDifficulties,
                                                                           @RequestParam(value = "workoutName", required = false, defaultValue = "") String workoutName,
                                                                           @RequestParam(value = "bodyParts", required = false, defaultValue = "") List<WorkoutEntity.BodyPartEnum> bodyPartEnums,
                                                                           @RequestParam(value = "agonistMuscles", required = false, defaultValue = "") List<WorkoutEntity.MuscleEnum> agonistMuscleEnums,
                                                                           @RequestParam(value = "antagonistMuscles", required = false, defaultValue = "") List<WorkoutEntity.MuscleEnum> antagonistMuscleEnums,
                                                                           @RequestParam(value = "synergistMuscles", required = false, defaultValue = "") List<WorkoutEntity.MuscleEnum> synergistMuscleEnums) {
        WorkoutDto.SearchWorkoutRequestDto searchWorkoutRequest = WorkoutDto.SearchWorkoutRequestDto.builder()
                .workoutDifficulties(workoutDifficulties)
                .workoutName(workoutName)
                .bodyPartEnums(bodyPartEnums)
                .agonistMuscleEnums(agonistMuscleEnums)
                .antagonistMuscleEnums(antagonistMuscleEnums)
                .synergistMuscleEnums(synergistMuscleEnums)
                .build();

        return ResponseEntity.ok()
                .body(workoutService.searchWorkout(searchWorkoutRequest, pageable));
    }

    // 근육 enum 조회
    @GetMapping("workouts/muscle-enums")
    public ResponseEntity<WorkoutEntity.MuscleEnum[]> muscleEnums() {
        return ResponseEntity.ok().body(WorkoutEntity.MuscleEnum.values());
    }

    // 난이도 enum 조회
    @GetMapping("workouts/difficulty-enums")
    public ResponseEntity<WorkoutEntity.DifficultyEnum[]> difficultyEnums() {
        return ResponseEntity.ok().body(WorkoutEntity.DifficultyEnum.values());
    }

    // uuid에 해당하는 운동 하나 조회
    @GetMapping("/workouts/{uuid}")
    public ResponseEntity<WorkoutDto.WorkoutDetailDto> workoutDetail(@PathVariable("uuid") UUID uuid) {
        WorkoutDto.WorkoutDetailDto workout = workoutService.getWorkoutDetail(uuid);

        return ResponseEntity.ok()
                .body(workout);
    }

    // 신체 부위 enum 조회
    @GetMapping("workouts/bodypart-enums")
    public ResponseEntity<WorkoutEntity.BodyPartEnum[]> bodyPartEnums(){
        return ResponseEntity.ok().body(WorkoutEntity.BodyPartEnum.values());
    }

}
