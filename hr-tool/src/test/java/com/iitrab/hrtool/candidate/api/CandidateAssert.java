package com.iitrab.hrtool.candidate.api;

import org.assertj.core.api.AbstractAssert;
import org.eclipse.jdt.annotation.Nullable;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public final class CandidateAssert extends AbstractAssert<CandidateAssert, Candidate> {

    private CandidateAssert(Candidate candidate) {
        super(candidate, CandidateAssert.class);
    }

    public static CandidateAssert assertThatCandidate(Candidate employee) {
        return new CandidateAssert(employee);
    }

    public CandidateAssert hasId(@Nullable Long id) {
        isNotNull();
        assertThat(actual.getId()).isEqualTo(id);
        return this;
    }

    public CandidateAssert hasName(String name) {
        isNotNull();
        assertThat(actual.getName()).isEqualTo(name);
        return this;
    }

    public CandidateAssert hasLastName(String lastName) {
        isNotNull();
        assertThat(actual.getLastName()).isEqualTo(lastName);
        return this;
    }

    public CandidateAssert wasBornOn(LocalDate date) {
        isNotNull();
        assertThat(actual.getBirthdate()).isEqualTo(date);
        return this;
    }

    public CandidateAssert hasRecruitmentStatus(RecruitmentStatus status) {
        isNotNull();
        assertThat(actual.getRecruitmentStatus()).isEqualTo(status);
        return this;
    }

}
