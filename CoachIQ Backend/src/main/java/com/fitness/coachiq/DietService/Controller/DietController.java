package com.fitness.coachiq.DietService.Controller;

import com.fitness.coachiq.Config.CurrentUser;
import com.fitness.coachiq.DietService.DTO.DietRequest;
import com.fitness.coachiq.DietService.DTO.DietResponse;
import com.fitness.coachiq.DietService.Entity.DietDay;
import com.fitness.coachiq.DietService.Service.DietService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diets")
@RequiredArgsConstructor
@Tag(name = "Diets", description = "AI diet generation and retrieval")
public class DietController {
    private final DietService dietService;
    private final CurrentUser currentUser;

    @Operation(summary = "Generate diet plan")
    @PostMapping
    public ResponseEntity<String> createDiet(@Valid @RequestBody DietRequest request, Authentication authentication) {
        request.setUserId(currentUser.id(authentication));
        return ResponseEntity.ok(dietService.createDiet(request));
    }

    @Operation(summary = "Get my diet plans")
    @GetMapping
    public ResponseEntity<List<DietResponse>> getUserDiets(Authentication authentication) {
        return ResponseEntity.ok(dietService.getUserDiets(currentUser.id(authentication)));
    }

    @Operation(summary = "Get diet plan")
    @GetMapping("/{dietId}")
    public ResponseEntity<DietResponse> getDiet(@PathVariable String dietId, Authentication authentication) {
        return ResponseEntity.ok(dietService.getDietById(dietId, currentUser.id(authentication)));
    }

    @Operation(summary = "Get diet day")
    @GetMapping("/{dietId}/days/{dayNumber}")
    public ResponseEntity<DietDay> getDietDay(@PathVariable String dietId, @PathVariable Integer dayNumber, Authentication authentication) {
        return ResponseEntity.ok(dietService.getDietDay(dietId, dayNumber, currentUser.id(authentication)));
    }
}
