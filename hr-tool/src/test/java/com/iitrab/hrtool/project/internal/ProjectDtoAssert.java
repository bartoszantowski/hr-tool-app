package com.iitrab.hrtool.project.internal;

import org.assertj.core.api.AbstractAssert;
import org.eclipse.jdt.annotation.Nullable;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public class ProjectDtoAssert extends AbstractAssert<ProjectDtoAssert, ProjectDto> {

    private ProjectDtoAssert(ProjectDto projectDto) {
        super(projectDto, ProjectDtoAssert.class);
    }

    public static ProjectDtoAssert assertThatProjectDto(ProjectDto projectDto) {
        return new ProjectDtoAssert(projectDto);
    }

    public ProjectDtoAssert hasId(@Nullable Long id) {
        isNotNull();
        assertThat(actual.getId()).isEqualTo(id);
        return this;
    }

    public ProjectDtoAssert hasClientId(Long clientId) {
        isNotNull();
        assertThat(actual.getClientId()).isEqualTo(clientId);
        return this;
    }

    public ProjectDtoAssert hasName(String name) {
        isNotNull();
        assertThat(actual.getName()).isEqualTo(name);
        return this;
    }

}
