package com.Duo960118.fitow.repository;

import static com.Duo960118.fitow.entity.QWorkoutEntity.workoutEntity;

import com.Duo960118.fitow.entity.WorkoutDto;
import com.Duo960118.fitow.entity.WorkoutEntity;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@RequiredArgsConstructor
public class WorkoutRepositoryCustomImpl implements WorkoutRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    //
    @Override
    public Page<WorkoutEntity> findBySearchWorkoutRequest(WorkoutDto.SearchWorkoutRequestDto searchWorkoutRequest, Pageable pageable) {

        List<WorkoutEntity> fetch = queryFactory
                .select(workoutEntity)
                .from(workoutEntity)
                .where(containsWorkoutName(searchWorkoutRequest.getWorkoutName()))
                .where(inWorkoutDifficulty(searchWorkoutRequest.getWorkoutDifficulties()))
                .where(containsAgonistMuscleEnums(searchWorkoutRequest.getAgonistMuscleEnums()))
                .where(containsAntagonistMuscleEnums(searchWorkoutRequest.getAntagonistMuscleEnums()))
                .where(containsSynergistMuscleEnums(searchWorkoutRequest.getSynergistMuscleEnums()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        //    "pageSize": 5, // 한 페이지에서 나타내는 원소의 수 (게시글 수)
        //        "pageNumber": 0, // 페이지 번호 (0번 부터 시작)
        //        "offset": 0, // 해당 페이지에 첫 번째 원소의 수

        JPAQuery<Long> countQuery = getCount(searchWorkoutRequest);

        //PageableExecutionUtils를 사용하면 Count 쿼리 최적화할 수 있다.
        //내부적으로 Count 쿼리가 필요없으면 조회해오지 않는다
        //조건 : 페이지 시작이면서 컨텐츠 사이즈가 페이지 사이즈보다 작을 때 마지막 페이지 일 때(offset + 컨텐츠 사이즈를 더해서 전체 사이즈를 구함)

        return PageableExecutionUtils.getPage(fetch, pageable, countQuery::fetchOne);
    }

    // 개수 세는 용도? 왜쓰는지 모름. 페이징할 때 필요하다는 것만 암
    private JPAQuery<Long> getCount(WorkoutDto.SearchWorkoutRequestDto searchWorkoutRequest) {

        return queryFactory
                .select(workoutEntity.count())
                .from(workoutEntity)
                .where(containsWorkoutName(searchWorkoutRequest.getWorkoutName()))
                .where(inWorkoutDifficulty(searchWorkoutRequest.getWorkoutDifficulties()))
                .where(containsAgonistMuscleEnums(searchWorkoutRequest.getAgonistMuscleEnums()))
                .where(containsAntagonistMuscleEnums(searchWorkoutRequest.getAntagonistMuscleEnums()))
                .where(containsSynergistMuscleEnums(searchWorkoutRequest.getSynergistMuscleEnums()));
//                .where(containsBodyPartEnums(searchWorkoutRequest.getBodyPartEnums(), searchWorkoutRequest.getAgonistMuscleEnums()));
    }

    // BooleanExpression은 null 반환 시 자동으로 조건절에서 제거 된다.
    // (단, 모든 조건이 null이 발생 시 전체 엔티티를 불러오게 되므로 대장애가 발생할 수 있음)

    // 운동 검색
    private BooleanExpression containsWorkoutName(String workoutName) {
        if (!StringUtils.hasLength(workoutName)) {
            return null;
        }
        return workoutEntity.workoutName.contains(workoutName);
    }

    // 운동 난이도 검색 
    private BooleanExpression inWorkoutDifficulty(List<WorkoutEntity.DifficultyEnum> workoutDifficultyEnums) {
        if (ObjectUtils.isEmpty(workoutDifficultyEnums)) {
            return null;
        }
        return workoutEntity.workoutDifficulty.in(workoutDifficultyEnums);
    }

    // 주동근 검색
    // 검색 조건으로 사용된 주동근들 중 하나라도 포함하면, 그 운동을 반환 하도록 하는 조건(BooleanExpression)
    private BooleanExpression containsAgonistMuscleEnums(List<WorkoutEntity.MuscleEnum> agonistMuscleEnums) {
        if (ObjectUtils.isEmpty(agonistMuscleEnums)) {
            return null;
        }

        BooleanExpression expression = null;

        for (WorkoutEntity.MuscleEnum muscle : agonistMuscleEnums) {
            if (expression == null) {
                expression = workoutEntity.agonistMuscleEnums.contains(muscle);
            } else {
                expression = expression.or(workoutEntity.agonistMuscleEnums.contains(muscle));
            }
        }
        return expression;
    }

    // 길항근 검색
    private BooleanExpression containsAntagonistMuscleEnums(List<WorkoutEntity.MuscleEnum> antagonistMuscleEnums) {
        if (ObjectUtils.isEmpty(antagonistMuscleEnums)) {
            return null;
        }

        BooleanExpression expression = null;

        for (WorkoutEntity.MuscleEnum muscle : antagonistMuscleEnums) {
            if (expression == null) {
                expression = workoutEntity.antagonistMuscleEnums.contains(muscle);
            } else {
                expression = expression.or(workoutEntity.antagonistMuscleEnums.contains(muscle));
            }
        }
        return expression;
    }

    // 협응근 검색
    private BooleanExpression containsSynergistMuscleEnums(List<WorkoutEntity.MuscleEnum> synergistMuscleEnums) {
        if (ObjectUtils.isEmpty(synergistMuscleEnums)) {
            return null;
        }

        BooleanExpression expression = null;

        for (WorkoutEntity.MuscleEnum muscle : synergistMuscleEnums) {
            if (expression == null) {
                expression = workoutEntity.synergistMuscleEnums.contains(muscle);
            } else {
                expression = expression.or(workoutEntity.synergistMuscleEnums.contains(muscle));
            }
        }
        return expression;
    }

    // 신체 부위로 검색
//    private BooleanExpression containsBodyPartEnums(List<WorkoutEntity.BodyPartEnum> bodyPartEnums, List<WorkoutEntity.MuscleEnum> agonistMuscleEnums) {
//        if (ObjectUtils.isEmpty(bodyPartEnums)) {
//            return null;
//        }
//        BooleanExpression expression = null;
//
//        for (WorkoutEntity.MuscleEnum muscle : agonistMuscleEnums) {
//            for (WorkoutEntity.BodyPartEnum bodyPart : bodyPartEnums) {
//                if (muscle.getBodyPart() == bodyPart) {
//                    if (expression == null) {
//                        expression = workoutEntity.agonistMuscleEnums.contains(muscle);
//                    } else {
//                        expression = expression.or(workoutEntity.agonistMuscleEnums.contains(muscle));
//                    }
//                }
//            }
//        }
//        return expression;
//    }
}

