package com.sequence_generator.reporitory;

import com.sequence_generator.model.entity.Sequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface SequenceRepository extends JpaRepository<Sequence, Long> {

    @Query("SELECT COUNT(s) from Sequence s")
    int countAll();

    Sequence findFirstByOrderBySequenceIdDesc();

}
