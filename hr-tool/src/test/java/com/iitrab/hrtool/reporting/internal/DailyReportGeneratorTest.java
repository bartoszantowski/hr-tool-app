package com.iitrab.hrtool.reporting.internal;

import com.iitrab.hrtool.contract.api.Contract;
import com.iitrab.hrtool.contract.api.ContractProvider;
import com.iitrab.hrtool.department.api.Department;
import com.iitrab.hrtool.department.api.DepartmentProvider;
import com.iitrab.hrtool.employee.api.Employee;
import com.iitrab.hrtool.SampleTestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DailyReportGeneratorTest {

    @Mock
    private DepartmentProvider departmentProviderMock;
    @Mock
    private ContractProvider contractProviderMock;
    @Mock
    private DailyReportMessageGenerator dailyReportMessageGeneratorMock;
    @InjectMocks
    private DailyReportGenerator dailyReportGenerator;

    @Test
    void shouldReturnListOfDailyReportDto_whenGenerating() {
        Employee employee1 = SampleTestDataFactory.employee();
        Employee employee2 = SampleTestDataFactory.employee();
        Employee employee3 = SampleTestDataFactory.employee();
        Department department1 = SampleTestDataFactory.department(employee1);
        Department department2 = SampleTestDataFactory.department(employee2);
        Contract contract1 = SampleTestDataFactory.activeContract(employee1, department1);
        Contract contract2 = SampleTestDataFactory.activeContract(employee2, department2);
        Contract contract3 = SampleTestDataFactory.activeContract(employee3, department2);
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDate localDate = localDateTime.toLocalDate();

        Map<Department, List<Contract>> departmentsAndContracts = new HashMap<>();
        departmentsAndContracts.put(department1, List.of(contract1));
        departmentsAndContracts.put(department2, List.of(contract2, contract3));

        DailyReportDto dto1 = new DailyReportDto(employee1.getEmail(), department1.getName(), "test");
        DailyReportDto dto2 = new DailyReportDto(employee2.getEmail(), department2.getName(), "test");

        when(departmentProviderMock.getAllDepartments()).thenReturn(List.of(department1, department2));
        when(contractProviderMock.getContractsInDepartmentAtDate(department1, localDate)).thenReturn(List.of(contract1));
        when(contractProviderMock.getContractsInDepartmentAtDate(department2, localDate)).thenReturn(List.of(contract2, contract3));
        when(dailyReportMessageGeneratorMock.generate(localDateTime, departmentsAndContracts)).thenReturn(List.of(dto1, dto2));

        List<DailyReportDto> expected = dailyReportGenerator.generate(localDateTime);

        assertThat(expected).hasSize(2);
    }
}
