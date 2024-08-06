package com.assignment.numbering_parts.controller;

import com.assignment.numbering_parts.exception.SequenceLimitExceededException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.assignment.numbering_parts.service.GuidService;
import com.assignment.numbering_parts.service.SequenceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/numbering")
@Tag(name = "Numbering API", description = "API for generating GUIDs and Sequences")
public class NumberingPartsController {

    private final GuidService guidService;
    private final SequenceService sequenceService;

    public NumberingPartsController(GuidService guidService, SequenceService sequenceService) {
        this.guidService = guidService;
        this.sequenceService = sequenceService;
    }

    @GetMapping("/guid")
    @Operation(summary = "Generate GUID", description = "Generates a new Globally Unique Identifier")
    public CompletableFuture<ResponseEntity<String>> getGuid() {
        return CompletableFuture.supplyAsync(guidService::generateGuid)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/sequence")

    @Operation(summary = "Get Next Sequence", description = "Retrieves the next sequence number")
    public ResponseEntity<Long> getNextSequence() {
        try {
            return ResponseEntity.ok(sequenceService.getNextSequence());
        } catch (SequenceLimitExceededException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/sequence/current")
    @Operation(summary = "Get Current Sequence", description = "Retrieves the current sequence number")
    public ResponseEntity<Long> getCurrentSequence() {
        return ResponseEntity.ok(sequenceService.getCurrentSequence());
    }
}
