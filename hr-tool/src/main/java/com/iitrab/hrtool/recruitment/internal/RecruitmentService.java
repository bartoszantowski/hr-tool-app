package com.iitrab.hrtool.recruitment.internal;

import com.iitrab.hrtool.candidate.api.Candidate;
import com.iitrab.hrtool.contract.api.Contract;
import com.iitrab.hrtool.contract.api.ContractService;
import com.iitrab.hrtool.department.api.Department;
import com.iitrab.hrtool.department.api.DepartmentNotFoundException;
import com.iitrab.hrtool.department.api.DepartmentProvider;
import com.iitrab.hrtool.employee.api.Employee;
import com.iitrab.hrtool.employee.api.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service is responsible for the recruitment of new employees.
 */
@Service
@RequiredArgsConstructor
@Slf4j
class RecruitmentService {

    private final ContractService contractService;
    private final DepartmentProvider departmentProvider;
    private final EmployeeService employeeService;
    private final FinalizedRecruitmentMapper finalizedRecruitmentMapper;
    private final RecruitmentCandidateService recruitmentCandidateService;
    private final RecruitmentEmailSender recruitmentEmailSender;
    private final EmailGenerator emailGenerator;

    /**
     * Finalizes recruitment process.
     * Checks and prepares the candidate.
     * Creates and persists new employee and new contract, based on the provided creation data.
     * Sends email to new employee.
     *
     * @param finalizeRecruitmentRequest data of the created employee and contract
     * @return created employee and contract
     */
    @Transactional
    public FinalizedRecruitmentDto finalizeRecruitment(FinalizeRecruitmentRequest finalizeRecruitmentRequest) {

        Candidate candidate = recruitmentCandidateService.prepareCandidate(finalizeRecruitmentRequest.candidateId());

        Employee employee = createEmployee(candidate);

        Contract contract = createContract(finalizeRecruitmentRequest, employee);

        recruitmentEmailSender.sendEmail(candidate.getName(), candidate.getLastName(), candidate.getPrivateEmail());

        return finalizedRecruitmentMapper.toDto(employee, contract, getDepartment(finalizeRecruitmentRequest.departmentId()));
    }

    private Employee createEmployee(Candidate candidate) {
        Employee employee = new Employee(candidate.getName(),
                                         candidate.getLastName(),
                                         candidate.getBirthdate(),
                                         emailGenerator.generateEmail(candidate.getName(), candidate.getLastName()));

        return employeeService.createEmployee(employee);
    }

    private Contract createContract(FinalizeRecruitmentRequest finalizeRecruitmentRequest, Employee employee) {
        Contract contract = new Contract(employee,
                                         finalizeRecruitmentRequest.contractStartDate(),
                                         finalizeRecruitmentRequest.contractEndDate(),
                                         getDepartment(finalizeRecruitmentRequest.departmentId()),
                                         finalizeRecruitmentRequest.salary(),
                                         finalizeRecruitmentRequest.grade(),
                                         finalizeRecruitmentRequest.position());

        contractService.createContract(contract);
        return contract;
    }

    private Department getDepartment(Long departmentId) {
        return departmentProvider.getDepartment(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException(departmentId));
    }
}
