package com.assignment.numbering_parts.repository;

import com.assignment.numbering_parts.entity.Sequence;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class SequenceRepository {
    private final Map<LocalDate, Sequence> sequences = new HashMap<>();

    public Optional<Sequence> findByDateForUpdate(LocalDate date) {
        return Optional.ofNullable(sequences.get(date));
    }

    public Sequence save(Sequence sequence) {
        sequences.put(sequence.getDate(), sequence);
        return sequence;
    }
}