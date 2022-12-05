package com.iitrab.hrtool.employee.internal;

import com.iitrab.hrtool.employee.api.Employee;
import com.iitrab.hrtool.employee.api.EmployeeProvider;
import com.iitrab.hrtool.employee.api.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
class EmployeeServiceImpl implements EmployeeProvider, EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public Optional<Employee> getEmployee(Long employeeId) {
        return employeeRepository.findById(employeeId);
    }

    @Override
    public Optional<Employee> getEmployeeByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }

    @Override
    public Employee createEmployee(Employee employee) {
        log.info("Creating employee {}", employee);
        if (employee.getId() != null) {
            throw new IllegalArgumentException("Employee has already DB ID, update is not permitted!");
        }
        return employeeRepository.save(employee);
    }

    /**
     * Returns all employees.
     *
     * @return list of all employees
     */
    List<Employee> findAllEmployees() {
        return employeeRepository.findAll();
    }

}
