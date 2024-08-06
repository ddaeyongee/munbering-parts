package service;

import entity.Sequence;
import exception.SequenceLimitExceededException;
import org.springframework.cache.annotation.Cacheable;
import repository.SequenceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class SequenceService {

    private final SequenceRepository sequenceRepository;

    public SequenceService(SequenceRepository sequenceRepository) {
        this.sequenceRepository = sequenceRepository;
    }

    @Transactional
    @Cacheable(value = "sequences", key = "#root.method.name")
    public long getNextSequence() {
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
        newSequence.setCurrentValue(1L);
        return sequenceRepository.save(newSequence);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "sequences", key = "#root.method.name")
    public long getCurrentSequence() {
        LocalDate today = LocalDate.now();
        return sequenceRepository.findByDateForUpdate(today)
                .map(Sequence::getCurrentValue)
                .orElse(0L);
    }
}