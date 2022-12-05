package com.iitrab.hrtool.employee.internal;

import com.iitrab.hrtool.employee.api.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static com.iitrab.hrtool.SampleTestDataFactory.employee;
import static java.time.format.DateTimeFormatter.ISO_DATE;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmployeeServiceImpl employeeServiceMock;

    @BeforeEach
    void setUp() {
        EmployeeController employeeController = new EmployeeController(employeeServiceMock, new EmployeeMapper());
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController)
                                 .build();
    }

    @Test
    void shouldReturnAllEmployees_whenSearchingEmployees() throws Exception {
        Employee employee = employee();
        when(employeeServiceMock.findAllEmployees()).thenReturn(List.of(employee));

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/employees"))
               .andDo(log())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$[0].name").value(employee.getName()))
               .andExpect(jsonPath("$[0].lastName").value(employee.getLastName()))
               .andExpect(jsonPath("$[0].email").value(employee.getEmail()))
               .andExpect(jsonPath("$[0].birthdate").value(ISO_DATE.format(employee.getBirthdate())))
               .andExpect(jsonPath("$[1]").doesNotExist());
    }

    @Test
    void shouldReturnEmptyList_whenGettingEmployees_andNoneIsPresent() throws Exception {
        when(employeeServiceMock.findAllEmployees()).thenReturn(List.of());

        mockMvc.perform(get("/v1/employees"))
               .andDo(log())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$[0]").doesNotExist());
    }

}