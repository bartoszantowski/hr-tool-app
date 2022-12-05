package com.iitrab.hrtool.department.internal;

import com.iitrab.hrtool.department.api.Department;
import com.iitrab.hrtool.department.api.DepartmentNotFoundException;
import com.iitrab.hrtool.employee.api.Employee;
import com.iitrab.hrtool.employee.api.EmployeeNotFoundException;
import com.iitrab.hrtool.SampleTestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class DepartmentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DepartmentServiceImpl departmentServiceMock;

    @BeforeEach
    void setUp() {
        DepartmentController departmentController = new DepartmentController(departmentServiceMock, new DepartmentMapper());
        mockMvc = MockMvcBuilders.standaloneSetup(departmentController)
                .build();
    }

    @Test
    void shouldReturnAllDepartments_whenFindingAllDepartments() throws Exception {
        Department department = SampleTestDataFactory.department(SampleTestDataFactory.employee());

        when(departmentServiceMock.getAllDepartments()).thenReturn(List.of(department));

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/departments"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value(department.getName()))
                .andExpect(jsonPath("$[1]").doesNotExist());
    }

    @Test
    void shouldReturnEmptyList_whenFindingAllDepartments_andNoneIsPresent() throws Exception {
        when(departmentServiceMock.getAllDepartments()).thenReturn(List.of());

        mockMvc.perform(get("/v1/departments"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").doesNotExist());
    }

    @Test
    void shouldReturnDepartment_whenGettingDepartmentById() throws Exception {
        Employee employee = SampleTestDataFactory.employee();
        Department department = SampleTestDataFactory.department(employee);
        long departmentId = 1L;

        when(departmentServiceMock.getDepartment(departmentId)).thenReturn(Optional.of(department));

        mockMvc.perform(get("/v1/departments/{departmentId}", departmentId))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(department.getName()));
    }

    @Test
    void shouldReturnNotFound_whenGettingDepartmentById_thatDoesNotExist() throws Exception {
        long departmentId = 1L;

        when(departmentServiceMock.getDepartment(departmentId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/v1/departments/{departmentId}", departmentId))
                .andDo(log())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnCreatedDepartment_whenCreatingDepartment() throws Exception {
        Employee manager = SampleTestDataFactory.employee();
        String name = "NameTest";

        Department department = new Department(name, manager);
        long managerId = 1L;
        CreateDepartmentRequest expectedRequest = new CreateDepartmentRequest(name, managerId);

        when(departmentServiceMock.create(expectedRequest)).thenReturn(department);

        String requestBody = """
                             {
                               "name": "%s",
                               "managerId": %s
                             }
                             """.formatted(name, managerId);

        mockMvc.perform(post("/v1/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(log())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(department.getName()));
    }

    @Test
    void shouldReturnBadRequest_whenCreatingDepartment_withEmptyRequestBody() throws Exception {
        String requestBody = "{}";

        mockMvc.perform(post("/v1/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(log())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnDepartment_whenUpdatingManagerDepartment() throws Exception {
        Employee employee2 = SampleTestDataFactory.employee();
        long employee2Id = 2L;
        Department department = SampleTestDataFactory.department(employee2);
        long departmentId = 1L;

        when(departmentServiceMock.updateDepartmentManager(departmentId, employee2Id)).thenReturn(department);

        mockMvc.perform(patch("/v1/departments/{departmentId}?managerId=2", departmentId))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(department.getName()))
                .andExpect(jsonPath("$.manager.name").value(employee2.getName()));
    }

    @Test
    void shouldReturnNotFound_whenUpdatingManagerDepartment_andDepartmentDoesNotExist() throws Exception {
        long employee2Id = 2L;
        long departmentId = 1L;

        when(departmentServiceMock.updateDepartmentManager(departmentId, employee2Id)).thenThrow(new DepartmentNotFoundException(departmentId));

        mockMvc.perform(patch("/v1/departments/{departmentId}?managerId=2", departmentId))
                .andDo(log())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnNotFound_whenUpdatingManagerDepartment_andManagerDoesNotExist() throws Exception {
        long employee2Id = 2L;
        long departmentId = 1L;

        when(departmentServiceMock.updateDepartmentManager(departmentId, employee2Id)).thenThrow(new EmployeeNotFoundException(employee2Id));

        mockMvc.perform(patch("/v1/departments/{departmentId}?managerId=2", departmentId))
                .andDo(log())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnNotFound_whenRemovingDepartmentById_thatDoesNotExist() throws Exception {
        long departmentId = 5L;

        Mockito.doThrow(new DepartmentNotFoundException(departmentId)).when(departmentServiceMock).deleteDepartment(departmentId);

        mockMvc.perform(delete("/v1/departments/{departmentId}", departmentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldRemoveDepartment_whenRemovingDepartmentById() throws Exception {
        Long departmentId = 1L;

        mockMvc.perform(delete("/v1/departments/{departmentId}", departmentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnBadRequest_whenRemovingDepartmentById() throws Exception {
        String departmentId = "sss";

        mockMvc.perform(delete("/v1/departments/{departmentId}", departmentId).contentType(MediaType.APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnMatchingDepartments_whenGettingDepartmentByName_withFragmentNameIgnoringCase() throws Exception {
        Employee employee = SampleTestDataFactory.employee();
        Department department1 = new Department("Boobi", employee);
        Department department2 = new Department("Neaoby", employee);

        when(departmentServiceMock.getDepartmentsByName("Ob")).thenReturn(List.of(department1, department2));

        mockMvc.perform(get("/v1/departments?name=Ob"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value(department1.getName()))
                .andExpect(jsonPath("$[1].name").value(department2.getName()))
                .andExpect(jsonPath("$[2]").doesNotExist());
    }

    @Test
    void shouldReturnEmptyList_whenGettingDepartmentByName_andNoneIsPresent() throws Exception {
        when(departmentServiceMock.getDepartmentsByName("Ob")).thenReturn(List.of());

        mockMvc.perform(get("/v1/departments?name=Ob"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").doesNotExist());
    }

    @Test
    void shouldReturnMatchingDepartments_whenGettingDepartmentByManager() throws Exception {
        Employee employee = SampleTestDataFactory.employee();
        Department department1 = new Department("Boobi", employee);
        Department department2 = new Department("Neaoby", employee);

        when(departmentServiceMock.getDepartmentsByManager(2L)).thenReturn(List.of(department1, department2));

        mockMvc.perform(get("/v1/departments?managerId=2"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value(department1.getName()))
                .andExpect(jsonPath("$[1].name").value(department2.getName()))
                .andExpect(jsonPath("$[2]").doesNotExist());
    }

    @Test
    void shouldReturnMatchingDepartments_whenGettingDepartmentByManager_andNoneIsMatching() throws Exception {
        when(departmentServiceMock.getDepartmentsByManager(2L)).thenReturn(List.of());

        mockMvc.perform(get("/v1/departments?managerId=2"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").doesNotExist());
    }

    @Test
    void shouldReturnMatchingDepartments_whenGettingDepartmentByManager_withNullRequestBody() throws Exception {
        Employee employee = SampleTestDataFactory.employee();
        Department department1 = new Department("Boobi", employee);
        Department department2 = new Department("Neaoby", employee);

        when(departmentServiceMock.getDepartmentsByManager(null)).thenReturn(List.of(department1, department2));

        mockMvc.perform(get("/v1/departments?managerId="))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value(department1.getName()))
                .andExpect(jsonPath("$[1].name").value(department2.getName()))
                .andExpect(jsonPath("$[2]").doesNotExist());
    }
}
