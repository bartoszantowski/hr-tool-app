package com.iitrab.hrtool.client.internal;

import com.iitrab.hrtool.client.api.Client;
import com.iitrab.hrtool.employee.api.Employee;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class ClientMapper {

    BasicClientDto toDtoPrimary(Client client) {
        return new BasicClientDto(client.getId(),
                                    client.getName());
    }

    ClientDto toDto(Client client) {
        return new ClientDto(client.getId(),
                             client.getName(),
                             client.getContactNumber(),
                             client.getContactEmail(),
                             Optional.ofNullable(client.getManager())
                                .map(this::toDto)
                                .orElse(null));
    }

    private ManagerDto toDto(Employee manager) {
        return new ManagerDto(manager.getId(),
                              manager.getName(),
                              manager.getLastName(),
                              manager.getEmail());
    }
}
