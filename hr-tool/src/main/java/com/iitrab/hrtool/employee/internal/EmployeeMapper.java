package com.iitrab.hrtool.employee.internal;

import com.iitrab.hrtool.employee.api.Employee;
import org.springframework.stereotype.Component;

@Component
class EmployeeMapper {

    EmployeeDto toDto(Employee employee) {
        return new EmployeeDto(employee.getId(),
                               employee.getName(),
                               employee.getLastName(),
                               employee.getBirthdate(),
                               employee.getEmail());
    }

}
