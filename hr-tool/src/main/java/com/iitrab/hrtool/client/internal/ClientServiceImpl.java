package com.iitrab.hrtool.client.internal;

import com.iitrab.hrtool.client.api.Client;
import com.iitrab.hrtool.client.api.ClientNotFoundException;
import com.iitrab.hrtool.client.api.ClientProvider;
import com.iitrab.hrtool.employee.api.Employee;
import com.iitrab.hrtool.employee.api.EmployeeNotFoundException;
import com.iitrab.hrtool.employee.api.EmployeeProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
class ClientServiceImpl implements ClientProvider {

    private final ClientRepository clientRepository;
    private final EmployeeProvider employeeProvider;

    /**
     * Returns all clients.
     *
     * @return list of all clients
     */
    public List<Client> findAllClients() {
        return clientRepository.findAll();
    }

    /**
     * Returns client by id.
     *
     * @param clientId id of the client to search
     * @return client
     */
    @Override
    public Optional<Client> getClient(Long clientId) {
        return clientRepository.findById(clientId);
    }

    /**
     * Returns clients by name or name fragment name.
     *
     * @param clientName name of the client to search
     * @return list of all clients
     */
    List<Client> getClientsByName(String clientName) {
        return clientRepository.findByName(clientName);
    }

    /**
     * Returns clients by manager.
     *
     * @param managerId id of the client manager to search
     * @return list of all clients
     */
    List<Client> getClientsByManager(Long managerId) {
        return clientRepository.findByManager(managerId);
    }


    /**
     * Creates and persists the client, based on the provided creation data.
     *
     * @param createClientRequest data of the created client
     * @return created client
     */
    Client create(CreateClientRequest createClientRequest) {
        Long managerId = createClientRequest.managerId();
        Employee manager = employeeProvider.getEmployee(managerId)
                .orElseThrow(() -> new EmployeeNotFoundException(managerId));

        Client client = new Client(createClientRequest.name(), createClientRequest.contactNumber(), createClientRequest.contactEmail(), manager);
        log.info("Creating the client {}", client);

        return clientRepository.save(client);
    }


    /**
     * Deletes the client, based on the provided deletion data.
     *
     * @param clientId data of the deleted client
     */
    void deleteClient(Long clientId) {
        getClient(clientId).orElseThrow(() -> new ClientNotFoundException(clientId));
        clientRepository.deleteById(clientId);
    }

    /**
     * Returns clients by name or/and by manager.
     *
     * @param searchingForm data for clients search
     * @return list of all clients
     */
    List<Client> getClientsByCriteria(SearchingForm searchingForm) {
        return clientRepository.findAllBySearchCriteria(searchingForm);
    }
}
