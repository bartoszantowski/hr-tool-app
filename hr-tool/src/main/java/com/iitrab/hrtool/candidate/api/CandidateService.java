package com.iitrab.hrtool.candidate.api;

/**
 * API interface for performing modifying operations on {@link Candidate} entities.
 * Implementation should perform the change in the database transaction either by continuing the existing one or creating new if needed.
 */
public interface CandidateService {

    /**
     * Updates the candidate status to the provided one and persists the change.
     * If the candidate has not been persisted yet (does not have ID), then the implementation might throw {@link IllegalArgumentException}.
     *
     * @param candidate candidate to update the recruitment status
     * @param status    new status of the candidate recruitment
     * @return updated candidate
     */
    Candidate updateRecruitmentStatus(Candidate candidate, RecruitmentStatus status);

}
