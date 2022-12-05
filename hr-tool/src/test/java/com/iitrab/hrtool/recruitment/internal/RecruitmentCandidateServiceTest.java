package com.iitrab.hrtool.recruitment.internal;

import com.iitrab.hrtool.candidate.api.Candidate;
import com.iitrab.hrtool.candidate.api.CandidateNotFoundException;
import com.iitrab.hrtool.candidate.api.CandidateProvider;
import com.iitrab.hrtool.candidate.api.RecruitmentStatus;
import com.iitrab.hrtool.SampleTestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.iitrab.hrtool.candidate.api.CandidateAssert.assertThatCandidate;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecruitmentCandidateServiceTest {

    @Mock
    private CandidateProvider candidateProviderMock;

    @InjectMocks
    RecruitmentCandidateService recruitmentCandidateService;


    @Test
    void shouldReturnNotFoundException_whenPreparingCandidate_withNoExistingCandidate() {
        Long candidateId = 2L;

        when(candidateProviderMock.getCandidate(candidateId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> recruitmentCandidateService.prepareCandidate(candidateId))
                .isInstanceOf(CandidateNotFoundException.class);
    }

    @Test
    void shouldReturnCandidateStatusException_whenPreparingCandidate_withNoAcceptedStatus() {
        Long candidateId = 2L;
        Candidate candidate = SampleTestDataFactory.candidate(RecruitmentStatus.NEW);

        when(candidateProviderMock.getCandidate(candidateId)).thenReturn(Optional.of(candidate));

        assertThatThrownBy(() -> recruitmentCandidateService.prepareCandidate(candidateId))
                .isInstanceOf(CandidateStatusException.class);
    }

    @Test
    void shouldReturnCandidate_whenPreparingCandidate() {
        Long candidateId = 2L;
        Candidate candidate = SampleTestDataFactory.candidate(RecruitmentStatus.ACCEPTED);

        when(candidateProviderMock.getCandidate(candidateId)).thenReturn(Optional.of(candidate));

        Candidate expected = recruitmentCandidateService.prepareCandidate(candidateId);

        assertThatCandidate(expected)
                .hasName(candidate.getName())
                        .hasLastName(candidate.getLastName())
                                .hasRecruitmentStatus(RecruitmentStatus.HIRED);

    }
}
