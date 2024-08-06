package com.assignment.numbering_parts.exception;

public class SequenceLimitExceededException extends RuntimeException {
    public SequenceLimitExceededException(String message) {
        super(message);
    }
}