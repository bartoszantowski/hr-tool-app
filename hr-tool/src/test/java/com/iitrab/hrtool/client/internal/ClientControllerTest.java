package com.iitrab.hrtool.client.internal;

import com.iitrab.hrtool.client.api.Client;
import com.iitrab.hrtool.client.api.ClientNotFoundException;
import com.iitrab.hrtool.employee.api.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static com.iitrab.hrtool.SampleTestDataFactory.client;
import static com.iitrab.hrtool.SampleTestDataFactory.employee;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ClientControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ClientServiceImpl clientServiceMock;

    @BeforeEach
    void setUp() {
        ClientController clientController = new ClientController(clientServiceMock, new ClientMapper());
        mockMvc = MockMvcBuilders.standaloneSetup(clientController)
                .build();
    }

    @Test
    void shouldReturnAllClients_whenFindingAllClients() throws Exception {
        Client client = client(employee());
        when(clientServiceMock.findAllClients()).thenReturn(List.of(client));

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/clients"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value(client.getName()))
                .andExpect(jsonPath("$[1]").doesNotExist());
    }

    @Test
    void shouldReturnEmptyList_whenFindingClients_andNoneIsPresent() throws Exception {
        when(clientServiceMock.findAllClients()).thenReturn(List.of());

        mockMvc.perform(get("/v1/clients"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").doesNotExist());
    }

    @Test
    void shouldReturnClient_whenGettingClientById() throws Exception {
        Employee employee = employee();
        Client client = client(employee);
        long clientId = 1L;
        when(clientServiceMock.getClient(clientId)).thenReturn(Optional.of(client));

        mockMvc.perform(get("/v1/clients/{clientId}", clientId))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(client.getName()));
    }

    @Test
    void shouldReturnNotFound_whenGettingClientById_thatDoesNotExist() throws Exception {
        long clientId = 1L;
        when(clientServiceMock.getClient(clientId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/v1/clients/{clientId}", clientId))
                .andDo(log())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnCreatedClient_whenCreatingClient() throws Exception {
        Employee manager = employee();
        String name = "NameTest";
        String contactNumber = "888777333";
        String contactEmail = "email@test.com";
        Client client = new Client(name, contactNumber, contactEmail, manager);
        long managerId = 1L;
        CreateClientRequest expectedRequest = new CreateClientRequest(name, contactNumber, contactEmail, managerId);

        when(clientServiceMock.create(expectedRequest)).thenReturn(client);

        String requestBody = """
                             {
                               "name": "%s",
                               "contactNumber": "%s",
                               "contactEmail": "%s",
                               "managerId": %s
                             }
                             """.formatted(name, contactNumber, contactEmail, managerId);

        mockMvc.perform(post("/v1/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(log())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(client.getName()));
    }

    @Test
    void shouldReturnBadRequest_whenCreatingClient_withWrongMail() throws Exception {
        String name = "NameTest";
        String contactNumber = "888777333";
        String contactEmail = "email@test.com.";
        long managerId = 1L;

        String requestBody = """
                             {
                               "name": "%s",
                               "contactNumber": "%s",
                               "contactEmail": "%s",
                               "managerId": %s
                             }
                             """.formatted(name, contactNumber, contactEmail, managerId);

        mockMvc.perform(post("/v1/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(log())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequest_whenCreatingClient_withWrongContactNumber() throws Exception {
        String name = "NameTest";
        String contactNumber = "888773333z7333";
        String contactEmail = "email@test.com";
        long managerId = 1L;

        String requestBody = """
                             {
                               "name": "%s",
                               "contactNumber": "%s",
                               "contactEmail": "%s",
                               "managerId": %s
                             }
                             """.formatted(name, contactNumber, contactEmail, managerId);

        mockMvc.perform(post("/v1/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(log())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequest_whenCreatingClient_withEmptyRequestBody() throws Exception {
        String requestBody = "{}";

        mockMvc.perform(post("/v1/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(log())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnNotFound_whenRemovingClientById_thatDoesNotExist() throws Exception {
        long clientId = 1L;

        doThrow(new ClientNotFoundException(clientId)).when(clientServiceMock).deleteClient(clientId);

        mockMvc.perform(delete("/v1/clients/{clientId}", clientId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldRemoveClient_whenRemovingClientById() throws Exception {
        Long clientId = 1L;

        mockMvc.perform(delete("/v1/clients/{clientId}", clientId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnBadRequest_whenRemovingClientById() throws Exception {
        String clientId = "xxx";

        mockMvc.perform(delete("/v1/clients/{clientId}", clientId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnMatchingClients_whenGettingClientByName_withFragmentNameIgnoringCase() throws Exception {
        Employee employee = employee();
        Client client1 = new Client("Boobi", null, null , employee);
        Client client2 = new Client("Neaoby", null, null , employee);

        when(clientServiceMock.getClientsByName("Ob")).thenReturn(List.of(client1, client2));

        mockMvc.perform(get("/v1/clients?name=Ob"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value(client1.getName()))
                .andExpect(jsonPath("$[1].name").value(client2.getName()))
                .andExpect(jsonPath("$[2]").doesNotExist());
    }

    @Test
    void shouldReturnEmptyList_whenGettingClientByName_andNoneIsPresent() throws Exception {
        when(clientServiceMock.getClientsByName("Ob")).thenReturn(List.of());

        mockMvc.perform(get("/v1/clients?name=Ob"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").doesNotExist());
    }

    @Test
    void shouldReturnMatchingClients_whenGettingClientByManager() throws Exception {
        Employee employee2 = employee();

        Client client1 = new Client("Boobi", null, null , employee2);
        Client client2 = new Client("Neaoby", null, null , employee2);

        when(clientServiceMock.getClientsByManager(2L)).thenReturn(List.of(client1, client2));

        mockMvc.perform(get("/v1/clients?managerId=2"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value(client1.getName()))
                .andExpect(jsonPath("$[1].name").value(client2.getName()))
                .andExpect(jsonPath("$[2]").doesNotExist());
    }

    @Test
    void shouldReturnMatchingClients_whenGettingClientByManager_andNoneIsMatching() throws Exception {
        when(clientServiceMock.getClientsByManager(2L)).thenReturn(List.of());

        mockMvc.perform(get("/v1/clients?managerId=2"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").doesNotExist());
    }

    @Test
    void shouldReturnMatchingClients_whenGettingClientByManager_withNullRequestBody() throws Exception {
        Employee employee2 = employee();
        Client client1 = new Client("Boobi", null, null , employee2);
        Client client2 = new Client("Neaoby", null, null , employee2);

        when(clientServiceMock.getClientsByManager(null)).thenReturn(List.of(client1, client2));

        mockMvc.perform(get("/v1/clients?managerId="))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value(client1.getName()))
                .andExpect(jsonPath("$[1].name").value(client2.getName()))
                .andExpect(jsonPath("$[2]").doesNotExist());
    }

    @Test
    void shouldReturnMatchingClients_whenGettingClientsByCriteria_withEmptyRequestBody() throws Exception {
        Employee employee1 = employee();
        Employee employee2 = employee();

        Client client1 = new Client("Boobi", null, null , employee1);
        Client client2 = new Client("Neaoby", null, null , employee2);

        SearchingForm searchingForm = new SearchingForm(null, null);

        when(clientServiceMock.getClientsByCriteria(searchingForm)).thenReturn(List.of(client1, client2));

        String requestBody = "{}";

        mockMvc.perform(post("/v1/clients/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value(client1.getName()))
                .andExpect(jsonPath("$[1].name").value(client2.getName()))
                .andExpect(jsonPath("$[2]").doesNotExist());
    }

    @Test
    void shouldReturnMatchingClients_whenGettingClientsByCriteria_withOnlyName() throws Exception {
        Employee employee1 = employee();
        Client client1 = new Client("Boobi", null, null , employee1);

        SearchingForm searchingForm = new SearchingForm("Boobi", null);

        when(clientServiceMock.getClientsByCriteria(searchingForm)).thenReturn(List.of(client1));

        String name = "Boobi";
        Long managerId = null;

        String requestBody = """
                             {
                               "name": "%s",
                               "managerId": %s
                             }
                             """.formatted(name, managerId);

        mockMvc.perform(post("/v1/clients/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value(client1.getName()))
                .andExpect(jsonPath("$[1]").doesNotExist());
    }

    @Test
    void shouldReturnMatchingClients_whenGettingClientsByCriteria_withOnlyManagerAndEmptyName() throws Exception {
        Employee employee2 = employee();

        Client client2 = new Client("Neaoby", null, null , employee2);

        SearchingForm searchingForm = new SearchingForm("", 2L);

        when(clientServiceMock.getClientsByCriteria(searchingForm)).thenReturn(List.of(client2));

        String name = "";
        Long managerId = 2L;

        String requestBody = """
                             {
                               "name": "%s",
                               "managerId": %s
                             }
                             """.formatted(name, managerId);

        mockMvc.perform(post("/v1/clients/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value(client2.getName()))
                .andExpect(jsonPath("$[1]").doesNotExist());
    }

    @Test
    void shouldReturnMatchingClients_whenGettingClientsByCriteria_withOnlyManagerAndNullName() throws Exception {
        Employee employee2 = employee();

        Client client2 = new Client("Neaoby", null, null , employee2);

        SearchingForm searchingForm = new SearchingForm(null, 2L);

        when(clientServiceMock.getClientsByCriteria(searchingForm)).thenReturn(List.of(client2));

        String name = null;
        Long managerId = 2L;

        String requestBody = """
                             {
                               "name": %s,
                               "managerId": %s
                             }
                             """.formatted(name, managerId);

        mockMvc.perform(post("/v1/clients/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value(client2.getName()))
                .andExpect(jsonPath("$[1]").doesNotExist());
    }

    @Test
    void shouldReturnMatchingClients_whenGettingClientsByCriteria_withNameAndManagerCorrect() throws Exception {
        Employee employee2 = employee();

        Client client2 = new Client("Neaoby", null, null , employee2);

        SearchingForm searchingForm = new SearchingForm("Neaoby", 2L);

        when(clientServiceMock.getClientsByCriteria(searchingForm)).thenReturn(List.of(client2));

        String name = "Neaoby";
        Long managerId = 2L;

        String requestBody = """
                             {
                               "name": "%s",
                               "managerId": %s
                             }
                             """.formatted(name, managerId);

        mockMvc.perform(post("/v1/clients/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value(client2.getName()))
                .andExpect(jsonPath("$[1]").doesNotExist());
    }
}
