package com.iitrab.hrtool.recruitment.internal;

import com.iitrab.hrtool.candidate.api.Candidate;
import com.iitrab.hrtool.candidate.api.CandidateNotFoundException;
import com.iitrab.hrtool.candidate.api.RecruitmentStatus;
import com.iitrab.hrtool.contract.api.ContractService;
import com.iitrab.hrtool.contract.api.Grade;
import com.iitrab.hrtool.department.api.Department;
import com.iitrab.hrtool.department.api.DepartmentNotFoundException;
import com.iitrab.hrtool.department.api.DepartmentProvider;
import com.iitrab.hrtool.employee.api.Employee;
import com.iitrab.hrtool.employee.api.EmployeeService;
import com.iitrab.hrtool.SampleTestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecruitmentServiceTest {

    @Mock
    private RecruitmentCandidateService recruitmentCandidateService;
    @Mock
    private EmployeeService employeeService;
    @Mock
    private DepartmentProvider departmentProvider;
    @Mock
    private FinalizedRecruitmentMapper finalizedRecruitmentMapper;
    @Mock
    private EmailGenerator emailGenerator;

    @Mock
    private ContractService contractService;

    @Mock
    private RecruitmentEmailSender recruitmentEmailSender;


    @InjectMocks
    private RecruitmentService recruitmentService;

    @Test
    void shouldReturnNotFoundException_whenFinalizing_withNoExistingCandidate() {
        Long hrEmployeeId = 2L;
        Long candidateId = 5L;
        LocalDate contractStartDate = LocalDate.of(2023, 1, 1);
        LocalDate contractEndDate = LocalDate.of(2024, 1, 1);
        Long departmentId = 5L;
        BigDecimal salary = BigDecimal.valueOf(5000);
        Grade grade = Grade.B;
        String position = "test";

        FinalizeRecruitmentRequest finalizeRecruitmentRequest = new FinalizeRecruitmentRequest(hrEmployeeId, candidateId, contractStartDate, contractEndDate,
                departmentId, salary, grade, position);

        Mockito.doThrow(new CandidateNotFoundException(candidateId)).when(recruitmentCandidateService).prepareCandidate(candidateId);

        assertThatThrownBy(() -> recruitmentService.finalizeRecruitment(finalizeRecruitmentRequest))
                .isInstanceOf(CandidateNotFoundException.class);
    }

    @Test
    void shouldReturnNotFoundException_whenFinalizing_withNoExistingDepartment() {
        Long hrEmployeeId = 2L;
        Long candidateId = 5L;
        LocalDate contractStartDate = LocalDate.of(2023, 1, 1);
        LocalDate contractEndDate = LocalDate.of(2024, 1, 1);
        Long departmentId = 5L;
        BigDecimal salary = BigDecimal.valueOf(5000);
        Grade grade = Grade.B;
        String position = "test";
        Candidate candidate = SampleTestDataFactory.candidate(RecruitmentStatus.ACCEPTED);

        FinalizeRecruitmentRequest finalizeRecruitmentRequest = new FinalizeRecruitmentRequest(hrEmployeeId, candidateId, contractStartDate, contractEndDate,
                departmentId, salary, grade, position);

        when(recruitmentCandidateService.prepareCandidate(candidateId)).thenReturn(candidate);
        Mockito.doThrow(new DepartmentNotFoundException(departmentId)).when(departmentProvider).getDepartment(departmentId);

        assertThatThrownBy(() -> recruitmentService.finalizeRecruitment(finalizeRecruitmentRequest))
                .isInstanceOf(DepartmentNotFoundException.class);
    }

    @Test
    void shouldReturnFinalizedRecruitmentDto_whenFinalizing() {
        Long hrEmployeeId = 2L;
        Long candidateId = 5L;
        LocalDate contractStartDate = LocalDate.of(2023, 5, 1);
        LocalDate contractEndDate = LocalDate.of(2024, 1, 1);
        Long departmentId = 5L;
        BigDecimal salary = BigDecimal.valueOf(5000);
        Grade grade = Grade.B;
        String position = "test";
        Candidate candidate = new Candidate("Tom", "Ayk", LocalDate.of(1990, 11, 23), "tom.ayk@gmail.com");

        String employeeEmail = candidate.getName() + candidate.getLastName() + "@company.com";
        Employee employee = new Employee(candidate.getName(), candidate.getLastName(), candidate.getBirthdate(), employeeEmail);
        Department department = new Department("HR", employee);

        FinalizeRecruitmentRequest finalizeRecruitmentRequest =
                new FinalizeRecruitmentRequest(hrEmployeeId, candidateId, contractStartDate, contractEndDate, departmentId, salary, grade, position);

        FinalizedRecruitmentDto finalizedRecruitmentDto = new FinalizedRecruitmentDto(
                10L,
                employee.getName(),
                employee.getLastName(),
                employee.getBirthdate(),
                employeeEmail,
                10L,
                contractStartDate,
                contractEndDate,
                departmentId,
                department.getName(),
                salary,
                grade.toString(),
                position
        );

        when(recruitmentCandidateService.prepareCandidate(candidateId)).thenReturn(candidate);
        when(emailGenerator.generateEmail(candidate.getName(), candidate.getLastName())).thenReturn(employeeEmail);
        when(employeeService.createEmployee(any())).thenReturn(employee);
        when(departmentProvider.getDepartment(departmentId)).thenReturn(Optional.of(department));
        when(finalizedRecruitmentMapper.toDto(any(), any(), any())).thenReturn(finalizedRecruitmentDto);
        doNothing().when(recruitmentEmailSender).sendEmail(candidate.getName(), candidate.getLastName(), candidate.getPrivateEmail());

        FinalizedRecruitmentDto expected = recruitmentService.finalizeRecruitment(finalizeRecruitmentRequest);

        FinalizedRecruitmentDtoAssert.assertThatFinalizedRecruitmentDto(expected)
                .hasEmployeeName(candidate.getName())
                .hasEmployeeLastName(candidate.getLastName())
                .hasEmployeeBirthdate(candidate.getBirthdate())
                .hasContractStartDate(contractStartDate)
                .hasContractEndDate(contractEndDate)
                .hasSalary(salary);
    }
}
