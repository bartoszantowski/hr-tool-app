package com.iitrab.hrtool.contract.internal;

import com.iitrab.hrtool.IntegrationTestBase;
import com.iitrab.hrtool.contract.api.Contract;
import com.iitrab.hrtool.contract.api.Grade;
import com.iitrab.hrtool.department.api.Department;
import com.iitrab.hrtool.employee.api.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.iitrab.hrtool.SampleTestDataFactory.*;
import static com.iitrab.hrtool.contract.api.ContractAssert.assertThatContract;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class ContractRepositoryTest extends IntegrationTestBase {

    @Autowired
    private ContractRepository contractRepository;

    @Test
    void shouldReturnActiveContracts_whenFindingActiveContractsInDepartmentAtDate() {
        LocalDate now = LocalDate.now();
        Employee manager = existingEmployee(employee());
        Department department = existingDepartment(department(manager));
        Contract contract1 = existingContract(activeContract(manager, department));
        Contract contract2 = existingContract(activeContract(manager, department));

        List<Contract> contracts = contractRepository.findAllActiveInDepartmentAtDate(department,
                                                                                      now);

        assertThat(contracts).hasSize(2)
                             .anySatisfy(found -> assertThatContract(found).hasId(contract1.getId()))
                             .anySatisfy(found -> assertThatContract(found).hasId(contract2.getId()));
    }

    @Test
    void shouldNotFindActiveContractsFromOtherDepartment_whenFindingActiveContractsInDepartmentAtDate() {
        LocalDate now = LocalDate.now();
        Employee manager = existingEmployee(employee());
        Department department1 = existingDepartment(department(manager));
        Department department2 = existingDepartment(department(manager));
        existingContract(activeContract(manager, department1));
        existingContract(activeContract(manager, department1));

        List<Contract> contracts = contractRepository.findAllActiveInDepartmentAtDate(department2,
                                                                                      now);

        assertThat(contracts).isEmpty();
    }

    @Test
    void shouldNotFindContractsThatAreNotYetActive_whenFindingActiveContractsInDepartmentAtDate() {
        LocalDate now = LocalDate.now()
                                 .minusDays(10L);
        Employee manager = existingEmployee(employee());
        Department department = existingDepartment(department(manager));
        existingContract(activeContract(manager, department));
        existingContract(activeContract(manager, department));

        List<Contract> contracts = contractRepository.findAllActiveInDepartmentAtDate(department,
                                                                                      now);

        assertThat(contracts).isEmpty();
    }

    @Test
    void shouldNotFindContractsThatAreExpired_whenFindingActiveContractsInDepartmentAtDate() {
        LocalDate now = LocalDate.now()
                                 .plusDays(10L);
        Employee manager = existingEmployee(employee());
        Department department = existingDepartment(department(manager));
        existingContract(activeContract(manager, department));
        existingContract(activeContract(manager, department));

        List<Contract> contracts = contractRepository.findAllActiveInDepartmentAtDate(department,
                                                                                      now);

        assertThat(contracts).isEmpty();
    }

    @Test
    void shouldReturnActiveContracts_whenFindingActiveContractsInDepartmentAtDate_andItDoesNotHaveFinishDate() {
        LocalDate now = LocalDate.now();
        Employee manager = existingEmployee(employee());
        Department department = existingDepartment(department(manager));
        Contract contract = existingContract(new Contract(manager,
                                                          DATE_IN_PAST,
                                                          null,
                                                          department,
                                                          BigDecimal.ONE,
                                                          Grade.B,
                                                          "Engineer"));

        List<Contract> contracts = contractRepository.findAllActiveInDepartmentAtDate(department,
                                                                                      now);

        assertThat(contracts).hasSize(1)
                             .anySatisfy(found -> assertThatContract(found).hasId(contract.getId()));
    }

}