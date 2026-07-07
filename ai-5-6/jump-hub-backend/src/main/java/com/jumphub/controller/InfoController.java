package com.jumphub.controller;

import com.jumphub.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/info")
public class InfoController {

    @Value("${spring.application.name:JumpHub}")
    private String appName;

    @GetMapping
    public ApiResponse<Map<String, Object>> getInfo() {
        Map<String, Object> info = Map.of(
                "name", appName,
                "version", "1.0.0",
                "serverTime", Instant.now().toString(),
                "sources", Map.of(
                        "trending", "GitHub Search API",
                        "news", Map.of(
                                "hackernews", "HackerNews Firebase API",
                                "devto", "Dev.to API",
                                "zhihu", "知乎热榜 API",
                                "juejin", "掘金推荐 API",
                                "v2ex", "V2EX API"
                        ),
                        "interviews", "本地预置题库"
                )
        );
        return ApiResponse.success(info);
    }
}
