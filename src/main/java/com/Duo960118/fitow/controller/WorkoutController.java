package com.Duo960118.fitow.controller;

import com.Duo960118.fitow.entity.WorkoutDto;
import com.Duo960118.fitow.entity.WorkoutEntity;
import com.Duo960118.fitow.service.WorkoutService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
public class WorkoutController {
    private final WorkoutService workoutService;
    
    // 운동 전체 조회
    @GetMapping("/workouts")
    public String workouts(Model model) {
        // 기본 정렬은 noticeId 기준 내림차순으로
        Sort sort = Sort.by("workoutId").descending();
        PageRequest pageRequest = PageRequest.of(0, 10).withSort(sort);
        List<WorkoutDto.WorkoutDetailDto> workouts = workoutService.getAllWorkout(pageRequest);
        model.addAttribute("muscleEnums", WorkoutEntity.MuscleEnum.values());
        model.addAttribute("workouts",workouts);

        return "/workout/workouts";
    }

    // uuid에 해당하는 운동 하나 조회
    @GetMapping("/workouts/{uuid}")
    public String workouts(@PathVariable("uuid") UUID uuid, Model model) {
        // 기본 정렬은 noticeId 기준 내림차순으로
        Sort sort = Sort.by("workoutId").descending();
        PageRequest pageRequest = PageRequest.of(0, 10).withSort(sort);
        WorkoutDto.WorkoutDetailDto workout = workoutService.getWorkoutDetail(uuid);

        model.addAttribute("workout",workout);

        return "/workout/workoutDetail";
    }

    // 운동 작성
    @GetMapping("/workouts/post")
    public String postWorkout(@ModelAttribute("workout") WorkoutDto.PostWorkoutRequestDto workout, Model model){
        model.addAttribute("muscleEnums", WorkoutEntity.MuscleEnum.values());
        model.addAttribute("difficultyEnums", WorkoutEntity.DifficultyEnum.values());
        return "/workout/postWorkout";
    }
    
    // 운동 수정
    @GetMapping("/workouts/edit/{uuid}")
    public String editWorkout(@PathVariable("uuid") UUID uuid,Model model){
        WorkoutDto.WorkoutDetailDto workoutDetail = workoutService.getWorkoutDetail(uuid);

        model.addAttribute("workout", workoutDetail);
        model.addAttribute("muscleEnums", WorkoutEntity.MuscleEnum.values());
        model.addAttribute("difficultyEnums", WorkoutEntity.DifficultyEnum.values());

        return "/workout/editWorkout";
    }
}
