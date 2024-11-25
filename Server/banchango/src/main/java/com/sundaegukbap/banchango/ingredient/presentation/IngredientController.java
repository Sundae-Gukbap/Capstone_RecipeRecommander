package com.sundaegukbap.banchango.ingredient.presentation;

import com.sundaegukbap.banchango.ingredient.application.IngredientQueryService;
import com.sundaegukbap.banchango.ingredient.application.IngredientService;
import com.sundaegukbap.banchango.ingredient.dto.IngredientInsertRequest;
import com.sundaegukbap.banchango.ingredient.dto.dto.ContainerIngredientDto;
import com.sundaegukbap.banchango.ingredient.dto.dto.ContainerIngredientDtos;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ingredients")
@Tag(name = "재료 관련 컨트롤러")
public class IngredientController {

    private final IngredientQueryService ingredientQueryService;
    private final IngredientService ingredientService;

    public IngredientController(IngredientQueryService ingredientQueryService,
        IngredientService ingredientService) {
        this.ingredientQueryService = ingredientQueryService;
        this.ingredientService = ingredientService;
    }

    @PostMapping("/{userId}")
    @Operation(summary = "소유 재료 추가", description = "소유한 재료를 추가한다.")
    public ResponseEntity<String> insertIngredient(@PathVariable("userId") Long userId,
        @RequestBody IngredientInsertRequest request) {
        long start = System.nanoTime();
        ingredientService.insertIngredient(userId, request);
        long end = System.nanoTime();
        System.out.println("insertIngredient: " + (end - start) / 1000000000.0 + "s");
        return new ResponseEntity<>("success add ingredient", HttpStatus.OK);
    }

    @DeleteMapping("/{containerIngredientId}")
    @Operation(summary = "소유 재료 제거", description = "소유한 재료를 제거한다.")
    public ResponseEntity<String> removeIngredient(
        @PathVariable("containerIngredientId") Long containerIngredientId) {
        ingredientService.removeIngredient(1L, containerIngredientId);
        return new ResponseEntity<>("success remove ingredient", HttpStatus.OK);
    }

    @GetMapping("/{containerIngredientId}")
    @Operation(summary = "재료 상세 정보 조회", description = "소유한 재료의 상세 정보를 확인한다.")
    public ResponseEntity<ContainerIngredientDto> getIngredientInfo(
        @PathVariable("containerIngredientId") Long containerIngredientId) {
        ContainerIngredientDto response = ingredientQueryService.getIngredientInfo(
            containerIngredientId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/main/list/{userId}")
    @Operation(summary = "소유한 재료 전체 조회", description = "사용자가 소유한 전체 재료 목록을 조회한다.")
    public ResponseEntity<ContainerIngredientDtos> getUserIngredients(
        @PathVariable("userId") Long userId) {
        ContainerIngredientDtos response = ingredientQueryService.getUserIngredients(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/container/list/{containerId}")
    @Operation(summary = "특정 창고 재료 목록 조회", description = "특정 창고에 속한 재료 목록을 조회한다.")
    public ResponseEntity<ContainerIngredientDtos> getContainerIngredients(
        @PathVariable("containerId") Long containerId) {
        ContainerIngredientDtos response = ingredientQueryService.getContainerIngredients(
            containerId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
