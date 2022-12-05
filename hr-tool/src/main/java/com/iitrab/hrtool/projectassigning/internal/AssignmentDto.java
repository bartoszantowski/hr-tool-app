package com.iitrab.hrtool.projectassigning.internal;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.eclipse.jdt.annotation.Nullable;

import java.time.LocalDate;

record AssignmentDto(@Nullable Long id, @Nullable ProjectDto project, @Nullable EmployeeDto employee, String role,
                     @JsonFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                     @JsonFormat(pattern = "yyyy-MM-dd") @Nullable LocalDate endDate) {

}
