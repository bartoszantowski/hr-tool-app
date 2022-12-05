package com.iitrab.hrtool.department.internal;

import com.iitrab.hrtool.department.api.Department;
import com.iitrab.hrtool.department.api.DepartmentNotFoundException;
import com.iitrab.hrtool.department.api.DepartmentProvider;
import com.iitrab.hrtool.employee.api.Employee;
import com.iitrab.hrtool.employee.api.EmployeeNotFoundException;
import com.iitrab.hrtool.employee.api.EmployeeProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepartmentServiceImpl implements DepartmentProvider {

    private final DepartmentRepository departmentRepository;
    private final EmployeeProvider employeeProvider;

    /**
     * Returns department by id.
     *
     * @param departmentId id of the department to search
     * @return department
     */
    @Override
    public Optional<Department> getDepartment(Long departmentId) {
        return departmentRepository.findById(departmentId);
    }

    /**
     * Returns all departments.
     *
     * @return list of all departments
     */
    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    /**
     * Creates and persists the department, based on the provided creation data.
     *
     * @param createDepartmentRequest data of the created department
     * @return created client
     */
    Department create(CreateDepartmentRequest createDepartmentRequest) {
        Long managerId = createDepartmentRequest.getManagerId();
        Employee manager = employeeProvider.getEmployee(managerId)
                .orElseThrow(() -> new EmployeeNotFoundException(managerId));

        Department department = new Department(createDepartmentRequest.getName(), manager);
        log.info("Creating the department {}", department);

        return departmentRepository.save(department);
    }

    /**
     * Updates the department manager to the provided one and persists the change.
     * If the department or manager has not been persisted yet (does not have ID), then the implementation might throw {@link IllegalArgumentException}.
     *
     * @param departmentId id of the department to update manager
     * @param managerId new id manager of the department manager
     * @return updated department
     */
    Department updateDepartmentManager(Long departmentId, Long managerId) {

        Department department = getDepartment(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException(departmentId));

        Employee manager = employeeProvider.getEmployee(managerId)
                .orElseThrow(() -> new EmployeeNotFoundException(managerId));

        department.setManager(manager);
        log.info("Changing the manager of department with ID={} to {}", department.getId(), manager.getId());

        return departmentRepository.saveAndFlush(department);
    }

    /**
     * Deletes the department, based on the provided deletion data.
     *
     * @param departmentId data of the deleted department
     */
    void deleteDepartment(Long departmentId) {
        departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException(departmentId));

        departmentRepository.deleteById(departmentId);
    }

    /**
     * Returns departments by name or name fragment name.
     *
     * @param departmentName name of the departments to search
     * @return list of all departments
     */
    List<Department> getDepartmentsByName(String departmentName) {
        return departmentRepository.findByName(departmentName);
    }

    /**
     * Returns departments by manager.
     *
     * @param managerId id of the department manager to search
     * @return list of all departments
     */
    List<Department> getDepartmentsByManager(Long managerId) {
        return departmentRepository.findByManager(managerId);
    }
}
