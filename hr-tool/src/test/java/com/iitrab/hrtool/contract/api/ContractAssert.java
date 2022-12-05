package com.iitrab.hrtool.contract.api;

import com.iitrab.hrtool.department.api.Department;
import com.iitrab.hrtool.employee.api.Employee;
import org.assertj.core.api.AbstractAssert;
import org.eclipse.jdt.annotation.Nullable;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public final class ContractAssert extends AbstractAssert<ContractAssert, Contract> {

    private ContractAssert(Contract contract) {
        super(contract, ContractAssert.class);
    }

    public static ContractAssert assertThatContract(Contract contract) {
        return new ContractAssert(contract);
    }

    public ContractAssert hasId(@Nullable Long id) {
        isNotNull();
        assertThat(actual.getId()).isEqualTo(id);
        return this;
    }

    public ContractAssert isForEmployee(Employee employee) {
        isNotNull();
        assertThat(actual.getEmployee()).isEqualTo(employee);
        return this;
    }

    public ContractAssert isInDepartment(Department department) {
        isNotNull();
        assertThat(actual.getDepartment()).isEqualTo(department);
        return this;
    }

    public ContractAssert startsOn(LocalDate startDate) {
        isNotNull();
        assertThat(actual.getStartDate()).isEqualTo(startDate);
        return this;
    }

    public ContractAssert endsOn(LocalDate endDate) {
        isNotNull();
        assertThat(actual.getEndDate()).isEqualTo(endDate);
        return this;
    }

    public ContractAssert hasSalary(BigDecimal salary) {
        isNotNull();
        assertThat(actual.getSalary()).isEqualTo(salary);
        return this;
    }

    public ContractAssert hasGrade(Grade grade) {
        isNotNull();
        assertThat(actual.getGrade()).isEqualTo(grade);
        return this;
    }

    public ContractAssert hasPosition(String position) {
        isNotNull();
        assertThat(actual.getPosition()).isEqualTo(position);
        return this;
    }

}
