package com.iitrab.hrtool.department.internal;

import org.assertj.core.api.AbstractAssert;
import org.eclipse.jdt.annotation.Nullable;

import static org.assertj.core.api.Assertions.assertThat;

public class DepartmentDtoAssert extends AbstractAssert<DepartmentDtoAssert, DepartmentDto> {

    private DepartmentDtoAssert(DepartmentDto departmentDto) {
        super(departmentDto, DepartmentDtoAssert.class);
    }

    public static DepartmentDtoAssert assertThatDepartmentDto(DepartmentDto departmentDto) {
        return new DepartmentDtoAssert(departmentDto);
    }

    public DepartmentDtoAssert hasId(@Nullable Long id) {
        isNotNull();
        assertThat(actual.getId()).isEqualTo(id);
        return this;
    }

    public DepartmentDtoAssert hasName(String name) {
        isNotNull();
        assertThat(actual.getName()).isEqualTo(name);
        return this;
    }

    public DepartmentDtoAssert hasManager(DepartmentManagerDto manager) {
        isNotNull();
        assertThat(actual.getManager()).isNotNull();
        assertThat(actual.getManager()).isEqualTo(manager);
        return this;
    }
}
