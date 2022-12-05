package com.iitrab.hrtool.reporting.internal;

import com.iitrab.hrtool.contract.api.Contract;
import com.iitrab.hrtool.contract.api.ContractProvider;
import com.iitrab.hrtool.department.api.Department;
import com.iitrab.hrtool.department.api.DepartmentProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A class to generate data to prepare daily reports on employees in each department and their salary.
 */
@Slf4j
@Component
@RequiredArgsConstructor
class DailyReportGenerator {

    private final DepartmentProvider departmentProvider;
    private final ContractProvider contractProvider;
    private final DailyReportMessageGenerator dailyReportMessageGenerator;

    /**
     * This method generates a data to prepare report from each department.
     * Using private methods retrieves the list of branches, creates a map of the department-list of contracts.
     * Using DailyReportMessageGenerator generates contain messages
     *
     * @param localDateTime exact date of the report generation.
     * @return list of DailyReportDto
     */
    List<DailyReportDto> generate(LocalDateTime localDateTime) {
        List<Department> departments = getAllDepartments();
        LocalDate localDate = localDateTime.toLocalDate();
        Map<Department, List<Contract>> departmentsAndContracts = createDepartmentsWithContractsMap(departments, localDate);

        return dailyReportMessageGenerator.generate(localDateTime, departmentsAndContracts);
    }

    private Map<Department, List<Contract>> createDepartmentsWithContractsMap(List<Department> departments, LocalDate date) {
        return departments.stream()
                .filter(department -> department.getManager() != null)
                .collect(Collectors.toMap(department -> department,
                        department -> getContractsInDepartmentAtDate(date, department)));
    }

    private List<Contract> getContractsInDepartmentAtDate(LocalDate date, Department department) {
        return contractProvider.getContractsInDepartmentAtDate(department, date);
    }

    private List<Department> getAllDepartments() {
        return departmentProvider.getAllDepartments();
    }
}
