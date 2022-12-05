package com.iitrab.hrtool.project.api;

import com.iitrab.hrtool.exception.api.NotFoundException;

/**
 * Exception indicating that the {@link Project} was not found.
 */
@SuppressWarnings("squid:S110")
public class ProjectNotFoundException extends NotFoundException {

    public ProjectNotFoundException(String message) {
        super(message);
    }

    public ProjectNotFoundException(Long id) {
        this("Project with ID=%s was not found".formatted(id));
    }

}
