package com.iitrab.hrtool.client.internal;

import org.assertj.core.api.AbstractAssert;
import org.eclipse.jdt.annotation.Nullable;

import static org.assertj.core.api.Assertions.assertThat;
public final class BasicClientDtoAssert extends AbstractAssert<BasicClientDtoAssert, BasicClientDto> {

    private BasicClientDtoAssert(BasicClientDto basicClientDto) {
        super(basicClientDto, BasicClientDtoAssert.class);
    }

    public static BasicClientDtoAssert assertThatClientDtoPrimary(BasicClientDto basicClientDto) {
        return new BasicClientDtoAssert(basicClientDto);
    }

    public BasicClientDtoAssert hasId(@Nullable Long id) {
        isNotNull();
        assertThat(actual.id()).isEqualTo(id);
        return this;
    }

    public BasicClientDtoAssert hasName(@Nullable String name) {
        isNotNull();
        assertThat(actual.name()).isEqualTo(name);
        return this;
    }
}
