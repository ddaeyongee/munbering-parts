package com.assignment.numbering_parts.service;

import com.assignment.numbering_parts.entity.Sequence;
import com.assignment.numbering_parts.exception.SequenceLimitExceededException;
import com.assignment.numbering_parts.repository.SequenceRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class SequenceService {

    private final SequenceRepository sequenceRepository;

    public SequenceService(SequenceRepository sequenceRepository) {
        this.sequenceRepository = sequenceRepository;
    }

    @Cacheable(value = "sequences", key = "#root.method.name")
    public synchronized long getNextSequence() {
        LocalDate today = LocalDate.now();
        Sequence sequence = sequenceRepository.findByDateForUpdate(today)
                .orElseGet(() -> createNewSequence(today));

        long nextValue = sequence.getCurrentValue() + 1;
        if (nextValue > 9_999_999_999L) {
            throw new SequenceLimitExceededException("최대값 도달");
        }

        sequence.setCurrentValue(nextValue);
        sequenceRepository.save(sequence);

        return nextValue;
    }

    private Sequence createNewSequence(LocalDate date) {
        Sequence newSequence = new Sequence();
        newSequence.setDate(date);
        newSequence.setCurrentValue(0L);
        return sequenceRepository.save(newSequence);
    }

    @Cacheable(value = "sequences", key = "#root.method.name")
    public long getCurrentSequence() {
        LocalDate today = LocalDate.now();
        return sequenceRepository.findByDateForUpdate(today)
                .map(Sequence::getCurrentValue)
                .orElse(0L);
    }
}