package com.iitrab.hrtool.client.internal;

import com.iitrab.hrtool.IntegrationTest;
import com.iitrab.hrtool.IntegrationTestBase;
import com.iitrab.hrtool.client.api.Client;
import com.iitrab.hrtool.employee.api.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.iitrab.hrtool.SampleTestDataFactory.*;
import static com.iitrab.hrtool.client.api.ClientAssert.assertThatClient;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@Transactional
class ClientApiIntegrationTest extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnAllClients_whenGettingAllClients() throws Exception {
        Employee clientManager = existingEmployee(employee());
        Client client1 = existingClient(client(clientManager));
        Client client2 = existingClient(client(clientManager));

        mockMvc.perform(get("/v1/clients").contentType(MediaType.APPLICATION_JSON))
               .andDo(log())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$[0].id").value(client1.getId()))
               .andExpect(jsonPath("$[0].name").value(client1.getName()))
               .andExpect(jsonPath("$[1].id").value(client2.getId()))
               .andExpect(jsonPath("$[1].name").value(client2.getName()))
               .andExpect(jsonPath("$[2]").doesNotExist());
    }

    @Test
    void shouldReturnMatchingClients_whenGettingClientsByName() throws Exception {
        Employee clientManager = existingEmployee(employee());
        Client client1 = existingClient(client(clientManager));
        existingClient(client(clientManager));

        mockMvc.perform(get("/v1/clients").param("name", client1.getName())
                                          .contentType(MediaType.APPLICATION_JSON))
               .andDo(log())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$[0].id").value(client1.getId()))
               .andExpect(jsonPath("$[0].name").value(client1.getName()))
               .andExpect(jsonPath("$[1]").doesNotExist());
    }

    @Test
    void shouldReturnMatchingClients_whenGettingClientsByManager() throws Exception {
        Employee clientManager1 = existingEmployee(employee());
        Employee clientManager2 = existingEmployee(employee());
        Client client1 = existingClient(client(clientManager1));
        existingClient(client(clientManager2));

        mockMvc.perform(get("/v1/clients").param("managerId", String.valueOf(clientManager1.getId()))
                                          .contentType(MediaType.APPLICATION_JSON))
               .andDo(log())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$[0].id").value(client1.getId()))
               .andExpect(jsonPath("$[0].name").value(client1.getName()))
               .andExpect(jsonPath("$[1]").doesNotExist());
    }

    @Test
    void shouldReturnMatchingClients_whenGettingClientsBySearchCriteria() throws Exception {
        Employee clientManager1 = existingEmployee(employee());
        Employee clientManager2 = existingEmployee(employee());
        Client client1 = existingClient(client(clientManager1));
        existingClient(client(clientManager2));
        String searchRequest = """
                               {
                                 "name": "%s",
                                 "managerId": %s
                               }
                               """.formatted(client1.getName(), clientManager1.getId());

        mockMvc.perform(post("/v1/clients/search")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(searchRequest))
               .andDo(log())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$[0].id").value(client1.getId()))
               .andExpect(jsonPath("$[0].name").value(client1.getName()))
               .andExpect(jsonPath("$[1]").doesNotExist());
    }

    @Test
    void shouldReturnClientDetails_whenGettingClientById() throws Exception {
        Employee clientManager1 = existingEmployee(employee());
        Employee clientManager2 = existingEmployee(employee());
        Client client1 = existingClient(client(clientManager1));
        existingClient(client(clientManager2));

        mockMvc.perform(get("/v1/clients/{clientId}", client1.getId())
                                .contentType(MediaType.APPLICATION_JSON))
               .andDo(log())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(client1.getId()))
               .andExpect(jsonPath("$.name").value(client1.getName()))
               .andExpect(jsonPath("$.contactNumber").value(client1.getContactNumber()))
               .andExpect(jsonPath("$.contactEmail").value(client1.getContactEmail()))
               .andExpect(jsonPath("$.manager.id").value(clientManager1.getId()))
               .andExpect(jsonPath("$.manager.name").value(clientManager1.getName()))
               .andExpect(jsonPath("$.manager.lastName").value(clientManager1.getLastName()))
               .andExpect(jsonPath("$.manager.email").value(clientManager1.getEmail()));
    }

    @Test
    void shouldReturnNotFound_whenGettingClientById_thatDoesNotExist() throws Exception {
        mockMvc.perform(get("/v1/clients/{clientId}", Long.MAX_VALUE)
                                .contentType(MediaType.APPLICATION_JSON))
               .andDo(log())
               .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnClientDetails_whenCreatingClient() throws Exception {
        Employee clientManager1 = existingEmployee(employee());
        String creationRequest = """
                                 {
                                   "name": "%s",
                                   "contactNumber": "%s",
                                   "contactEmail": "%s",
                                   "managerId": %s
                                 }
                                 """.formatted(CLIENT_NAME,
                                               CLIENT_CONTACT_NUMBER,
                                               CLIENT_CONTACT_EMAIL,
                                               clientManager1.getId());

        mockMvc.perform(post("/v1/clients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(creationRequest))
               .andDo(log())
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id").isNotEmpty())
               .andExpect(jsonPath("$.name").value(CLIENT_NAME))
               .andExpect(jsonPath("$.contactNumber").value(CLIENT_CONTACT_NUMBER))
               .andExpect(jsonPath("$.contactEmail").value(CLIENT_CONTACT_EMAIL))
               .andExpect(jsonPath("$.manager.id").value(clientManager1.getId()))
               .andExpect(jsonPath("$.manager.name").value(clientManager1.getName()))
               .andExpect(jsonPath("$.manager.lastName").value(clientManager1.getLastName()))
               .andExpect(jsonPath("$.manager.email").value(clientManager1.getEmail()));
    }

    @Test
    void shouldPersistClient_whenCreatingClient() throws Exception {
        Employee clientManager1 = existingEmployee(employee());
        String creationRequest = """
                                 {
                                   "name": "%s",
                                   "contactNumber": "%s",
                                   "contactEmail": "%s",
                                   "managerId": %s
                                 }
                                 """.formatted(CLIENT_NAME,
                                               CLIENT_CONTACT_NUMBER,
                                               CLIENT_CONTACT_EMAIL,
                                               clientManager1.getId());

        mockMvc.perform(post("/v1/clients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(creationRequest))
               .andDo(log())
               .andExpect(status().isCreated());

        List<Client> allClients = getAllClients();
        assertThat(allClients).hasSize(1)
                              .anySatisfy(client -> assertThatClient(client).hasName(CLIENT_NAME)
                                                                            .hasContactEmail(CLIENT_CONTACT_EMAIL)
                                                                            .hasContactNumber(CLIENT_CONTACT_NUMBER)
                                                                            .hasManagerThat(manager -> manager.hasId(
                                                                                    clientManager1.getId())));
    }

    @Test
    void shouldRemoveClientFromRepository_whenDeletingClient() throws Exception {
        Employee clientManager1 = existingEmployee(employee());
        Client client1 = existingClient(new Client(CLIENT_NAME,
                                                   CLIENT_CONTACT_NUMBER,
                                                   CLIENT_CONTACT_EMAIL,
                                                   clientManager1));

        mockMvc.perform(delete("/v1/clients/{clientId}", client1.getId())
                                .contentType(MediaType.APPLICATION_JSON))
               .andDo(log())
               .andExpect(status().isNoContent());

        List<Client> allClients = getAllClients();
        assertThat(allClients).isEmpty();
    }

}
