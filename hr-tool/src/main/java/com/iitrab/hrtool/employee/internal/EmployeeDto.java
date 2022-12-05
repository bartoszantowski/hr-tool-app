package com.iitrab.hrtool.employee.internal;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.eclipse.jdt.annotation.Nullable;

import java.time.LocalDate;

record EmployeeDto(@Nullable Long id, String name, String lastName,
                   @JsonFormat(pattern = "yyyy-MM-dd") LocalDate birthdate,
                   String email) {

}
