package com.iitrab.hrtool.recruitment.internal;

import org.eclipse.jdt.annotation.Nullable;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

record FinalizedRecruitmentDto(@NotNull @Positive Long employeeId,
                               @NotNull String employeeName,
                               @NotNull String employeeLastName,
                               @NotNull @Past LocalDate birthdate,
                               @NotNull String employeeEmail,
                               @NotNull @Positive Long contractId,
                               @NotNull @FutureOrPresent LocalDate contractStartDate,
                               @Nullable @Future LocalDate contractEndDate,
                               @NotNull @Positive Long departmentId,
                               @NotNull String departmentName,
                               @NotNull @Positive BigDecimal salary,
                               @NotNull String grade,
                               @NotNull String position) {
}
