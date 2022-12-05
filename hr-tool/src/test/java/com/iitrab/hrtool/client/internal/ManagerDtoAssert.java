package com.iitrab.hrtool.client.internal;

import org.assertj.core.api.AbstractAssert;
import org.eclipse.jdt.annotation.Nullable;

import static org.assertj.core.api.Assertions.assertThat;

public class ManagerDtoAssert extends AbstractAssert<ManagerDtoAssert, ManagerDto> {

    private ManagerDtoAssert(ManagerDto managerDto) {
        super(managerDto, ManagerDtoAssert.class);
    }

    public static  ManagerDtoAssert assertThatManagerDto(ManagerDto managerDto) {
        return new ManagerDtoAssert(managerDto);
    }

    public ManagerDtoAssert hasId(@Nullable Long id) {
        isNotNull();
        assertThat(actual.id()).isEqualTo(id);
        return this;
    }

    public ManagerDtoAssert hasName(String name) {
        isNotNull();
        assertThat(actual.name()).isEqualTo(name);
        return this;
    }

    public ManagerDtoAssert hasLastName(String lastName) {
        isNotNull();
        assertThat(actual.lastName()).isEqualTo(lastName);
        return this;
    }

    public ManagerDtoAssert hasEmail(String email) {
        isNotNull();
        assertThat(actual.email()).isEqualTo(email);
        return this;
    }
}
