package com.iitrab.hrtool.recruitment.internal;

import com.iitrab.hrtool.candidate.api.Candidate;
import com.iitrab.hrtool.candidate.api.CandidateNotFoundException;
import com.iitrab.hrtool.candidate.api.CandidateProvider;
import com.iitrab.hrtool.candidate.api.RecruitmentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service is responsible for finalizing the candidate recruitment process.
 */
@Service
@RequiredArgsConstructor
@Slf4j
class RecruitmentCandidateService {
    private final CandidateProvider candidateProvider;

    /**
     * This method prepares the selected candidate's ID to complete the recruitment process.
     * Gets a candidate from the database - if there is one.
     * Checks the candidate's status if it is 'ACCEPTED'.
     * Changes the status of a candidate to 'HIRED'.
     *
     * @param candidateId candidate id
     * @return candidate
     */
    Candidate prepareCandidate(Long candidateId) {
        log.info("Checking the candidate...");
        Candidate candidate = getCandidate(candidateId);
        log.info("The candidate with ID=%s exist".formatted(candidateId));
        log.info("Checking the candidate's status...");
        isAccepted(candidate);
        log.info("Candidate's status is ACCEPTED");
        log.info("Changing candidate status...");
        candidate.setRecruitmentStatus(RecruitmentStatus.HIRED);
        log.info("Changed candidate with ID=%s status to HIRED.".formatted(candidateId));

        return candidate;
    }

    private Candidate getCandidate(Long candidateId) {
        return candidateProvider.getCandidate(candidateId)
                .orElseThrow(() -> new CandidateNotFoundException(candidateId));
    }

    private void isAccepted(Candidate candidate) {
        if (candidate.getRecruitmentStatus() != RecruitmentStatus.ACCEPTED) {
            log.info("The candidate has wrong recruitment status to be hired!");
            throw new CandidateStatusException(candidate.getId(), candidate.getRecruitmentStatus());
        }
    }
}
