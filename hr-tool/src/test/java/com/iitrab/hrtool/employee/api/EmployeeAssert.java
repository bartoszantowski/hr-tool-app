package com.iitrab.hrtool.employee.api;

import org.assertj.core.api.AbstractAssert;
import org.eclipse.jdt.annotation.Nullable;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public final class EmployeeAssert extends AbstractAssert<EmployeeAssert, Employee> {

    private EmployeeAssert(Employee employee) {
        super(employee, EmployeeAssert.class);
    }

    public static EmployeeAssert assertThatEmployee(Employee employee) {
        return new EmployeeAssert(employee);
    }

    public EmployeeAssert hasId(@Nullable Long id) {
        isNotNull();
        assertThat(actual.getId()).isEqualTo(id);
        return this;
    }

    public EmployeeAssert hasName(String name) {
        isNotNull();
        assertThat(actual.getName()).isEqualTo(name);
        return this;
    }

    public EmployeeAssert hasLastName(String lastName) {
        isNotNull();
        assertThat(actual.getLastName()).isEqualTo(lastName);
        return this;
    }

    public EmployeeAssert hasEmail(String email) {
        isNotNull();
        assertThat(actual.getEmail()).isEqualTo(email);
        return this;
    }

    public EmployeeAssert wasBornOn(LocalDate date) {
        isNotNull();
        assertThat(actual.getBirthdate()).isEqualTo(date);
        return this;
    }

}
