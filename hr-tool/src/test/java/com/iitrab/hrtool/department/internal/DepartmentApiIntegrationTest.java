package com.iitrab.hrtool.department.internal;

import com.iitrab.hrtool.IntegrationTest;
import com.iitrab.hrtool.IntegrationTestBase;
import com.iitrab.hrtool.department.api.Department;
import com.iitrab.hrtool.employee.api.Employee;
import com.iitrab.hrtool.SampleTestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.iitrab.hrtool.department.api.DepartmentAssert.assertThatDepartment;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@Transactional
class DepartmentApiIntegrationTest extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnAllDepartments_whenGettingAllDepartments() throws Exception {
        Employee departmentManager = existingEmployee(SampleTestDataFactory.employee());
        Department department1 = existingDepartment(SampleTestDataFactory.department(departmentManager));
        Department department2 = existingDepartment(SampleTestDataFactory.department(departmentManager));

        mockMvc.perform(get("/v1/departments").contentType(MediaType.APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(department1.getId()))
                .andExpect(jsonPath("$[0].name").value(department1.getName()))
                .andExpect(jsonPath("$[1].id").value(department2.getId()))
                .andExpect(jsonPath("$[1].name").value(department2.getName()))
                .andExpect(jsonPath("$[2]").doesNotExist());
    }

    @Test
    void shouldReturnMatchingDepartments_whenGettingDepartmentsByName() throws Exception {
        Employee departmentManager = existingEmployee(SampleTestDataFactory.employee());
        Department department1 = existingDepartment(SampleTestDataFactory.department(departmentManager));
        existingDepartment(SampleTestDataFactory.department(departmentManager));

        mockMvc.perform(get("/v1/departments").param("name", department1.getName())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(department1.getId()))
                .andExpect(jsonPath("$[0].name").value(department1.getName()))
                .andExpect(jsonPath("$[1]").doesNotExist());
    }

    @Test
    void shouldReturnMatchingDepartments_whenGettingDepartmentsByManager() throws Exception {
        Employee departmentManager1 = existingEmployee(SampleTestDataFactory.employee());
        Employee departmentManager2 = existingEmployee(SampleTestDataFactory.employee());
        Department department1 = existingDepartment(SampleTestDataFactory.department(departmentManager1));
        existingDepartment(SampleTestDataFactory.department(departmentManager2));

        mockMvc.perform(get("/v1/departments").param("managerId", String.valueOf(departmentManager1.getId()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(department1.getId()))
                .andExpect(jsonPath("$[0].name").value(department1.getName()))
                .andExpect(jsonPath("$[1]").doesNotExist());
    }

    @Test
    void shouldReturnDepartmentDetails_whenUpdatingDepartmentManager() throws Exception {
        Employee departmentManager1 = existingEmployee(SampleTestDataFactory.employee());
        Employee departmentManager2 = existingEmployee(SampleTestDataFactory.employee());
        Department department1 = existingDepartment(SampleTestDataFactory.department(departmentManager1));

        mockMvc.perform(patch("/v1/departments/{departmentId}", department1.getId())
                        .param("managerId", String.valueOf(departmentManager2.getId()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(department1.getId()))
                .andExpect(jsonPath("$.name").value(department1.getName()))
                .andExpect(jsonPath("$.manager.id").value(departmentManager2.getId()))
                .andExpect(jsonPath("$.manager.name").value(departmentManager2.getName()))
                .andExpect(jsonPath("$.manager.lastName").value(departmentManager2.getLastName()))
                .andExpect(jsonPath("$.manager.email").value(departmentManager2.getEmail()));
    }

    @Test
    void shouldReturnDepartmentDetails_whenGettingDepartmentById() throws Exception {
        Employee departmentManager1 = existingEmployee(SampleTestDataFactory.employee());
        Employee departmentManager2 = existingEmployee(SampleTestDataFactory.employee());
        Department department1 = existingDepartment(SampleTestDataFactory.department(departmentManager1));
        existingDepartment(SampleTestDataFactory.department(departmentManager2));

        mockMvc.perform(get("/v1/departments/{departmentId}", department1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(department1.getId()))
                .andExpect(jsonPath("$.name").value(department1.getName()))
                .andExpect(jsonPath("$.manager.id").value(departmentManager1.getId()))
                .andExpect(jsonPath("$.manager.name").value(departmentManager1.getName()))
                .andExpect(jsonPath("$.manager.lastName").value(departmentManager1.getLastName()))
                .andExpect(jsonPath("$.manager.email").value(departmentManager1.getEmail()));
    }

    @Test
    void shouldReturnNotFound_whenGettingDepartmentById_thatDoesNotExist() throws Exception {
        mockMvc.perform(get("/v1/departments/{departmentId}", Long.MAX_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnDepartmentDetails_whenCreatingDepartment() throws Exception {
        Employee departmentManager1 = existingEmployee(SampleTestDataFactory.employee());
        String creationRequest = """
                                 {
                                   "name": "%s",
                                   "managerId": %s
                                 }
                                 """.formatted(SampleTestDataFactory.DEPARTMENT_NAME,
                                               departmentManager1.getId());

        mockMvc.perform(post("/v1/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(creationRequest))
                .andDo(log())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value(SampleTestDataFactory.DEPARTMENT_NAME))
                .andExpect(jsonPath("$.manager.id").value(departmentManager1.getId()))
                .andExpect(jsonPath("$.manager.name").value(departmentManager1.getName()))
                .andExpect(jsonPath("$.manager.lastName").value(departmentManager1.getLastName()))
                .andExpect(jsonPath("$.manager.email").value(departmentManager1.getEmail()));
    }

    @Test
    void shouldPersistDepartment_whenCreatingDepartment() throws Exception {
        Employee departmentManager1 = existingEmployee(SampleTestDataFactory.employee());
        String creationRequest = """
                                 {
                                   "name": "%s",
                                   "managerId": %s
                                 }
                                 """.formatted(SampleTestDataFactory.DEPARTMENT_NAME,
                departmentManager1.getId());

        mockMvc.perform(post("/v1/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(creationRequest))
                .andDo(log())
                .andExpect(status().isCreated());

        List<Department> allDepartments = getAllDepartments();
        assertThat(allDepartments).hasSize(1)
                .anySatisfy(department -> assertThatDepartment(department).hasName(SampleTestDataFactory.DEPARTMENT_NAME)
                        .hasManagerThat(manager -> manager.hasId(
                                departmentManager1.getId())));
    }

    @Test
    void shouldRemoveDepartmentFromRepository_whenDeletingDepartment() throws Exception {
        Employee departmentManager1 = existingEmployee(SampleTestDataFactory.employee());
        Department department1 = existingDepartment(new Department(SampleTestDataFactory.DEPARTMENT_NAME,
                departmentManager1));

        mockMvc.perform(delete("/v1/departments/{departmentId}", department1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().isNoContent());

        List<Department> allDepartments = getAllDepartments();
        assertThat(allDepartments).isEmpty();
    }
}
