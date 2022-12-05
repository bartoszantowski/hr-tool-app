package com.iitrab.hrtool.assignment.api;

import com.iitrab.hrtool.employee.api.EmployeeAssert;
import com.iitrab.hrtool.project.api.ProjectAssert;
import org.assertj.core.api.AbstractAssert;
import org.eclipse.jdt.annotation.Nullable;

import java.time.LocalDate;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public final class ProjectAssignmentAssert extends AbstractAssert<ProjectAssignmentAssert, ProjectAssignment> {

    private ProjectAssignmentAssert(ProjectAssignment projectAssignment) {
        super(projectAssignment, ProjectAssignmentAssert.class);
    }

    public static ProjectAssignmentAssert assertThatProjectAssignment(ProjectAssignment projectAssignment) {
        return new ProjectAssignmentAssert(projectAssignment);
    }

    public ProjectAssignmentAssert hasId(@Nullable Long id) {
        isNotNull();
        assertThat(actual.getId()).isEqualTo(id);
        return this;
    }

    public ProjectAssignmentAssert isForEmployeeThat(Consumer<EmployeeAssert> employee) {
        isNotNull();
        employee.accept(EmployeeAssert.assertThatEmployee(actual.getEmployee()));
        return this;
    }

    public ProjectAssignmentAssert isToProject(Consumer<ProjectAssert> project) {
        isNotNull();
        project.accept(ProjectAssert.assertThatProject(actual.getProject()));
        return this;
    }

    public ProjectAssignmentAssert startsOn(LocalDate startDate) {
        isNotNull();
        assertThat(actual.getStartDate()).isEqualTo(startDate);
        return this;
    }

    public ProjectAssignmentAssert endsOn(@Nullable LocalDate endDate) {
        isNotNull();
        assertThat(actual.getEndDate()).isEqualTo(endDate);
        return this;
    }

    public ProjectAssignmentAssert doesNotHaveEndDate() {
        return endsOn(null);
    }

    public ProjectAssignmentAssert hasRole(String role) {
        isNotNull();
        assertThat(actual.getRole()).isEqualTo(role);
        return this;
    }

}
