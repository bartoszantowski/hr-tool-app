package com.iitrab.hrtool.department.internal;

import com.iitrab.hrtool.department.api.Department;
import com.iitrab.hrtool.employee.api.Employee;
import com.iitrab.hrtool.SampleTestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class DepartmentMapperTest {

    @InjectMocks
    private DepartmentMapper departmentMapper;

    @Test
    void shouldReturnDepartmentDtoPrimary_whenMappingDepartmentDtoPrimary() {
        Department department = SampleTestDataFactory.department(SampleTestDataFactory.employee());

        BasicDepartmentDto basicDepartmentDto = departmentMapper.toDtoPrimary(department);

        BasicDepartmentDtoAssert.assertThatDepartmentDtoPrimary(basicDepartmentDto).hasName(department.getName());
    }

    @Test
    void shouldReturnDepartmentDto_whenMappingDepartmentDto() {
        Employee employee = SampleTestDataFactory.employee();
        Department department = SampleTestDataFactory.department(employee);

        DepartmentDto departmentDto = departmentMapper.toDto(department);

        DepartmentDto expected = new DepartmentDto(null,
                                                    department.getName(),
                                                    new DepartmentManagerDto(employee.getId(),
                                                        employee.getName(),
                                                        employee.getLastName(),
                                                        employee.getEmail()));

        assertThat(departmentDto).isEqualTo(expected);
    }

    @Test
    void shouldReturnDepartmentDto_whenMappingDepartmentDto_andManagerIsNull() {
        Department department = SampleTestDataFactory.department(null);

        DepartmentDto departmentDto = departmentMapper.toDto(department);

        DepartmentDto expected = new DepartmentDto(null,
                department.getName(),
                null);

        assertThat(departmentDto).isEqualTo(expected);
    }
}
