package com.iitrab.hrtool.employee.internal;

import com.iitrab.hrtool.employee.api.Employee;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.iitrab.hrtool.SampleTestDataFactory.employee;
import static com.iitrab.hrtool.employee.internal.EmployeeDtoAssert.assertThatEmployeeDto;

@ExtendWith(MockitoExtension.class)
class EmployeeMapperTest {

    @InjectMocks
    private EmployeeMapper employeeMapper;

    @Test
    void shouldReturnMappedEmployee_whenMappingEmployeeToDto() {
        Employee employee = employee();

        EmployeeDto employeeDto = employeeMapper.toDto(employee);

        assertThatEmployeeDto(employeeDto).hasName(employee.getName())
                                          .hasLastName(employee.getLastName())
                                          .wasBornOn(employee.getBirthdate())
                                          .hasEmail(employee.getEmail());
    }

}