package com.iitrab.hrtool.recruitment.internal;

import com.iitrab.hrtool.contract.api.Contract;
import com.iitrab.hrtool.department.api.Department;
import com.iitrab.hrtool.employee.api.Employee;
import com.iitrab.hrtool.SampleTestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class FinalizedRecruitmentMapperTest {

    @InjectMocks
    private FinalizedRecruitmentMapper mapper;

    @Test
    void shouldReturnFinalizedRecruitmentDto_whenMappingFinalizedRecruitmentDto() {
        Employee manger = SampleTestDataFactory.employee();
        Employee employee = SampleTestDataFactory.employee();
        Department department = SampleTestDataFactory.department(manger);
        Contract contract = SampleTestDataFactory.activeContract(employee, department);

        FinalizedRecruitmentDto finalizedRecruitmentDto = mapper.toDto(employee, contract, department);

        FinalizedRecruitmentDto expected = new FinalizedRecruitmentDto(employee.getId(),
                                                                       employee.getName(),
                                                                       employee.getLastName(),
                                                                       employee.getBirthdate(),
                                                                       employee.getEmail(),
                                                                       contract.getId(),
                                                                       contract.getStartDate(),
                                                                       contract.getEndDate(),
                                                                       department.getId(),
                                                                       department.getName(),
                                                                       contract.getSalary(),
                                                                       contract.getGrade().toString(),
                                                                       contract.getPosition());

        assertThat(finalizedRecruitmentDto).isEqualTo(expected);
    }
}
