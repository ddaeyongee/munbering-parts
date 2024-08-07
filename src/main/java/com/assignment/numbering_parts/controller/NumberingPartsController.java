package com.assignment.numbering_parts.controller;

import com.assignment.numbering_parts.exception.SequenceLimitExceededException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.assignment.numbering_parts.service.GuidService;
import com.assignment.numbering_parts.service.SequenceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/numbering")
@Tag(name = "Numbering API", description = "GUID 및 시퀀스 생성")
public class NumberingPartsController {

    private final GuidService guidService;
    private final SequenceService sequenceService;

    public NumberingPartsController(GuidService guidService, SequenceService sequenceService) {
        this.guidService = guidService;
        this.sequenceService = sequenceService;
    }

    @GetMapping("/guid")
    @Operation(summary = "GUID 생성", description = "신규 GUID 생성")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getGuid(
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

    @GetMapping("/sequence")
    @Operation(summary = "시퀀스 생성", description = "다음 시퀀스 생성")
    public ResponseEntity<Long> getNextSequence() {
        try {
            return ResponseEntity.ok(sequenceService.getNextSequence());
        } catch (SequenceLimitExceededException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/sequence/current")
    @Operation(summary = "시퀀스 조회", description = "현재 시퀀스 조회")
    public ResponseEntity<Long> getCurrentSequence() {
        return ResponseEntity.ok(sequenceService.getCurrentSequence());
    }
}
