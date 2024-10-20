package com.sequence_generator.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Sequence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long sequenceId;

    private long accountNumber;
}
