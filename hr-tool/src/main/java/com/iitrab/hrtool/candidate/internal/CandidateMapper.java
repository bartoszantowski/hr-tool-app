package com.iitrab.hrtool.candidate.internal;

import com.iitrab.hrtool.candidate.api.Candidate;
import org.springframework.stereotype.Component;

@Component
class CandidateMapper {

    CandidateDto toDto(Candidate candidate) {
        return new CandidateDto(candidate.getId(),
                                candidate.getName(),
                                candidate.getLastName(),
                                candidate.getBirthdate(),
                                candidate.getRecruitmentStatus(),
                                candidate.getPrivateEmail());
    }

}
