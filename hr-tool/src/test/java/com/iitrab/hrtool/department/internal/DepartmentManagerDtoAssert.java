package com.iitrab.hrtool.department.internal;

import org.assertj.core.api.AbstractAssert;
import org.eclipse.jdt.annotation.Nullable;

import static org.assertj.core.api.Assertions.assertThat;

public class DepartmentManagerDtoAssert extends AbstractAssert<DepartmentManagerDtoAssert, DepartmentManagerDto> {

    private DepartmentManagerDtoAssert(DepartmentManagerDto departmentManagerDto) {
        super(departmentManagerDto, DepartmentManagerDtoAssert.class);
    }

    public static DepartmentManagerDtoAssert assertThatManagerDtoDepartment(DepartmentManagerDto departmentManagerDto) {
        return new DepartmentManagerDtoAssert(departmentManagerDto);
    }

    public DepartmentManagerDtoAssert hasId(@Nullable Long id) {
        isNotNull();
        assertThat(actual.getId()).isEqualTo(id);
        return this;
    }

    public DepartmentManagerDtoAssert hasName(String name) {
        isNotNull();
        assertThat(actual.getName()).isEqualTo(name);
        return this;
    }

    public DepartmentManagerDtoAssert hasLastName(String lastName) {
        isNotNull();
        assertThat(actual.getLastName()).isEqualTo(lastName);
        return this;
    }

    public DepartmentManagerDtoAssert hasEmail(String email) {
        isNotNull();
        assertThat(actual.getEmail()).isEqualTo(email);
        return this;
    }

}
