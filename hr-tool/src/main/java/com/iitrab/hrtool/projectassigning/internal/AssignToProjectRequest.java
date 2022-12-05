package com.iitrab.hrtool.projectassigning.internal;

import org.eclipse.jdt.annotation.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

record AssignToProjectRequest(@NotNull @Positive Long employeeId, @NotBlank String role, @NotNull LocalDate startDate,
                              @Nullable LocalDate endDate) {

}
