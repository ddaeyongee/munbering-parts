package com.assignment.numbering_parts.entity;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class Sequence {
    private Long id;
    private LocalDate date;
    private Long currentValue;
}