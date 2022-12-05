package com.iitrab.hrtool.reporting.internal;

import com.iitrab.hrtool.contract.api.Contract;
import com.iitrab.hrtool.department.api.Department;
import com.iitrab.hrtool.employee.api.Employee;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * A class to generate daily reports on employees in each department and their salary.
 */
@Slf4j
@Component
@RequiredArgsConstructor
class DailyReportMessageGenerator {

    private final DailyReportMapper dailyReportMapper;

    /**
     * This method generates a report for each department based on the results and data prepared.
     * In DailyReportDto includes the e-mail address of the recipient (department manager),
     * the subject of the message, the content of the report as
     * information about manager and
     * sorted data of employees of each department and their current contract
     *
     * @param localDateTime exact date of the report generation.
     * @param departmentsAndContracts a map with departments and their associated contracts.
     * @return list of DailyReportDto
     */
    List<DailyReportDto> generate(LocalDateTime localDateTime, Map<Department, List<Contract>> departmentsAndContracts) {

        List<DailyReportDto> dailyReportDtos = new ArrayList<>();

        for (Map.Entry<Department, List<Contract>> entry : departmentsAndContracts.entrySet()) {
            String tempDepartmentName = entry.getKey().getName();
            String tempSubject = "Daily report for: " + tempDepartmentName.toUpperCase();
            Employee tempManager = entry.getKey().getManager();
            String tempContent = crateContentMessage(localDateTime, tempManager, tempDepartmentName, entry.getValue());
            dailyReportDtos.add(dailyReportMapper.toDto(tempManager.getEmail(), tempSubject, tempContent));

        }
        return dailyReportDtos;
    }

    private String crateContentMessage(LocalDateTime localDateTime, Employee manager, String departmentName, List<Contract> contrats) {
        return "SALARY REPORT " + localDateTime +
               "\nDepartment " + departmentName +
               "\nManager " + manager.getName() + " " + manager.getLastName() + " email: " + manager.getEmail() +
               generateDepartmentEmployeesForReport(sortContractsByGradeAndSalary(contrats));
    }

    private List<Contract> sortContractsByGradeAndSalary(List<Contract> contracts) {
        return contracts.stream()
                .sorted(Comparator.comparing(Contract::getGrade)
                        .thenComparing(Contract::getSalary).reversed())
                .toList();
    }

    private String generateDepartmentEmployeesForReport(List<Contract> contracts) {
        StringBuilder content = new StringBuilder("\n");

        for (Contract contract : contracts) {
            Employee tempEmployee = contract.getEmployee();

            content.append("\t")
                    .append(tempEmployee.getName())
                    .append(" ").append(tempEmployee.getLastName())
                    .append(", email: ").append(tempEmployee.getEmail())
                    .append(", position: ").append(contract.getPosition())
                    .append(", grade: ").append(contract.getGrade())
                    .append(", salary: ").append(contract.getSalary())
                    .append("\n");

        }
        return content.toString();
    }
}
