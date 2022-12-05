package com.iitrab.hrtool.employee.internal;

import org.assertj.core.api.AbstractAssert;
import org.eclipse.jdt.annotation.Nullable;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public final class EmployeeDtoAssert extends AbstractAssert<EmployeeDtoAssert, EmployeeDto> {

    private EmployeeDtoAssert(EmployeeDto employeeDto) {
        super(employeeDto, EmployeeDtoAssert.class);
    }

    public static EmployeeDtoAssert assertThatEmployeeDto(EmployeeDto employeeDto) {
        return new EmployeeDtoAssert(employeeDto);
    }

    public EmployeeDtoAssert hasId(@Nullable Long id) {
        isNotNull();
        assertThat(actual.id()).isEqualTo(id);
        return this;
    }

    public EmployeeDtoAssert hasName(String name) {
        isNotNull();
        assertThat(actual.name()).isEqualTo(name);
        return this;
    }

    public EmployeeDtoAssert hasLastName(String lastName) {
        isNotNull();
        assertThat(actual.lastName()).isEqualTo(lastName);
        return this;
    }

    public EmployeeDtoAssert wasBornOn(LocalDate birthdate) {
        isNotNull();
        assertThat(actual.birthdate()).isEqualTo(birthdate);
        return this;
    }

    public EmployeeDtoAssert hasEmail(String email) {
        isNotNull();
        assertThat(actual.email()).isEqualTo(email);
        return this;
    }

}
