package com.iitrab.hrtool.employee.internal;

import com.iitrab.hrtool.IntegrationTest;
import com.iitrab.hrtool.IntegrationTestBase;
import com.iitrab.hrtool.employee.api.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static com.iitrab.hrtool.SampleTestDataFactory.employee;
import static java.time.format.DateTimeFormatter.ISO_DATE;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@Transactional
class EmployeeApiIntegrationTest extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnAllEmployees_whenSearchingEmployees() throws Exception {
        Employee employee = existingEmployee(employee());

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/employees"))
               .andDo(log())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$[0].id").value(employee.getId()))
               .andExpect(jsonPath("$[0].name").value(employee.getName()))
               .andExpect(jsonPath("$[0].lastName").value(employee.getLastName()))
               .andExpect(jsonPath("$[0].email").value(employee.getEmail()))
               .andExpect(jsonPath("$[0].birthdate").value(ISO_DATE.format(employee.getBirthdate())))
               .andExpect(jsonPath("$[1]").doesNotExist());
    }

}