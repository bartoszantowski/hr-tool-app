package com.iitrab.hrtool.contract.internal;

import com.iitrab.hrtool.contract.api.Contract;
import com.iitrab.hrtool.department.api.Department;
import com.iitrab.hrtool.employee.api.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static com.iitrab.hrtool.SampleTestDataFactory.*;
import static java.time.format.DateTimeFormatter.ISO_DATE;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ContractControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ContractServiceImpl contractServiceMock;

    @BeforeEach
    void setUp() {
        ContractController controller = new ContractController(contractServiceMock, new ContractMapper());
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                                 .build();
    }

    @Test
    void shouldReturnAllContracts_whenGettingAllContracts() throws Exception {
        Employee employee = employee();
        Department department = department(employee);
        Contract contract1 = activeContract(employee, department);
        Contract contract2 = activeContract(employee, department);
        when(contractServiceMock.findAllContracts()).thenReturn(List.of(contract1, contract2));

        mockMvc.perform(get("/v1/contracts"))
               .andDo(log())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$[0].employeeId").value(employee.getId()))
               .andExpect(jsonPath("$[0].departmentId").value(department.getId()))
               .andExpect(jsonPath("$[0].startDate").value(ISO_DATE.format(contract1.getStartDate())))
               .andExpect(jsonPath("$[0].endDate").value(ISO_DATE.format(contract1.getEndDate())))
               .andExpect(jsonPath("$[0].salary").value(contract1.getSalary()
                                                                 .intValue()))
               .andExpect(jsonPath("$[0].grade").value(contract1.getGrade()
                                                                .name()))
               .andExpect(jsonPath("$[1].employeeId").value(employee.getId()))
               .andExpect(jsonPath("$[1].departmentId").value(department.getId()))
               .andExpect(jsonPath("$[1].startDate").value(ISO_DATE.format(contract2.getStartDate())))
               .andExpect(jsonPath("$[1].endDate").value(ISO_DATE.format(contract2.getEndDate())))
               .andExpect(jsonPath("$[1].salary").value(contract2.getSalary()
                                                                 .intValue()))
               .andExpect(jsonPath("$[1].grade").value(contract2.getGrade()
                                                                .name()))
               .andExpect(jsonPath("$[2]").doesNotExist());
    }

    @Test
    void shouldReturnEmptyList_whenGettingContracts_andNoneIsPresent() throws Exception {
        when(contractServiceMock.findAllContracts()).thenReturn(List.of());

        mockMvc.perform(get("/v1/contracts"))
               .andDo(log())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$[0]").doesNotExist());
    }

}