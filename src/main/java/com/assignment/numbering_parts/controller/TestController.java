package com.assignment.numbering_parts.controller;

import com.assignment.numbering_parts.exception.SequenceLimitExceededException;
import com.assignment.numbering_parts.service.GuidService;
import com.assignment.numbering_parts.service.SequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/numbering/test")
public class TestController {

    private final GuidService guidService;
    private final SequenceService sequenceService;

    @Autowired
    public TestController(GuidService guidService, SequenceService sequenceService) {
        this.guidService = guidService;
        this.sequenceService = sequenceService;
    }

    @GetMapping("/guid")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> testGuid(
            @RequestHeader(value = "X-Server-Trace", required = false) String serverTrace) {
        return CompletableFuture.supplyAsync(() -> {
            String guid = guidService.generateGuid();
            Map<String, Object> result = new HashMap<>();
            result.put("guid", guid);
            result.put("길이", guid.length());
            result.put("유효성", guid.matches("[a-zA-Z0-9-]+"));
            result.put("서버추적정보", serverTrace != null ? serverTrace : "없음");
            return ResponseEntity.ok(result);
        });
    }

    @GetMapping("/sequence/next")
    public ResponseEntity<Map<String, Object>> testNextSequence() {
        Map<String, Object> result = new HashMap<>();
        try {
            long nextSequence = sequenceService.getNextSequence();
            result.put("다음 시퀀스", nextSequence);
            result.put("상태", "성공");
        } catch (SequenceLimitExceededException e) {
            result.put("상태", "오류");
            result.put("메시지", e.getMessage());
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/sequence/current")
    public ResponseEntity<Map<String, Object>> testCurrentSequence() {
        Map<String, Object> result = new HashMap<>();
        long currentSequence = sequenceService.getCurrentSequence();
        result.put("현재 시퀀스", currentSequence);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/sequence/limit")
    public ResponseEntity<Map<String, Object>> testSequenceLimit() {
        Map<String, Object> result = new HashMap<>();
        try {
            // 시퀀스 한계 테스트(반복 호출)
            for (long i = 0; i < 10_000_000_000L; i++) {
                sequenceService.getNextSequence();
            }
            result.put("상태", "오류");
            result.put("메시지", "예상된 SequenceLimitExceededException 발생 안함");
        } catch (SequenceLimitExceededException e) {
            result.put("상태", "성공");
            result.put("메시지", "예상대로 SequenceLimitExceededException 발생");
        }
        return ResponseEntity.ok(result);
    }
}