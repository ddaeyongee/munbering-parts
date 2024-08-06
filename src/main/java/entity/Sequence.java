package entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "sequences")
@Getter
@Setter
public class Sequence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private Long currentValue;
}
