package com.iitrab.hrtool.candidate.internal;

import com.iitrab.hrtool.candidate.api.Candidate;
import com.iitrab.hrtool.candidate.api.CandidateProvider;
import com.iitrab.hrtool.candidate.api.CandidateService;
import com.iitrab.hrtool.candidate.api.RecruitmentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
class CandidateServiceImpl implements CandidateService, CandidateProvider {

    private final CandidateRepository candidateRepository;

    @Override
    public Optional<Candidate> getCandidate(Long id) {
        return candidateRepository.findById(id);
    }

    @Override
    public Candidate updateRecruitmentStatus(Candidate candidate, RecruitmentStatus status) {
        log.info("Changing the recruitment status of candidate with ID={} to {}", candidate.getId(), status);
        candidate.setRecruitmentStatus(status);
        return candidateRepository.saveAndFlush(candidate);
    }

    /**
     * Returns all candidates that match the provided search criteria.
     *
     * @param searchCriteria criteria, that candidate should match
     * @return list of all found candidates
     */
    List<Candidate> findMatchingCandidates(CandidateSearchCriteria searchCriteria) {
        return candidateRepository.findAllBySearchCriteria(searchCriteria);
    }

}
