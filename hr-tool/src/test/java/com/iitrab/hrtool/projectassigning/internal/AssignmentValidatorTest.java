package com.iitrab.hrtool.projectassigning.internal;

import com.iitrab.hrtool.employee.api.Employee;
import com.iitrab.hrtool.project.api.Project;
import com.iitrab.hrtool.SampleTestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatCode;

@ExtendWith(MockitoExtension.class)
class AssignmentValidatorTest {

    @InjectMocks
    private AssignmentValidator assignmentValidator;

    @Test
    void shouldNotThrowException_whenValidatingAssignment() {
        Employee employee = SampleTestDataFactory.employee();
        Project project = SampleTestDataFactory.project(SampleTestDataFactory.client(employee));

        assertThatCode(() -> assignmentValidator.canBeAssigned(employee, project)).doesNotThrowAnyException();
    }

}