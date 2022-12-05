package com.iitrab.hrtool.projectassigning.internal;

import com.iitrab.hrtool.assignment.api.ProjectAssignment;
import com.iitrab.hrtool.employee.api.Employee;
import com.iitrab.hrtool.project.api.Project;
import com.iitrab.hrtool.SampleTestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class AssignmentMapperTest {

    private static final String ROLE = "Developer";

    @InjectMocks
    private AssignmentMapper assignmentMapper;

    @Test
    void shouldReturnMappedDto_whenMappingProjectAssignment() {
        Employee employee = SampleTestDataFactory.employee();
        Project project = SampleTestDataFactory.project(SampleTestDataFactory.client(employee));
        ProjectAssignment assignment = new ProjectAssignment(employee,
                                                             project,
                                                             SampleTestDataFactory.DATE_IN_PAST,
                                                             SampleTestDataFactory.DATE_IN_FUTURE,
                                                             ROLE);

        AssignmentDto dto = assignmentMapper.toDto(assignment);

        AssignmentDto expected = new AssignmentDto(null,
                                                   new ProjectDto(project.getId(), project.getName()),
                                                   new EmployeeDto(employee.getId(),
                                                                   employee.getName(),
                                                                   employee.getLastName(),
                                                                   employee.getEmail()),
                                                   ROLE,
                                                   SampleTestDataFactory.DATE_IN_PAST,
                                                   SampleTestDataFactory.DATE_IN_FUTURE);
        assertThat(dto).isEqualTo(expected);
    }

}