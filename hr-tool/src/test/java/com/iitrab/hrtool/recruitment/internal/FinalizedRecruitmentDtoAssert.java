package com.iitrab.hrtool.recruitment.internal;

import org.assertj.core.api.AbstractAssert;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class FinalizedRecruitmentDtoAssert extends AbstractAssert<FinalizedRecruitmentDtoAssert, FinalizedRecruitmentDto> {

    private FinalizedRecruitmentDtoAssert(FinalizedRecruitmentDto finalizedRecruitmentDto) {
        super(finalizedRecruitmentDto, FinalizedRecruitmentDtoAssert.class);
    }

    public static  FinalizedRecruitmentDtoAssert assertThatFinalizedRecruitmentDto(FinalizedRecruitmentDto finalizedRecruitmentDto) {
        return new FinalizedRecruitmentDtoAssert(finalizedRecruitmentDto);
    }

    public FinalizedRecruitmentDtoAssert hasEmployeeId(Long id) {
        isNotNull();
        assertThat(actual.employeeId()).isEqualTo(id);
        return this;
    }

    public FinalizedRecruitmentDtoAssert hasEmployeeName(String employeeName) {
        isNotNull();
        assertThat(actual.employeeName()).isEqualTo(employeeName);
        return this;
    }

    public FinalizedRecruitmentDtoAssert hasEmployeeLastName(String employeeLastName) {
        isNotNull();
        assertThat(actual.employeeLastName()).isEqualTo(employeeLastName);
        return this;
    }

    public FinalizedRecruitmentDtoAssert hasEmployeeBirthdate(LocalDate birthdate) {
        isNotNull();
        assertThat(actual.birthdate()).isEqualTo(birthdate);
        return this;
    }

    public FinalizedRecruitmentDtoAssert hasEmployeeEmail(String employeeEmail) {
        isNotNull();
        assertThat(actual.employeeEmail()).isEqualTo(employeeEmail);
        return this;
    }

    public FinalizedRecruitmentDtoAssert hasContractId(Long contractId) {
        isNotNull();
        assertThat(actual.contractId()).isEqualTo(contractId);
        return this;
    }

    public FinalizedRecruitmentDtoAssert hasContractStartDate(LocalDate contractStartDate) {
        isNotNull();
        assertThat(actual.contractStartDate()).isEqualTo(contractStartDate);
        return this;
    }

    public FinalizedRecruitmentDtoAssert hasContractEndDate(LocalDate contractEndDate) {
        isNotNull();
        assertThat(actual.contractEndDate()).isEqualTo(contractEndDate);
        return this;
    }

    public FinalizedRecruitmentDtoAssert hasDepartmentId(Long departmentId) {
        isNotNull();
        assertThat(actual.departmentId()).isEqualTo(departmentId);
        return this;
    }

    public FinalizedRecruitmentDtoAssert hasDepartmentName(String departmentName) {
        isNotNull();
        assertThat(actual.departmentName()).isEqualTo(departmentName);
        return this;
    }

    public FinalizedRecruitmentDtoAssert hasSalary(BigDecimal salary) {
        isNotNull();
        assertThat(actual.salary()).isEqualTo(salary);
        return this;
    }

    public FinalizedRecruitmentDtoAssert hasGrade(String grade) {
        isNotNull();
        assertThat(actual.grade()).isEqualTo(grade);
        return this;
    }

    public FinalizedRecruitmentDtoAssert hasPosition(String position) {
        isNotNull();
        assertThat(actual.position()).isEqualTo(position);
        return this;
    }
}
