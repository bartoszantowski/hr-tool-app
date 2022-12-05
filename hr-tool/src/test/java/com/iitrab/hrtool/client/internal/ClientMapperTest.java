package com.iitrab.hrtool.client.internal;

import com.iitrab.hrtool.client.api.Client;
import com.iitrab.hrtool.employee.api.Employee;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.iitrab.hrtool.SampleTestDataFactory.client;
import static com.iitrab.hrtool.SampleTestDataFactory.employee;
import static com.iitrab.hrtool.client.internal.BasicClientDtoAssert.assertThatClientDtoPrimary;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ClientMapperTest {

    @InjectMocks
    private ClientMapper clientMapper;

    @Test
    void shouldReturnClientDtoPrimary_whenMappingClientDtoPrimary() {
        Client client = client(employee());

        BasicClientDto basicClientDto = clientMapper.toDtoPrimary(client);

        assertThatClientDtoPrimary(basicClientDto).hasName(client.getName());
    }

    @Test
    void shouldReturnClientDto_whenMappingClientDto() {
        Employee employee = employee();
        Client client = client(employee);

        ClientDto clientDto = clientMapper.toDto(client);

        ClientDto expected = new ClientDto(null,
                                            client.getName(),
                                            client.getContactNumber(),
                                            client.getContactEmail(),
                                            new ManagerDto(employee.getId(),
                                                           employee.getName(),
                                                           employee.getLastName(),
                                                           employee.getEmail()));

        assertThat(clientDto).isEqualTo(expected);
    }

    @Test
    void shouldReturnClientDto_whenMappingClientDto_andManagerIsNull() {
        Client client = client(null);

        ClientDto clientDto = clientMapper.toDto(client);

        ClientDto expected = new ClientDto(null,
                client.getName(),
                client.getContactNumber(),
                client.getContactEmail(),
                null);

        assertThat(clientDto).isEqualTo(expected);
    }
}
