package com.iitrab.hrtool.client.internal;

import org.assertj.core.api.AbstractAssert;
import org.eclipse.jdt.annotation.Nullable;

import static org.assertj.core.api.Assertions.assertThat;

public class ClientDtoAssert extends AbstractAssert<ClientDtoAssert, ClientDto> {

    private ClientDtoAssert(ClientDto clientDto) {
        super(clientDto, ClientDtoAssert.class);
    }

    public static ClientDtoAssert assertThatClientDto(ClientDto clientDto) {
        return new ClientDtoAssert(clientDto);
    }

    public ClientDtoAssert hasId(@Nullable Long id) {
        isNotNull();
        assertThat(actual.id()).isEqualTo(id);
        return this;
    }

    public ClientDtoAssert hasName(String name) {
        isNotNull();
        assertThat(actual.name()).isEqualTo(name);
        return this;
    }

    public ClientDtoAssert hasContactNumber(String contactNumber) {
        isNotNull();
        assertThat(actual.contactNumber()).isEqualTo(contactNumber);
        return this;
    }

    public ClientDtoAssert hasContactEmail(String contactEmail) {
        isNotNull();
        assertThat(actual.contactEmail()).isEqualTo(contactEmail);
        return this;
    }

    public ClientDtoAssert hasManager(ManagerDto manager) {
        isNotNull();
        assertThat(actual.manager()).isNotNull();
        assertThat(actual.manager()).isEqualTo(manager);
        return this;
    }
}
