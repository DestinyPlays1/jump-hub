package com.jumphub.controller;

import com.jumphub.dto.ApiResponse;
import com.jumphub.model.InterviewQuestion;
import com.jumphub.service.InterviewService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/interviews")
public class InterviewController {

    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @GetMapping("/categories")
    public ApiResponse<Map<String, Object>> getCategories() {
        return ApiResponse.success(interviewService.getCategories());
    }

    @GetMapping
    public ApiResponse<List<InterviewQuestion>> getQuestions(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String difficulty) {
        return ApiResponse.success(interviewService.getQuestions(category, difficulty));
    }

    @GetMapping("/random")
    public ApiResponse<InterviewQuestion> getRandom() {
        return interviewService.getRandomQuestion()
                .map(ApiResponse::success)
                .orElse(ApiResponse.error(404, "No questions available"));
    }

    @GetMapping("/{category}/{slug}")
    public ApiResponse<InterviewQuestion> getQuestion(
            @PathVariable String category,
            @PathVariable String slug) {
        return interviewService.getQuestion(category, slug)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error(404, "Question not found"));
    }
}
