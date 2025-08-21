package org.example.kuit_kac.domain.routine.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.example.kuit_kac.domain.routine.dto.*;
import org.example.kuit_kac.domain.routine.model.*;
import org.example.kuit_kac.domain.routine.service.*;
import org.example.kuit_kac.domain.user.model.User;
import org.example.kuit_kac.domain.user.service.UserService;
import org.example.kuit_kac.global.util.TimeRange;

import java.util.Objects;

@RestController
@RequestMapping("/routines")
@Tag(name = "루틴 관리", description = "루틴을 생성하고 관리하는 API입니다.")
@RequiredArgsConstructor
public class RoutineController {

    private final RoutineService routineService;
    private final UserService userService;

    @GetMapping("/records/profiles")
    @PreAuthorize("@owner.same(#userId, authentication) or hasRole('ADMIN')")
    @Operation(summary = "사용자 ID로 루틴 기록 조회", description = "제공된 사용자 ID를 사용하여 해당 사용자의 오늘의 루틴 기록을 조회합니다.")
    public ResponseEntity<List<RoutineRecordProfileResponse>> getRoutineRecords(
            @Parameter(description = "조회할 사용자의 고유 ID", example = "1")
            @RequestParam("userId") Long userId) {

        TimeRange timeRange = TimeRange.getTodayTimeRange();
        List<Routine> routines = routineService.getRoutinesByUserIdBetween(userId, RoutineType.RECORD, timeRange.start(), timeRange.end());

        List<RoutineRecordProfileResponse> responses = routines.stream()
                .map(RoutineRecordProfileResponse::from)
                .filter(Objects::nonNull)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/template/profiles")
    @PreAuthorize("@owner.same(#userId, authentication) or hasRole('ADMIN')")
    @Operation(summary = "사용자 ID로 나만의 루틴 조회", description = "제공된 사용자 ID를 사용하여 해당 사용자의 나만의 루틴을 조회합니다.")
    public ResponseEntity<List<RoutineRecordProfileResponse>> getTemplateRoutines(
            @Parameter(description = "조회할 사용자의 고유 ID", example = "1")
            @RequestParam("userId") Long userId) {

        List<Routine> routines = routineService.getRoutinesByUserId(userId, RoutineType.TEMPLATE);

        List<RoutineRecordProfileResponse> responses = routines.stream()
                .map(RoutineRecordProfileResponse::from)
                .filter(Objects::nonNull)
                .toList();

        return ResponseEntity.ok(responses);
    }

//     @PutMapping("/template/{dietId}")
//     @Operation(summary = "나만의 식단 수정", description = "식단 ID와 식단 이름, 음식을 입력하여 나만의 식단을 수정합니다.")
//     public ResponseEntity<DietRecordProfileResponse> updateTemplateDiet(
//             @PathVariable("dietId") Long dietId,
//             @RequestBody @Valid DietTemplateUpdateRequest request
//     ) {
//         Diet diet = dietService.getDietById(dietId);
//         dietService.updateTemplateDiet(diet, request.name(), request.foods());
//         DietRecordProfileResponse response = DietRecordProfileResponse.from(diet);
//         return ResponseEntity.ok(response);
//     }

    @PostMapping("/records")
    @PreAuthorize("@owner.sameBody(#request, authentication) or hasRole('ADMIN')")
    @Operation(summary = "운동 기록 생성", description = "유저 ID와 루틴 이름, 루틴 항목 종류, 루틴 운동을 입력하여 운동 기록을 생성합니다.")
    public ResponseEntity<RoutineRecordProfileResponse> createRecordRoutine(
            @RequestBody @Valid RoutineGeneralCreateRequest request
    ) {
        User user = userService.getUserById(request.userId());
        Routine routine = routineService.createGeneralRoutine(user, request.name(), request.routineExercises());
        RoutineRecordProfileResponse response = RoutineRecordProfileResponse.from(routine);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/records/{routineId}")
    @Operation(summary = "운동 기록 수정", description = "루틴 ID와 루틴 이름, 루틴 항목 종류, 루틴 운동을 입력하여 운동 기록을 수정합니다.")
    public ResponseEntity<RoutineRecordProfileResponse> updateRecordRoutine(
            @PathVariable("routineId") Long routineId,
            @RequestBody @Valid RoutineGeneralUpdateRequest request
    ) {
        Routine routine = routineService.getRoutineById(routineId);
        routineService.updateGeneralRoutine(routine, request.name(), request.routineExercises());
        RoutineRecordProfileResponse response = RoutineRecordProfileResponse.from(routine);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/records/simples")
    @PreAuthorize("@owner.sameBody(#request, authentication) or hasRole('ADMIN')")
    @Operation(summary = "운동 기록 생성", description = "유저 ID와 루틴 이름, 루틴 항목 종류, 루틴 운동을 입력하여 운동 기록을 생성합니다.")
    public ResponseEntity<RoutineRecordProfileResponse> createSimpleRecordRoutine(
            @RequestBody @Valid RoutineSimpleCreateRequest request
    ) {
        User user = userService.getUserById(request.userId());
        Routine routine = routineService.createSimpleRoutine(user, request.aerobicDetail(), request.anaerobicDetail());
        RoutineRecordProfileResponse response = RoutineRecordProfileResponse.from(routine);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/templates")
    @PreAuthorize("@owner.sameBody(#request, authentication) or hasRole('ADMIN')")
    @Operation(summary = "나만의 운동 루틴 생성", description = "유저 ID와 루틴 이름, 루틴 운동을 입력하여 나만의 운동 루틴을 생성합니다.")
    public ResponseEntity<RoutineRecordProfileResponse> createTemplateRoutine(
            @RequestBody @Valid RoutineTemplateCreateRequest request
    ) {
        User user = userService.getUserById(request.userId());
        Routine routine = routineService.createTemplateRoutine(user, request.name(), request.routineExercises());
        RoutineRecordProfileResponse response = RoutineRecordProfileResponse.from(routine);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/templates/{routineId}")
    @Operation(summary = "나만의 운동 루틴 수정", description = "루틴 ID와 루틴 이름, 루틴 운동을 입력하여 나만의 운동 루틴을 수정합니다.")
    public ResponseEntity<RoutineRecordProfileResponse> updateTemplateRoutine(
            @PathVariable("routineId") Long routineId,
            @RequestBody @Valid RoutineTemplateUpdateRequest request
    ) {
        Routine routine = routineService.getRoutineById(routineId);
        routineService.updateTemplateRoutine(routine, request.name(), request.routineExercises());
        RoutineRecordProfileResponse response = RoutineRecordProfileResponse.from(routine);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{routineId}")
    @Operation(summary = "운동 기록 삭제", description = "루틴 ID를 입력하여 운동 기록을 삭제합니다.")
    public ResponseEntity<Void> deleteRecordRoutine(
            @PathVariable("routineId") Long routineId
    ) {
        Routine routine = routineService.getRoutineById(routineId);
        routineService.deleteRoutine(routine);
        return ResponseEntity.noContent().build();
    }
}