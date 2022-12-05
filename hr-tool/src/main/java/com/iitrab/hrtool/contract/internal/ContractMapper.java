package com.iitrab.hrtool.contract.internal;

import com.iitrab.hrtool.contract.api.Contract;
import com.iitrab.hrtool.department.api.Department;
import com.iitrab.hrtool.employee.api.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
class ContractMapper {

    ContractDto toDto(Contract contract) {
        Long employeeId = Optional.ofNullable(contract.getEmployee())
                                  .map(Employee::getId)
                                  .orElse(null);
        Long departmentId = Optional.ofNullable(contract.getDepartment())
                                    .map(Department::getId)
                                    .orElse(null);
        return new ContractDto(contract.getId(),
                               employeeId,
                               departmentId,
                               contract.getStartDate(),
                               contract.getEndDate(),
                               contract.getSalary(),
                               contract.getGrade(),
                               contract.getPosition());
    }

}
