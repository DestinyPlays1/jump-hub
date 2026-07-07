package com.jumphub.service;

import com.jumphub.model.InterviewQuestion;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InterviewService {

    private final ObjectMapper objectMapper;
    private List<InterviewQuestion> allQuestions = new ArrayList<>();

    private static final Map<String, String> CATEGORY_NAMES = Map.ofEntries(
            Map.entry("algorithms", "算法与数据结构"),
            Map.entry("frontend", "前端"),
            Map.entry("backend", "后端"),
            Map.entry("system-design", "系统设计"),
            Map.entry("database", "数据库"),
            Map.entry("network", "计算机网络"),
            Map.entry("os", "操作系统")
    );

    public InterviewService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() {
        try {
            InputStream is = new ClassPathResource("data/questions.json").getInputStream();
            allQuestions = objectMapper.readValue(is, new TypeReference<List<InterviewQuestion>>() {});
        } catch (Exception e) {
            allQuestions = new ArrayList<>();
        }
    }

    public Map<String, Object> getCategories() {
        Map<String, Long> counts = allQuestions.stream()
                .collect(Collectors.groupingBy(InterviewQuestion::getCategory, Collectors.counting()));
        Map<String, Object> result = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : CATEGORY_NAMES.entrySet()) {
            Map<String, Object> cat = new HashMap<>();
            cat.put("name", entry.getValue());
            cat.put("count", counts.getOrDefault(entry.getKey(), 0L));
            result.put(entry.getKey(), cat);
        }
        return result;
    }

    public List<InterviewQuestion> getQuestions(String category, String difficulty) {
        return allQuestions.stream()
                .filter(q -> category == null || category.isEmpty() || q.getCategory().equals(category))
                .filter(q -> difficulty == null || difficulty.isEmpty() || q.getDifficulty().equals(difficulty))
                .collect(Collectors.toList());
    }

    public Optional<InterviewQuestion> getQuestion(String category, String slug) {
        return allQuestions.stream()
                .filter(q -> q.getCategory().equals(category) && q.getSlug().equals(slug))
                .findFirst();
    }

    public Optional<InterviewQuestion> getRandomQuestion() {
        if (allQuestions.isEmpty()) return Optional.empty();
        return Optional.of(allQuestions.get(new Random().nextInt(allQuestions.size())));
    }
}
