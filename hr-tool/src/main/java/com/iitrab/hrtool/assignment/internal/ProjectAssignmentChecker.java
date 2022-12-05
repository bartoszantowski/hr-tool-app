package com.iitrab.hrtool.assignment.internal;

import com.iitrab.hrtool.assignment.api.ProjectAssignment;
import com.iitrab.hrtool.util.TimeProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
class ProjectAssignmentChecker {

    private final TimeProvider timeProvider;

    boolean isActiveAssignment(ProjectAssignment projectAssignment) {
        LocalDate now = timeProvider.dateNow();
        return !now.isBefore(projectAssignment.getStartDate()) && (projectAssignment.getEndDate() == null || !now.isAfter(
                projectAssignment.getEndDate()));
    }

}
