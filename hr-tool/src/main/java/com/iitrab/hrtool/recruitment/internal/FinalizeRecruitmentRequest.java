package com.iitrab.hrtool.recruitment.internal;

import com.iitrab.hrtool.contract.api.Grade;
import org.eclipse.jdt.annotation.Nullable;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

record FinalizeRecruitmentRequest(@NotNull @Positive Long hrEmployeeId,
                                  @NotNull @Positive Long candidateId,
                                  @NotNull @FutureOrPresent LocalDate contractStartDate,
                                  @Nullable @Future LocalDate contractEndDate,
                                  @NotNull @Positive Long departmentId,
                                  @NotNull @Positive BigDecimal salary,
                                  @NotNull Grade grade,
                                  @NotNull String position) {
}
