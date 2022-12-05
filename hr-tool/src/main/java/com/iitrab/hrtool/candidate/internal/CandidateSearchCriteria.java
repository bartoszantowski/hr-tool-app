package com.iitrab.hrtool.candidate.internal;

import com.iitrab.hrtool.candidate.api.RecruitmentStatus;
import org.eclipse.jdt.annotation.Nullable;

/**
 * Criteria for searching the candidates.
 * Null parameters in the constructor indicate, that given criteria should not be taken for candidate filtering.
 * For no search criteria (all candidates should be found), please use {@link CandidateSearchCriteria#EMPTY}.
 *
 * @param status status of the candidate recruitment
 */
record CandidateSearchCriteria(@Nullable RecruitmentStatus status) {

    static final CandidateSearchCriteria EMPTY = new CandidateSearchCriteria(null);

}
