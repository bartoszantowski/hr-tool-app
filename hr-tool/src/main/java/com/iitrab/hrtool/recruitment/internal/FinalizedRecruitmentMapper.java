package com.iitrab.hrtool.recruitment.internal;

import com.iitrab.hrtool.contract.api.Contract;
import com.iitrab.hrtool.department.api.Department;
import com.iitrab.hrtool.employee.api.Employee;
import org.springframework.stereotype.Component;

@Component
class FinalizedRecruitmentMapper {

    FinalizedRecruitmentDto toDto(Employee employee, Contract contract, Department department) {
        return new FinalizedRecruitmentDto(employee.getId(),
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
    }
}
