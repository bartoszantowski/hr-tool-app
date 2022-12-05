package com.iitrab.hrtool.project.internal;

import com.iitrab.hrtool.project.api.Project;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.iitrab.hrtool.SampleTestDataFactory.*;
import static com.iitrab.hrtool.project.internal.ProjectDtoAssert.assertThatProjectDto;

@ExtendWith(MockitoExtension.class)
class ProjectMapperTest {

    @InjectMocks
    private ProjectMapper projectMapper;

    @Test
    void shouldReturnProjectDto_whenMappingProjectToDto() {
        Project project = project(client(employee()));

        ProjectDto projectDto = projectMapper.toDto(project);

        assertThatProjectDto(projectDto).hasName(project.getName());
    }

}