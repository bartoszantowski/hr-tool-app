package com.iitrab.hrtool.assignment.api;

/**
 * API interface for performing modifying operations on {@link ProjectAssignment} entities.
 * Implementation should perform the change in the database transaction either by continuing the existing one or creating new if needed.
 */
public interface ProjectAssignmentService {

    /**
     * Persists the passed project assignment.
     * If the assignment has already DB ID assigned, then the implementation might throw an {@link IllegalArgumentException}.
     *
     * @param projectAssignment assignment to create
     * @return created assignment
     */
    ProjectAssignment create(ProjectAssignment projectAssignment);

}
