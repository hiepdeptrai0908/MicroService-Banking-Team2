package com.sequence_generator.controller;

import com.sequence_generator.model.entity.Sequence;
import com.sequence_generator.service.SequenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/sequence")
public class SequenceController {

    private final SequenceService sequenceService;

    /**
     * Generates an account number.
     *
     * @return The generated account number.
     */
    @PostMapping
    public Sequence generateAccountNumber() {
        return sequenceService.create();
    }
}
