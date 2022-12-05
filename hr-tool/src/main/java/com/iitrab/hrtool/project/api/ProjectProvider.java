package com.iitrab.hrtool.project.api;

import javax.persistence.EntityManager;
import java.util.Optional;

/**
 * API interface for query operations on the {@link Project} entities.
 * Implementation does not have to open new DB transaction. If the returned data has to be managed by the {@link EntityManager},
 * then the caller must open the transaction itself.
 */
public interface ProjectProvider {

    /**
     * Returns the project based on its ID.
     * If the project with given ID is not found, then {@link Optional#empty()} will be returned.
     *
     * @param id id of the project to search
     * @return {@link Optional} containing found project or {@link Optional#empty()} if not found
     */
    Optional<Project> getProject(Long id);

}
