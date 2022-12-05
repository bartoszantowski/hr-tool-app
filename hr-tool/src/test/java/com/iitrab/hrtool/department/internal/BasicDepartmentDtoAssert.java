package com.iitrab.hrtool.department.internal;

import org.assertj.core.api.AbstractAssert;
import org.eclipse.jdt.annotation.Nullable;

import static org.assertj.core.api.Assertions.assertThat;

public class BasicDepartmentDtoAssert extends AbstractAssert<BasicDepartmentDtoAssert, BasicDepartmentDto> {

    private BasicDepartmentDtoAssert(BasicDepartmentDto basicDepartmentDto) {
        super(basicDepartmentDto, BasicDepartmentDtoAssert.class);
    }

    public static BasicDepartmentDtoAssert assertThatDepartmentDtoPrimary(BasicDepartmentDto basicDepartmentDto) {
        return new BasicDepartmentDtoAssert(basicDepartmentDto);
    }

    public BasicDepartmentDtoAssert hasId(@Nullable Long id) {
        isNotNull();
        assertThat(actual.getId()).isEqualTo(id);
        return this;
    }

    public BasicDepartmentDtoAssert hasName(@Nullable String name) {
        isNotNull();
        assertThat(actual.getName()).isEqualTo(name);
        return this;
    }
}
