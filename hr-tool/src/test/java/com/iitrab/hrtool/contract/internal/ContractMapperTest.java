package com.iitrab.hrtool.contract.internal;

import com.iitrab.hrtool.contract.api.Contract;
import com.iitrab.hrtool.department.api.Department;
import com.iitrab.hrtool.employee.api.Employee;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.iitrab.hrtool.SampleTestDataFactory.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ContractMapperTest {

    @InjectMocks
    private ContractMapper contractMapper;

    @Test
    void shouldReturnMappedContract_whenMappingToDto() {
        Employee employee = employee();
        Department department = department(employee);
        Contract contract = activeContract(employee, department);

        ContractDto dto = contractMapper.toDto(contract);

        ContractDto expectedDto = new ContractDto(contract.getId(),
                                                  employee.getId(),
                                                  department.getId(),
                                                  contract.getStartDate(),
                                                  contract.getEndDate(),
                                                  contract.getSalary(),
                                                  contract.getGrade(),
                                                  contract.getPosition());
        assertThat(dto).isEqualTo(expectedDto);
    }

}