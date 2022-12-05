package com.iitrab.hrtool.department.internal;

import com.iitrab.hrtool.department.api.Department;
import com.iitrab.hrtool.employee.api.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
final class DepartmentMapper {

    BasicDepartmentDto toDtoPrimary(Department department) {
        return new BasicDepartmentDto(department.getId(),
                                        department.getName());
    }

    DepartmentDto toDto(Department department) {
        return new DepartmentDto(department.getId(),
                                department.getName(),
                                Optional.ofNullable(department.getManager())
                                    .map(this::toDto)
                                    .orElse(null));
    }

    private DepartmentManagerDto toDto(Employee manager) {
        return new DepartmentManagerDto(manager.getId(),
                                        manager.getName(),
                                        manager.getLastName(),
                                        manager.getEmail());
    }
}
