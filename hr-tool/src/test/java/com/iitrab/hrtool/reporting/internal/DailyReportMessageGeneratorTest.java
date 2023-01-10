//package com.iitrab.hrtool.reporting.internal;
//
//import com.iitrab.hrtool.IntegrationTestBase;
//import com.iitrab.hrtool.contract.api.Contract;
//import com.iitrab.hrtool.department.api.Department;
//import com.iitrab.hrtool.employee.api.Employee;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Map;
//
//import static com.iitrab.hrtool.SampleTestDataFactory.department;
//import static com.iitrab.hrtool.SampleTestDataFactory.activeContract;
//import static com.iitrab.hrtool.SampleTestDataFactory.employee;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.assertj.core.api.Assertions.assertThat;
//
//
//@ExtendWith(MockitoExtension.class)
//class DailyReportMessageGeneratorTest extends IntegrationTestBase {
//
//    @Mock
//    private DailyReportMapper dailyReportMapper;
//
//    @InjectMocks
//    private DailyReportMessageGenerator dailyReportMessageGenerator;
//
//    @Test
//    void shouldReturnGeneratedDailyReportDtoList_whenGenerating() {
//        Employee manager = existingEmployee(employee());
//        Department department = existingDepartment(department(manager));
//        Employee employee1 = existingEmployee(employee());
//        Employee employee2 = existingEmployee(employee());
//        Employee employee3 = existingEmployee(employee());
//
//        Contract contract0 = existingContract(activeContract(manager, department));
//        Contract contract1 = existingContract(activeContract(employee1, department));
//        Contract contract2 = existingContract(activeContract(employee2, department));
//        Contract contract3 = existingContract(activeContract(employee3, department));
//
//        List<Contract> contracts = List.of(contract0, contract1, contract2, contract3);
//        Map<Department, List<Contract>> departmentsAndContracts = Map.of(department, contracts);
//
//        LocalDateTime localDateTime = LocalDateTime.now();
//
//        String tempSubject = "Daily report for: " + department.getName().toUpperCase();
//        String tempContent = "SALARY REPORT " + localDateTime +
//                "\nDepartment " + department.getName() +
//                "\nManager " + manager.getName() + " " + manager.getLastName() + " email: " + manager.getEmail();
//
//        StringBuilder content = new StringBuilder("\n");
//        for (Contract contract : contracts) {
//            Employee tempEmployee = contract.getEmployee();
//
//            content.append("\t")
//                    .append(tempEmployee.getName())
//                    .append(" ").append(tempEmployee.getLastName())
//                    .append(", email: ").append(tempEmployee.getEmail())
//                    .append(", position: ").append(contract.getPosition())
//                    .append(", grade: ").append(contract.getGrade())
//                    .append(", salary: ").append(contract.getSalary())
//                    .append("\n");
//        }
//        //tempContent + content.toString();
//
//        String content2 = tempContent + content.toString();
//
//        DailyReportDto dailyReportDto = new DailyReportDto(manager.getEmail(), tempSubject, content2);
//
//        when(dailyReportMapper.toDto(manager.getEmail(), tempSubject, content2)).thenReturn(dailyReportDto);
//
//        List<DailyReportDto> expected = dailyReportMessageGenerator.generate(localDateTime, departmentsAndContracts);
//
//        assertThat(expected).hasSize(1);
//
//    }
//}
