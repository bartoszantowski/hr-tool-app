package com.iitrab.hrtool.client.internal;

import com.iitrab.hrtool.IntegrationTestBase;
import com.iitrab.hrtool.client.api.Client;
import com.iitrab.hrtool.employee.api.Employee;
import com.iitrab.hrtool.client.api.ClientAssert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.iitrab.hrtool.SampleTestDataFactory.employee;
import static org.assertj.core.api.Assertions.assertThat;

class ClientRepositoryTest extends IntegrationTestBase {

    @Autowired
    private ClientRepository clientRepository;

    @Test
    void shouldReturnEmptyList_whenSearchingClientsByName_andThereAreNone() {
        List<Client> found = clientRepository.findByName("Boo");

        assertThat(found).isEmpty();
    }

    @Test
    void shouldReturnMatchingClient_whenSearchingClientsByName() {
        Employee manager = existingEmployee(employee());
        Client client1 = existingClient(new Client("Boobi", null, null , manager));
        Client client2 = existingClient(new Client("Neaoby", null, null , manager));

        List<Client> found = clientRepository.findByName("oB");

        assertThat(found).hasSize(2)
                .anySatisfy(cli -> ClientAssert.assertThatClient(cli).hasName(client1.getName()))
                .anySatisfy(cli -> ClientAssert.assertThatClient(cli).hasName(client2.getName()));
    }

    @Test
    void shouldReturnMatchingClient_whenSearchingClientsByName_withEmptySearchRequest() {
        Employee manager = existingEmployee(employee());
        Client client1 = existingClient(new Client("Boobi", null, null , manager));
        Client client2 = existingClient(new Client("Neaoby", null, null , manager));

        List<Client> found = clientRepository.findByName("");

        assertThat(found).hasSize(2)
                .anySatisfy(cli -> ClientAssert.assertThatClient(cli).hasName(client1.getName()))
                .anySatisfy(cli -> ClientAssert.assertThatClient(cli).hasName(client2.getName()));
    }

    @Test
    void shouldReturnEmptyList_whenSearchingClientsByManager_andThereAreNone() {
        List<Client> found = clientRepository.findByManager(1L);

        assertThat(found).isEmpty();
    }

    @Test
    void shouldReturnMatchingClients_whenSearchingClientsByManager() {
        Employee manager1 = existingEmployee(employee());
        Employee manager2 = existingEmployee(employee());
        Client client1 = existingClient(new Client("Boobi", null, null , manager2));
        Client client2 = existingClient(new Client("Noe", null, null , manager2));
        Client client3 = existingClient(new Client("Al", null, null , manager1));

        List<Client> found = clientRepository.findByManager(client1.getManager().getId());

        assertThat(found).hasSize(2)
                .anySatisfy(cli -> ClientAssert.assertThatClient(cli).hasManagerThat(employeeAssert -> employeeAssert.hasId(client1.getManager().getId())))
                .anySatisfy(cli -> ClientAssert.assertThatClient(cli).hasManagerThat(employeeAssert -> employeeAssert.hasId(client2.getManager().getId())));
    }

    @Test
    void shouldReturnMatchingClients_whenSearchingClientsByManager_withNullManager() {
        Employee manager = existingEmployee(employee());
        Client client1 = existingClient(new Client("Boobi", null, null , null));
        Client client2 = existingClient(new Client("Noe", null, null , manager));

        List<Client> found = clientRepository.findByManager(manager.getId());

        assertThat(found).hasSize(1)
                .anySatisfy(cli -> ClientAssert.assertThatClient(cli).hasManagerThat(employeeAssert -> employeeAssert.hasId(client2.getManager().getId())));
    }

    @Test
    void shouldReturnMatchingClients_whenSearchingClientsByManager_withAllNullManager() {
        Employee manager = existingEmployee(employee());
        Client client1 = existingClient(new Client("Boobi", null, null , null));
        Client client2 = existingClient(new Client("Noe", null, null , null));

        List<Client> found = clientRepository.findByManager(manager.getId());

        assertThat(found).isEmpty();
    }

    @Test
    void shouldReturnAllClients_whenSearchingClientsByCriteria_withNullInSearchingForm() {
        Employee manager1 = existingEmployee(employee());
        Employee manager2 = existingEmployee(employee());
        Client client1 = existingClient(new Client("Boobi", null, null , manager1));
        Client client2 = existingClient(new Client("Noe", null, null , manager2));

        SearchingForm searchingForm = new SearchingForm(null, null);

        List<Client> found = clientRepository.findAllBySearchCriteria(searchingForm);

        assertThat(found).hasSize(2)
                .anySatisfy(cli -> ClientAssert.assertThatClient(cli).hasName(client1.getName()))
                .anySatisfy(cli -> ClientAssert.assertThatClient(cli).hasName(client2.getName()));
    }
    @Test
    void shouldReturnMatchingClients_whenSearchingClientsByCriteria_withOnlyNameInSearchingForm() {
        Employee manager1 = existingEmployee(employee());
        Employee manager2 = existingEmployee(employee());
        Client client1 = existingClient(new Client("Boobi", null, null , manager1));
        Client client2 = existingClient(new Client("Noe", null, null , manager2));

        SearchingForm searchingForm = new SearchingForm("Noe", null);

        List<Client> found = clientRepository.findAllBySearchCriteria(searchingForm);

        assertThat(found).hasSize(1)
                .anySatisfy(cli -> ClientAssert.assertThatClient(cli).hasName(client2.getName()));
    }

    @Test
    void shouldReturnMatchingClients_whenSearchingClientsByCriteria_withOnlyManagerInSearchingForm() {
        Employee manager1 = existingEmployee(employee());
        Employee manager2 = existingEmployee(employee());
        Client client1 = existingClient(new Client("Boobi", null, null , manager1));
        Client client2 = existingClient(new Client("Noe", null, null , manager2));

        SearchingForm searchingForm = new SearchingForm(null, manager2.getId());

        List<Client> found = clientRepository.findAllBySearchCriteria(searchingForm);

        assertThat(found).hasSize(1)
                .anySatisfy(cli -> ClientAssert.assertThatClient(cli).hasName(client2.getName()));
    }

    @Test
    void shouldReturnmatchingClients_whenSearchingClientsByCriteria_withNameAndManagerInSearchingForm() {
        Employee manager1 = existingEmployee(employee());
        Employee manager2 = existingEmployee(employee());
        Client client1 = existingClient(new Client("Boobi", null, null , manager1));
        Client client2 = existingClient(new Client("Noe", null, null , manager2));

        SearchingForm searchingForm = new SearchingForm("Noe", manager2.getId());

        List<Client> found = clientRepository.findAllBySearchCriteria(searchingForm);

        assertThat(found).hasSize(1)
                .anySatisfy(cli -> ClientAssert.assertThatClient(cli).hasName(client2.getName()));
    }

    @Test
    void shouldReturnmatchigClients_whenSearchingClientsByCriteria_withBadNameAndManagerInSearchingForm_andNoneIsMatching() {
        Employee manager1 = existingEmployee(employee());
        Employee manager2 = existingEmployee(employee());
        Client client1 = existingClient(new Client("Boobi", null, null , manager1));
        Client client2 = existingClient(new Client("Noe", null, null , manager2));

        SearchingForm searchingForm = new SearchingForm("Boob", 1L);

        List<Client> found = clientRepository.findAllBySearchCriteria(searchingForm);

        assertThat(found).isEmpty();
    }

    @Test
    void shouldReturnMatchingClients_whenSearchingClientsByCriteria_withCorrectNameAndBadManagerInSearchingForm_andNoneIsMatching() {
        Employee manager1 = existingEmployee(employee());
        Employee manager2 = existingEmployee(employee());
        Client client1 = existingClient(new Client("Boobi", null, null , manager1));
        Client client2 = existingClient(new Client("Noe", null, null , manager2));

        SearchingForm searchingForm = new SearchingForm("Boobi", 2L);

        List<Client> found = clientRepository.findAllBySearchCriteria(searchingForm);

        assertThat(found).isEmpty();
    }

    @Test
    void shouldReturnMatchingClients_whenSearchingClientsByCriteria_withOnlyBadManager_andNoneIsMatching() {
        Employee manager1 = existingEmployee(employee());
        Employee manager2 = existingEmployee(employee());
        Client client1 = existingClient(new Client("Boobi", null, null , manager1));
        Client client2 = existingClient(new Client("Noe", null, null , manager2));

        SearchingForm searchingForm = new SearchingForm(null, 552L);

        List<Client> found = clientRepository.findAllBySearchCriteria(searchingForm);

        assertThat(found).isEmpty();
    }
}
