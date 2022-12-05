package com.iitrab.hrtool.candidate.internal;

import com.iitrab.hrtool.candidate.api.Candidate;
import com.iitrab.hrtool.SampleTestDataFactory;
import com.iitrab.hrtool.candidate.api.RecruitmentStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.iitrab.hrtool.candidate.api.CandidateAssert.assertThatCandidate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CandidateServiceImplTest {

    @InjectMocks
    private CandidateServiceImpl candidateService;
    @Mock
    private CandidateRepository candidateRepositoryMock;
    @Captor
    private ArgumentCaptor<Candidate> candidateCaptor;

    @Test
    void shouldReturnAllFoundCandidates_whenFindingCandidatesBySearchCriteria() {
        Candidate candidate1 = SampleTestDataFactory.candidate(RecruitmentStatus.ACCEPTED);
        Candidate candidate2 = SampleTestDataFactory.candidate(RecruitmentStatus.REJECTED);
        CandidateSearchCriteria searchCriteria = new CandidateSearchCriteria(RecruitmentStatus.ACCEPTED);
        when(candidateRepositoryMock.findAllBySearchCriteria(searchCriteria)).thenReturn(List.of(candidate1,
                                                                                                 candidate2));

        List<Candidate> foundCandidates = candidateService.findMatchingCandidates(searchCriteria);

        assertThat(foundCandidates).containsExactly(candidate1, candidate2);
    }

    @Test
    void shouldReturnEmpty_whenFindingCandidateById_andNoneIsFound() {
        long candidateId = 1L;
        when(candidateRepositoryMock.findById(candidateId)).thenReturn(Optional.empty());

        Optional<Candidate> foundCandidate = candidateService.getCandidate(candidateId);

        assertThat(foundCandidate).isEmpty();
    }

    @Test
    void shouldReturnFoundCandidate_whenFindingCandidateById() {
        long candidateId = 1L;
        Candidate candidate = SampleTestDataFactory.candidate(RecruitmentStatus.ACCEPTED);
        when(candidateRepositoryMock.findById(candidateId)).thenReturn(Optional.of(candidate));

        Optional<Candidate> foundCandidate = candidateService.getCandidate(candidateId);

        assertThat(foundCandidate).contains(candidate);
    }

    @Test
    void shouldPersistUpdatedCandidate_whenUpdatingRecruitmentStatus() {
        Candidate candidate = SampleTestDataFactory.candidate(RecruitmentStatus.ACCEPTED);

        candidateService.updateRecruitmentStatus(candidate, RecruitmentStatus.HIRED);

        verify(candidateRepositoryMock).saveAndFlush(candidateCaptor.capture());
        assertThatCandidate(candidate).isSameAs(candidate)
                                      .hasRecruitmentStatus(RecruitmentStatus.HIRED);
    }

    @Test
    void shouldReturnUpdatedCandidate_whenUpdatingRecruitmentStatus() {
        Candidate candidate = SampleTestDataFactory.candidate(RecruitmentStatus.ACCEPTED);
        when(candidateRepositoryMock.saveAndFlush(any())).then(returnsFirstArg());

        Candidate updatedCandidate = candidateService.updateRecruitmentStatus(candidate, RecruitmentStatus.HIRED);

        assertThatCandidate(updatedCandidate).hasRecruitmentStatus(RecruitmentStatus.HIRED);
    }

}