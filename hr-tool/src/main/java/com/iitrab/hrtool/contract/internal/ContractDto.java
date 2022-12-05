package com.iitrab.hrtool.contract.internal;

import com.iitrab.hrtool.contract.api.Grade;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.eclipse.jdt.annotation.Nullable;

import java.math.BigDecimal;
import java.time.LocalDate;

record ContractDto(@Nullable Long id, @Nullable Long employeeId, @Nullable Long departmentId,
                   @JsonFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                   @JsonFormat(pattern = "yyyy-MM-dd") @Nullable LocalDate endDate, BigDecimal salary, Grade grade,
                   String position) {

}
