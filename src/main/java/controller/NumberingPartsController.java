package controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.GuidService;
import service.SequenceService;

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
    public String getGuid() {
        return guidService.generateGuid();
    }

    @GetMapping("/sequence")
    public long getNextSequence() {
        return sequenceService.getNextSequence();
    }

    @GetMapping("/sequence/current")
    public long getCurrentSequence() {
        return sequenceService.getCurrentSequence();
    }
}
