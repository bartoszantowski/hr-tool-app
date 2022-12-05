package com.iitrab.hrtool.contract.internal;

import com.iitrab.hrtool.IntegrationTest;
import com.iitrab.hrtool.IntegrationTestBase;
import com.iitrab.hrtool.contract.api.Contract;
import com.iitrab.hrtool.department.api.Department;
import com.iitrab.hrtool.employee.api.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static com.iitrab.hrtool.SampleTestDataFactory.*;
import static java.time.format.DateTimeFormatter.ISO_DATE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@Transactional
class ContractApiIntegrationTest extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnAllContracts_whenGettingAllContracts() throws Exception {
        Employee employee = existingEmployee(employee());
        Department department = existingDepartment(department(employee));
        Contract contract1 = existingContract(activeContract(employee, department));
        Contract contract2 = existingContract(activeContract(employee, department));

        mockMvc.perform(get("/v1/contracts"))
               .andDo(log())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$[0].id").value(contract1.getId()))
               .andExpect(jsonPath("$[0].employeeId").value(employee.getId()))
               .andExpect(jsonPath("$[0].departmentId").value(department.getId()))
               .andExpect(jsonPath("$[0].startDate").value(ISO_DATE.format(contract1.getStartDate())))
               .andExpect(jsonPath("$[0].endDate").value(ISO_DATE.format(contract1.getEndDate())))
               .andExpect(jsonPath("$[0].salary").value(contract1.getSalary()
                                                                 .intValue()))
               .andExpect(jsonPath("$[0].grade").value(contract1.getGrade()
                                                                .name()))
               .andExpect(jsonPath("$[1].id").value(contract2.getId()))
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

}