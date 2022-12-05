package com.iitrab.hrtool.candidate.internal;

import com.iitrab.hrtool.IntegrationTestBase;
import com.iitrab.hrtool.candidate.api.Candidate;
import com.iitrab.hrtool.candidate.api.CandidateAssert;
import com.iitrab.hrtool.candidate.api.RecruitmentStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class CandidateRepositoryTest extends IntegrationTestBase {

    @Autowired
    private CandidateRepository candidateRepository;

    @Test
    void shouldReturnAllCandidates_whenSearchingForCandidates_withEmptySearchRequest() {
        Candidate candidate1 = existingCandidate(new Candidate("John",
                                                               "Kovalsky",
                                                               LocalDate.now(),
                                                               "john.kovalsky@private.com",
                                                               RecruitmentStatus.NEW));
        Candidate candidate2 = existingCandidate(new Candidate("Bob",
                                                               "Smith",
                                                               LocalDate.now(),
                                                               "bob.smith@private.com",
                                                               RecruitmentStatus.ACCEPTED));
        CandidateSearchCriteria searchCriteria = new CandidateSearchCriteria(null);

        List<Candidate> foundCandidates = candidateRepository.findAllBySearchCriteria(searchCriteria);

        assertThat(foundCandidates).hasSize(2)
                                   .anySatisfy(cand -> CandidateAssert.assertThatCandidate(cand).hasId(candidate1.getId()))
                                   .anySatisfy(cand -> CandidateAssert.assertThatCandidate(cand).hasId(candidate2.getId()));
    }

    @Test
    void shouldReturnAllCandidatesWithMatchingRecruitmentStatus_whenSearchingForCandidates_byRecruitmentStatus() {
        Candidate candidate1 = existingCandidate(new Candidate("John",
                                                               "Kovalsky",
                                                               LocalDate.now(),
                                                               "john.kovalsky@private.com",
                                                               RecruitmentStatus.NEW));
        Candidate candidate2 = existingCandidate(new Candidate("Bob",
                                                               "Smith",
                                                               LocalDate.now(),
                                                               "bob.smith@private.com",
                                                               RecruitmentStatus.NEW));
        CandidateSearchCriteria searchCriteria = new CandidateSearchCriteria(RecruitmentStatus.NEW);

        List<Candidate> foundCandidates = candidateRepository.findAllBySearchCriteria(searchCriteria);

        assertThat(foundCandidates).hasSize(2)
                                   .anySatisfy(cand -> CandidateAssert.assertThatCandidate(cand).hasId(candidate1.getId()))
                                   .anySatisfy(cand -> CandidateAssert.assertThatCandidate(cand).hasId(candidate2.getId()));
    }

    @Test
    void shouldNotReturnCandidatesWithDifferentStatuses_whenSearchingForCandidates_byRecruitmentStatus() {
        existingCandidate(new Candidate("John", "Kovalsky", LocalDate.now(), "john.kovalsky@private.com", RecruitmentStatus.NEW));
        existingCandidate(new Candidate("Bob", "Smith", LocalDate.now(), "bob.smith@private.com", RecruitmentStatus.ACCEPTED));
        CandidateSearchCriteria searchCriteria = new CandidateSearchCriteria(RecruitmentStatus.HIRED);

        List<Candidate> foundCandidates = candidateRepository.findAllBySearchCriteria(searchCriteria);

        assertThat(foundCandidates).isEmpty();
    }

}