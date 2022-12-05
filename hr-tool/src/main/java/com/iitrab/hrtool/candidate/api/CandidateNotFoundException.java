package com.iitrab.hrtool.candidate.api;

import com.iitrab.hrtool.exception.api.NotFoundException;

/**
 * Exception indicating that the {@link Candidate} was not found.
 */
@SuppressWarnings("squid:S110")
public class CandidateNotFoundException extends NotFoundException {

    public CandidateNotFoundException(String message) {
        super(message);
    }

    public CandidateNotFoundException(Long id) {
        this("Candidate with ID=%s was not found".formatted(id));
    }

}
