package com.iitrab.hrtool.candidate.internal;

import com.iitrab.hrtool.SampleTestDataFactory;
import com.iitrab.hrtool.candidate.api.Candidate;
import com.iitrab.hrtool.candidate.api.RecruitmentStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CandidateMapperTest {

    @InjectMocks
    private CandidateMapper candidateMapper;

    @Test
    void shouldReturnMappedCandidate_whenMappingToDto() {
        Candidate candidate = SampleTestDataFactory.candidate(RecruitmentStatus.ACCEPTED);

        CandidateDto candidateDto = candidateMapper.toDto(candidate);

        CandidateDto expectedDto = new CandidateDto(candidate.getId(),
                                                    candidate.getName(),
                                                    candidate.getLastName(),
                                                    candidate.getBirthdate(),
                                                    candidate.getRecruitmentStatus(),
                                                    candidate.getPrivateEmail());
        assertThat(candidateDto).isEqualTo(expectedDto);
    }

}