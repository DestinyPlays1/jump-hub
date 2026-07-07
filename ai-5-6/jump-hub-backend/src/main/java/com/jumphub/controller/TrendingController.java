package com.jumphub.controller;

import com.jumphub.dto.ApiResponse;
import com.jumphub.model.TrendingRepo;
import com.jumphub.service.TrendingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trending")
public class TrendingController {

    private final TrendingService trendingService;

    public TrendingController(TrendingService trendingService) {
        this.trendingService = trendingService;
    }

    @GetMapping
    public ApiResponse<List<TrendingRepo>> getTrending(
            @RequestParam(defaultValue = "daily") String period,
            @RequestParam(defaultValue = "all") String language) {
        List<TrendingRepo> repos = trendingService.getTrending(period, language);
        return ApiResponse.success(repos);
    }
}
