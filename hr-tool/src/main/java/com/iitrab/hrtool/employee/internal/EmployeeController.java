package com.iitrab.hrtool.employee.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/employees")
@RequiredArgsConstructor
class EmployeeController {

    private final EmployeeServiceImpl employeeService;
    private final EmployeeMapper employeeMapper;

    @GetMapping
    public List<EmployeeDto> getAllEmployees() {
        return employeeService.findAllEmployees()
                              .stream()
                              .map(employeeMapper::toDto)
                              .toList();
    }

}
