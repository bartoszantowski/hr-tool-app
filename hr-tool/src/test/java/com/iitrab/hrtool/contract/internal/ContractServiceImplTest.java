package com.iitrab.hrtool.contract.internal;

import com.iitrab.hrtool.SampleTestDataFactory;
import com.iitrab.hrtool.contract.api.Contract;
import com.iitrab.hrtool.department.api.Department;
import com.iitrab.hrtool.employee.api.Employee;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContractServiceImplTest {

    @InjectMocks
    private ContractServiceImpl contractService;

    @Mock
    private ContractRepository contractRepositoryMock;

    @Test
    void shouldReturnAllFoundContracts_whenFindingAllContracts() {
        Employee employee = SampleTestDataFactory.employee();
        Department department = SampleTestDataFactory.department(employee);
        Contract contract = SampleTestDataFactory.activeContract(employee, department);
        when(contractRepositoryMock.findAll()).thenReturn(List.of(contract));

        List<Contract> allContracts = contractService.findAllContracts();

        assertThat(allContracts).containsExactly(contract);
    }

    @Test
    void shouldReturnAllFoundContracts_whenFindingActiveContractsFromDepartment() {
        Employee employee = SampleTestDataFactory.employee();
        Department department = SampleTestDataFactory.department(employee);
        Contract contract = SampleTestDataFactory.activeContract(employee, department);
        LocalDate date = LocalDate.now();
        when(contractRepositoryMock.findAllActiveInDepartmentAtDate(department, date)).thenReturn(List.of(contract));

        List<Contract> allContracts = contractService.getContractsInDepartmentAtDate(department, date);

        assertThat(allContracts).containsExactly(contract);
    }

    @Test
    void shouldPersistContract_whenCreatingContract() {
        Employee employee = SampleTestDataFactory.employee();
        Department department = SampleTestDataFactory.department(employee);
        Contract contract = SampleTestDataFactory.activeContract(employee, department);

        contractService.createContract(contract);

        verify(contractRepositoryMock).save(contract);
    }

    @Test
    void shouldReturnCreatedContract_whenCreatingContract() {
        Employee employee = SampleTestDataFactory.employee();
        Department department = SampleTestDataFactory.department(employee);
        Contract contract = SampleTestDataFactory.activeContract(employee, department);
        when(contractRepositoryMock.save(any())).then(returnsFirstArg());

        Contract createdContract = contractService.createContract(contract);

        assertThat(createdContract).isSameAs(contract);
    }

    @Test
    void shouldThrowIllegalArgumentException_whenCreatingContract_whichHasIdSet() {
        Contract contractMock = mock(Contract.class);
        when(contractMock.getId()).thenReturn(1L);

        assertThatThrownBy(() -> contractService.createContract(contractMock)).isInstanceOf(IllegalArgumentException.class);
    }

}