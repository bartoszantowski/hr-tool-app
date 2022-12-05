package com.iitrab.hrtool.candidate.internal;

import com.iitrab.hrtool.candidate.api.Candidate;
import com.iitrab.hrtool.candidate.api.RecruitmentStatus;
import org.eclipse.jdt.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.function.Predicate;

interface CandidateRepository extends JpaRepository<Candidate, Long> {

    /**
     * Query for searching the candidates by provided criteria.
     *
     * @param searchCriteria criteria for search
     * @return {@link List} of found candidates
     */
    default List<Candidate> findAllBySearchCriteria(CandidateSearchCriteria searchCriteria) {
        Predicate<Candidate> recruitmentStatusMatcher = recruitmentStatusMatcher(searchCriteria.status());
        return findAll().stream()
                        .filter(recruitmentStatusMatcher)
                        .toList();
    }

    private Predicate<Candidate> recruitmentStatusMatcher(@Nullable RecruitmentStatus status) {
        return candidate -> status == null || candidate.getRecruitmentStatus() == status;
    }

}
