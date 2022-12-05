package com.iitrab.hrtool.client.api;

import com.iitrab.hrtool.employee.api.EmployeeAssert;
import org.assertj.core.api.AbstractAssert;
import org.eclipse.jdt.annotation.Nullable;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public final class ClientAssert extends AbstractAssert<ClientAssert, Client> {

    private ClientAssert(Client client) {
        super(client, ClientAssert.class);
    }

    public static ClientAssert assertThatClient(Client client) {
        return new ClientAssert(client);
    }

    public ClientAssert hasId(@Nullable Long id) {
        isNotNull();
        assertThat(actual.getId()).isEqualTo(id);
        return this;
    }

    public ClientAssert hasName(String name) {
        isNotNull();
        assertThat(actual.getName()).isEqualTo(name);
        return this;
    }

    public ClientAssert hasContactNumber(String contactNumber) {
        isNotNull();
        assertThat(actual.getContactNumber()).isEqualTo(contactNumber);
        return this;
    }

    public ClientAssert hasContactEmail(String contactEmail) {
        isNotNull();
        assertThat(actual.getContactEmail()).isEqualTo(contactEmail);
        return this;
    }

    public ClientAssert hasManagerThat(Consumer<EmployeeAssert> consumer) {
        isNotNull();
        consumer.accept(EmployeeAssert.assertThatEmployee(actual.getManager()));
        return this;
    }

}
