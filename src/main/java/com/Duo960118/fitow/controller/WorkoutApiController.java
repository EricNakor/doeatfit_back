package com.Duo960118.fitow.controller;

import com.Duo960118.fitow.annotaion.Enum;
import com.Duo960118.fitow.annotaion.File;
import com.Duo960118.fitow.entity.WorkoutDto;
import com.Duo960118.fitow.entity.WorkoutEntity;
import com.Duo960118.fitow.response.ApiResponse;
import com.Duo960118.fitow.service.WorkoutService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api")
public class WorkoutApiController {
    private final WorkoutService workoutService;

    // 운동 추가
    @PostMapping("def-cms/workouts")
    public ApiResponse<WorkoutDto.WorkoutDetailDto> postWorkout(@RequestPart(value = "mediaFile", required = false) @NotNull(message = "{NotNull.multipartFile}") @File(allowedFileExt = {"mp4"}, fileSizeLimit = 1024 * 1024 * 50)MultipartFile multipartFile,
                                                                         @Valid @RequestPart(value = "postWorkoutRequest") WorkoutDto.PostWorkoutRequestDto postWorkoutRequest) {
        postWorkoutRequest.setMediaFile(multipartFile);
        return ApiResponse.success(workoutService.postWorkout(postWorkoutRequest));
    }

    // 운동 수정
    @PutMapping("def-cms/workouts/{uuid}")
    public ApiResponse<WorkoutDto.WorkoutDetailDto> editWorkout(
            @PathVariable("uuid") UUID uuid,
            @RequestPart(value = "mediaFile", required = false) @File(allowedFileExt = {"mp4"}, fileSizeLimit = 1024 * 1024 * 50) MultipartFile multipartFile,
            @Valid @RequestPart(value = "editWorkoutRequest") WorkoutDto.EditWorkoutRequestDto editWorkoutRequest) {

        editWorkoutRequest.setWorkoutFile(multipartFile);
        editWorkoutRequest.setUuid(uuid);
        return ApiResponse.success(workoutService.editWorkout(editWorkoutRequest));
    }

    // 운동 삭제
    @DeleteMapping("def-cms/workouts/{uuid}")
    public ApiResponse<Object> deleteWorkout(@PathVariable("uuid") UUID uuid) {
        workoutService.deleteWorkout(uuid);
        return ApiResponse.success(null);
    }

    // 운동 전체 조회
    @GetMapping("workouts")
    public ApiResponse<List<WorkoutDto.WorkoutDetailDto>> workouts(
            @PageableDefault(page = 0, size = 10, sort = "workoutId", direction = Sort.Direction.ASC) Pageable pageable) {

        List<WorkoutDto.WorkoutDetailDto> result = workoutService.getAllWorkout(pageable);

        return ApiResponse.success(result);
    }

    // 운동 영상 조회
    @GetMapping("workouts/media/{filename}")
    public ApiResponse<Resource> loadWorkoutMedia(@PathVariable("filename") String filename) {
        return ApiResponse.successResource(workoutService.loadWorkoutMedia(filename));
    }

    // 운동 검색
    @GetMapping("workouts/search")
    public ApiResponse<Slice<WorkoutDto.WorkoutDetailDto>> searchWorkout(@PageableDefault(size = 4, sort = "workoutId", direction = Sort.Direction.DESC) Pageable pageable,
                                                                         @RequestParam(value = "workoutDifficulties", required = false, defaultValue = "") List<@Enum(enumClass = WorkoutEntity.DifficultyEnum.class,message = "{Enum.workoutDifficulty}") String> workoutDifficulties,
                                                                         @Size(min=0,max=30,message = "{Size.workoutName}") @RequestParam(value = "workoutName", required = false, defaultValue = "") String workoutName,
                                                                         @RequestParam(value = "majorMuscles", required = false, defaultValue = "") List<@Enum(enumClass = WorkoutEntity.MajorMuscleEnum.class, message = "{Enum.bodyPartEnum}")String> majorMuscles,
                                                                         @RequestParam(value = "agonistMuscles", required = false, defaultValue = "") List<@Enum(enumClass = WorkoutEntity.MinorMuscleEnum.class, message = "{Enum.agonistMuscles}")String> agonistMuscles,
                                                                         @RequestParam(value = "antagonistMuscles", required = false, defaultValue = "") List<@Enum(enumClass = WorkoutEntity.MinorMuscleEnum.class, message = "{Enum.antagonistMuscles}")String> antagonistMuscles,
                                                                         @RequestParam(value = "synergistMuscles", required = false, defaultValue = "") List<@Enum(enumClass = WorkoutEntity.MinorMuscleEnum.class, message = "{Enum.synergistMuscles}")String> synergistMuscles) {

        WorkoutDto.SearchWorkoutRequestDto searchWorkoutRequest = WorkoutDto.SearchWorkoutRequestDto.builder()
                .workoutDifficulties(workoutDifficulties)
                .workoutName(workoutName)
                .majorMuscles(majorMuscles)
                .agonistMuscles(agonistMuscles)
                .antagonistMuscles(antagonistMuscles)
                .synergistMuscles(synergistMuscles)
                .build();

        return ApiResponse.success(workoutService.searchWorkout(searchWorkoutRequest, pageable));
    }

    // uuid에 해당하는 운동 하나 조회
    @GetMapping("/workouts/{uuid}")
    public ApiResponse<WorkoutDto.WorkoutDetailDto> workoutDetail(@PathVariable("uuid") UUID uuid) {
        WorkoutDto.WorkoutDetailDto workout = workoutService.getWorkoutDetail(uuid);
        return ApiResponse.success(workout);
    }

//    // 근육 enum 조회
//    @GetMapping("workouts/muscle-enums")
//    public ResponseEntity<WorkoutEntity.MuscleEnum[]> muscleEnums() {
//        return ResponseEntity.ok().body(WorkoutEntity.MuscleEnum.values());
//    }
//
//    // 난이도 enum 조회
//    @GetMapping("workouts/difficulty-enums")
//    public ResponseEntity<WorkoutEntity.DifficultyEnum[]> difficultyEnums() {
//        return ResponseEntity.ok().body(WorkoutEntity.DifficultyEnum.values());
//    }
//    // 신체 부위 enum 조회
//    @GetMapping("workouts/bodypart-enums")
//    public ResponseEntity<WorkoutEntity.BodyPartEnum[]> bodyPartEnums(){
//        return ResponseEntity.ok().body(WorkoutEntity.BodyPartEnum.values());
//    }

}
