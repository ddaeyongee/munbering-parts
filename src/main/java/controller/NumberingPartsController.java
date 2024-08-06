package controller;

import exception.SequenceLimitExceededException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.GuidService;
import service.SequenceService;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/numbering")
public class NumberingPartsController {

    private final GuidService guidService;
    private final SequenceService sequenceService;

    public NumberingPartsController(GuidService guidService, SequenceService sequenceService) {
        this.guidService = guidService;
        this.sequenceService = sequenceService;
    }

    @GetMapping("/guid")
    public CompletableFuture<ResponseEntity<String>> getGuid() {
        return CompletableFuture.supplyAsync(guidService::generateGuid)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/sequence")
    public ResponseEntity<Long> getNextSequence() {
        try {
            return ResponseEntity.ok(sequenceService.getNextSequence());
        } catch (SequenceLimitExceededException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/sequence/current")
    public ResponseEntity<Long> getCurrentSequence() {
        return ResponseEntity.ok(sequenceService.getCurrentSequence());
    }
}
