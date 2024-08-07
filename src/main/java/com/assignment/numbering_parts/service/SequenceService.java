package com.assignment.numbering_parts.service;

import com.assignment.numbering_parts.entity.Sequence;
import com.assignment.numbering_parts.exception.SequenceLimitExceededException;
import com.assignment.numbering_parts.repository.SequenceRepository;
import com.assignment.numbering_parts.util.RedisLockUtil;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class SequenceService {

    private final SequenceRepository sequenceRepository;
    private final RedisLockUtil redisLockUtil;

    public SequenceService(SequenceRepository sequenceRepository, RedisLockUtil redisLockUtil) {
        this.sequenceRepository = sequenceRepository;
        this.redisLockUtil = redisLockUtil;
    }

    public long getNextSequence() {
        String lockKey = "sequenceLock:" + LocalDate.now();
        try {
            if (redisLockUtil.tryLock(lockKey, 10000)) {
                return getAndIncrementSequence();
            } else {
                throw new RuntimeException("Failed to acquire lock");
            }
        } catch (RedisConnectionFailureException e) {
            return getAndIncrementSequenceWithoutLock();
        } finally {
            try {
                redisLockUtil.unlock(lockKey);
            } catch (RedisConnectionFailureException e) {
                System.out.println("Failed to release lock: " + e.getMessage());
            }
        }
    }

    private long getAndIncrementSequence() {
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

    private long getAndIncrementSequenceWithoutLock() {
        return getAndIncrementSequence();
    }

    private Sequence createNewSequence(LocalDate date) {
        Sequence newSequence = new Sequence();
        newSequence.setDate(date);
        newSequence.setCurrentValue(0L);
        return sequenceRepository.save(newSequence);
    }

    public long getCurrentSequence() {
        LocalDate today = LocalDate.now();
        return sequenceRepository.findByDateForUpdate(today)
                .map(Sequence::getCurrentValue)
                .orElse(0L);
    }
}