package com.iitrab.hrtool.recruitment.internal;

import com.iitrab.hrtool.candidate.api.Candidate;
import com.iitrab.hrtool.candidate.api.CandidateNotFoundException;
import com.iitrab.hrtool.candidate.api.RecruitmentStatus;
import com.iitrab.hrtool.contract.api.Contract;
import com.iitrab.hrtool.contract.api.Grade;
import com.iitrab.hrtool.department.api.Department;
import com.iitrab.hrtool.department.api.DepartmentNotFoundException;
import com.iitrab.hrtool.employee.api.Employee;
import com.iitrab.hrtool.SampleTestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RecruitmentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RecruitmentService recruitmentServiceMock;

    @BeforeEach
    void setUp() {
        RecruitmentController recruitmentController = new RecruitmentController(recruitmentServiceMock);
        mockMvc = MockMvcBuilders.standaloneSetup(recruitmentController)
                .build();
    }

    @Test
    void shouldReturnNotFound_whenFinalizingRecruitmentProcess_thatCandidateNotExist() throws Exception {
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
        Mockito.doThrow(new CandidateNotFoundException(candidateId)).when(recruitmentServiceMock).finalizeRecruitment(finalizeRecruitmentRequest);

        String requestBody = """
                             {
                               "hrEmployeeId": %s,
                               "candidateId": %s,
                               "contractStartDate": "%s",
                               "contractEndDate": "%s",
                               "departmentId": %s,
                               "salary": %s,
                               "grade": "%s",
                               "position": "%s"
                             }
                             """.formatted(hrEmployeeId, candidateId, contractStartDate, contractEndDate,
                departmentId, salary, grade, position);

        mockMvc.perform(post("/v1/recruitment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(log())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnNotFound_whenFinalizingRecruitmentProcess_thatDepartmentNotExist() throws Exception {
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
        Mockito.doThrow(new DepartmentNotFoundException(departmentId)).when(recruitmentServiceMock).finalizeRecruitment(finalizeRecruitmentRequest);

        String requestBody = """
                             {
                               "hrEmployeeId": %s,
                               "candidateId": %s,
                               "contractStartDate": "%s",
                               "contractEndDate": "%s",
                               "departmentId": %s,
                               "salary": %s,
                               "grade": "%s",
                               "position": "%s"
                             }
                             """.formatted(hrEmployeeId, candidateId, contractStartDate, contractEndDate,
                departmentId, salary, grade, position);

        mockMvc.perform(post("/v1/recruitment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(log())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnBadRequest_whenFinalizingRecruitmentProcess_thatWrongContractStartDate() throws Exception {
        Long hrEmployeeId = 2L;
        Long candidateId = 5L;
        LocalDate contractStartDate = LocalDate.of(2020, 1, 1);
        LocalDate contractEndDate = LocalDate.of(2024, 1, 1);
        Long departmentId = 5L;
        BigDecimal salary = BigDecimal.valueOf(5000);
        Grade grade = Grade.B;
        String position = "test";

        String requestBody = """
                             {
                               "hrEmployeeId": %s,
                               "candidateId": %s,
                               "contractStartDate": "%s",
                               "contractEndDate": "%s",
                               "departmentId": %s,
                               "salary": %s,
                               "grade": "%s",
                               "position": "%s"
                             }
                             """.formatted(hrEmployeeId, candidateId, contractStartDate, contractEndDate,
                departmentId, salary, grade, position);

        mockMvc.perform(post("/v1/recruitment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(log())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequest_whenFinalizingRecruitmentProcess_andEmptyRequest() throws Exception {
        String requestBody = """
                             {
                               
                             }
                             """;

        mockMvc.perform(post("/v1/recruitment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(log())
                .andExpect(status().isBadRequest());
    }

    @Test
    void FinalizedRecruitmentDto_whenFinalizingRecruitmentProcess_thatDepartmentNotExist() throws Exception {
        Long hrEmployeeId = 2L;
        Long candidateId = 5L;
        LocalDate contractStartDate = LocalDate.of(2023, 1, 1);
        LocalDate contractEndDate = LocalDate.of(2024, 1, 1);
        Long departmentId = 5L;
        BigDecimal salary = BigDecimal.valueOf(5000);
        Grade grade = Grade.B;
        String position = "test";

        Department department = new Department("department", SampleTestDataFactory.employee());
        Candidate candidate = SampleTestDataFactory.candidate(RecruitmentStatus.ACCEPTED);
        Employee employee = new Employee(candidate.getName(), candidate.getLastName(), candidate.getBirthdate(), candidate.getName() + candidate.getLastName() + "company.com");
        Contract contract = new Contract(employee, contractStartDate, contractEndDate, department, salary, grade, position);

        FinalizeRecruitmentRequest finalizeRecruitmentRequest = new FinalizeRecruitmentRequest(hrEmployeeId, candidateId, contractStartDate, contractEndDate,
                departmentId, salary, grade, position);
        //doThrow(new DepartmentNotFoundException(departmentId)).when(recruitmentServiceMock).finalizeRecruitment(finalizeRecruitmentRequest);

        FinalizedRecruitmentDto expected = new FinalizedRecruitmentDto(employee.getId(),
                employee.getName(),
                employee.getLastName(),
                employee.getBirthdate(),
                employee.getEmail(),
                contract.getId(),
                contract.getStartDate(),
                contract.getEndDate(),
                department.getId(),
                department.getName(),
                salary,
                grade.toString(),
                position);

        when(recruitmentServiceMock.finalizeRecruitment(finalizeRecruitmentRequest)).thenReturn(expected);

        String requestBody = """
                             {
                               "hrEmployeeId": %s,
                               "candidateId": %s,
                               "contractStartDate": "%s",
                               "contractEndDate": "%s",
                               "departmentId": %s,
                               "salary": %s,
                               "grade": "%s",
                               "position": "%s"
                             }
                             """.formatted(hrEmployeeId, candidateId, contractStartDate, contractEndDate,
                departmentId, salary, grade, position);

        mockMvc.perform(post("/v1/recruitment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(log())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.employeeId").value(employee.getId()));
    }
}
