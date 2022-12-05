package com.iitrab.hrtool.client.internal;

import com.iitrab.hrtool.client.api.Client;
import com.iitrab.hrtool.client.api.ClientNotFoundException;
import com.iitrab.hrtool.employee.api.Employee;
import com.iitrab.hrtool.employee.api.EmployeeNotFoundException;
import com.iitrab.hrtool.employee.api.EmployeeProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.iitrab.hrtool.SampleTestDataFactory.client;
import static com.iitrab.hrtool.SampleTestDataFactory.employee;
import static com.iitrab.hrtool.client.api.ClientAssert.assertThatClient;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @InjectMocks
    private ClientServiceImpl clientService;

    @Mock
    private ClientRepository clientRepositoryMock;
    @Mock
    private EmployeeProvider employeeProviderMock;

    @Captor
    private ArgumentCaptor<Client> clientCaptor;

    @Captor
    private ArgumentCaptor<Long> clientIdCaptor;

    @Test
    void shouldReturnAllClients_whenFindingAllClients() {
        Client client = client(employee());

        when(clientRepositoryMock.findAll()).thenReturn(List.of(client));

        List<Client> foundClients = clientService.findAllClients();

        assertThat(foundClients).hasSize(1)
                .contains(client);
    }

    @Test
    void shouldReturnMatchingClient_whenFindingClientById() {
        Employee employee = employee();
        Client client = client(employee);
        long clientId = 1L;
        when(clientRepositoryMock.findById(clientId)).thenReturn(Optional.of(client));

        Optional<Client> foundClient = clientService.getClient(clientId);

        assertThat(foundClient).contains(client);
    }

    @Test
    void shouldReturnEmptyOptional_whenFindingClientById_andDoesNotExist() {
        long clientId = 1L;

        when(clientRepositoryMock.findById(clientId)).thenReturn(Optional.empty());

        Optional<Client> foundClient = clientService.getClient(clientId);

        assertThat(foundClient).isEmpty();
    }

    @Test
    void shouldReturnNotFoundException_whenCreatingClient_withNoExistingManager() {
        long managerId = 1L;
        CreateClientRequest createClientRequest = new CreateClientRequest("Name", "888888888", "test@test.com", managerId);

        when(employeeProviderMock.getEmployee(managerId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clientService.create(createClientRequest)).isInstanceOf(
                EmployeeNotFoundException.class);
    }

    @Test
    void shouldPersistClient_whenCreatingClient() {
        Employee manager = employee();
        String name = "NameTest";
        String contactNumber = "888777333";
        String contactEmail = "email@test.com";
        long managerId = 1L;
        CreateClientRequest createClientRequest = new CreateClientRequest(name, contactNumber, contactEmail, managerId);

        when(employeeProviderMock.getEmployee(managerId)).thenReturn(Optional.of(manager));

        clientService.create(createClientRequest);

        verify(clientRepositoryMock).save(clientCaptor.capture());

        assertThatClient(clientCaptor.getValue()).hasName(name)
                .hasManagerThat(employeeAssert -> employeeAssert.isSameAs(manager));
    }

    @Test
    void shouldReturnCreatedClient_whenCreatingClient() {
        Employee manager = employee();
        String name = "NameTest";
        String contactNumber = "888777333";
        String contactEmail = "email@test.com";
        long managerId = 1L;
        CreateClientRequest createClientRequest = new CreateClientRequest(name, contactNumber, contactEmail, managerId);

        when(employeeProviderMock.getEmployee(managerId)).thenReturn(Optional.of(manager));
        when(clientRepositoryMock.save(any())).then(returnsFirstArg());

        Client client = clientService.create(createClientRequest);

        assertThatClient(client).hasName(name)
                .hasManagerThat(employeeAssert -> employeeAssert.isSameAs(manager));
    }

    @Test
    void shouldRemoveClient_whenDeletingDepartment() {
        Employee employee = employee();
        Client client = client(employee);
        long clientId = 1L;

        when(clientRepositoryMock.findById(clientId)).thenReturn(Optional.of(client));
        clientService.deleteClient(clientId);

        verify(clientRepositoryMock).deleteById(clientIdCaptor.capture());

        assertThat(clientIdCaptor.getValue().equals(clientId));
    }

    @Test
    void shouldRemoveClient_whenDeletingDepartment_andDoesNotExist() {
        long clientId = 1L;

        when(clientRepositoryMock.findById(clientId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clientService.deleteClient(clientId))
                .isInstanceOf(ClientNotFoundException.class);
    }

    @Test
    void shouldReturnMatchingClients_whenFindingClientsByName_withFragmentNameIgnoringCase() {
        Employee employee = employee();
        Client client1 = new Client("Boobi", null, null , employee);

        when(clientRepositoryMock.findByName("boo")).thenReturn(List.of(client1));

        List<Client> foundClient = clientService.getClientsByName("boo");

        assertThat(foundClient).contains(client1);
    }

    @Test
    void shouldReturnEmptyList_whenFindingClientsByName_withFragmentNameIgnoringCase_andNoneIsMatching() {
        when(clientRepositoryMock.findByName("Ob")).thenReturn(List.of());

        List<Client> foundClient = clientService.getClientsByName("Ob");

        assertThat(foundClient).isEmpty();
    }

    @Test
    void shouldReturnAllClients_whenFindingClientsByName_withEmptyName() {
        Employee employee = employee();
        Client client1 = new Client("Boobi", null, null , employee);
        Client client2 = new Client("Neaoby", null, null , employee);

        when(clientRepositoryMock.findByName("")).thenReturn(List.of(client1, client2));

        List<Client> foundClient = clientService.getClientsByName("");

        assertThat(foundClient).contains(client1, client2);
    }

    @Test
    void shouldReturnMatchingClients_whenFindingClientByManager() {
        Employee employee2 = employee();
        Client client1 = new Client("Boobi", null, null , employee2);
        Client client2 = new Client("Noe", null, null , employee2);

        when(clientRepositoryMock.findByManager(2L)).thenReturn(List.of(client1, client2));

        List<Client> foundClient = clientService.getClientsByManager(2L);

        assertThat(foundClient).contains(client1, client2);
    }

    @Test
    void shouldReturnEmptyList_whenFindingClientsByManager_andNoneIsMatching() {
        Long managerId = 3L;

        when(clientRepositoryMock.findByManager(managerId)).thenReturn(List.of());

        List<Client> foundClient = clientService.getClientsByManager(managerId);

        assertThat(foundClient).isEmpty();
    }

    @Test
    void shouldReturnMatchingClients_whenFindingClientsByManager_withNullManager() {
        Employee employee2 = employee();
        Client client1 = new Client("Boobi", null, null , employee2);
        Client client2 = new Client("Noe", null, null , employee2);

        when(clientRepositoryMock.findByManager(null)).thenReturn(List.of(client1, client2));

        List<Client> foundClient = clientService.getClientsByManager(null);

        assertThat(foundClient).contains(client1, client2);
    }

    @Test
    void shouldReturnMatchingClients_whenGettingClientsByCriteria_withEmptySearchingForm() {
        Employee employee1 = employee();
        Employee employee2 = employee();
        Client client1 = new Client("Boobi", null, null , employee1);
        Client client2 = new Client("Neaoby", null, null , employee2);

        SearchingForm searchingForm = new SearchingForm(null, null);

        when(clientRepositoryMock.findAllBySearchCriteria(searchingForm)).thenReturn(List.of(client1, client2));

        List<Client> foundClient = clientService.getClientsByCriteria(searchingForm);

        assertThat(foundClient).contains(client1, client2);
    }

    @Test
    void shouldReturnMatchingClients_whenGettingClientsByCriteria_withNameInSearchingForm() {
        Employee employee2 = employee();
        Client client2 = new Client("Neaoby", null, null , employee2);

        SearchingForm searchingForm = new SearchingForm("Neaoby", null);

        when(clientRepositoryMock.findAllBySearchCriteria(searchingForm)).thenReturn(List.of(client2));

        List<Client> foundClient = clientService.getClientsByCriteria(searchingForm);

        assertThat(foundClient).contains(client2);
    }

    @Test
    void shouldReturnMatchingClients_whenGettingClientsByCriteria_withManagerInSearchingForm() {
        Employee employee1 = employee();
        Client client1 = new Client("Boobi", null, null , employee1);

        SearchingForm searchingForm = new SearchingForm(null, 1L);

        when(clientRepositoryMock.findAllBySearchCriteria(searchingForm)).thenReturn(List.of(client1));

        List<Client> foundClient = clientService.getClientsByCriteria(searchingForm);

        assertThat(foundClient).contains(client1);
    }

    @Test
    void shouldReturnMatchingClients_whenGettingClientsByCriteria_withNameAndManagerInSearchingForm() {
        Employee employee1 = employee();
        Client client1 = new Client("Boobi", null, null , employee1);

        SearchingForm searchingForm = new SearchingForm("Boobi", 1L);

        when(clientRepositoryMock.findAllBySearchCriteria(searchingForm)).thenReturn(List.of(client1));

        List<Client> foundClient = clientService.getClientsByCriteria(searchingForm);

        assertThat(foundClient).contains(client1);
    }
}
