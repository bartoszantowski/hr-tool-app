package com.iitrab.hrtool.contract.internal;

import com.iitrab.hrtool.contract.api.Contract;
import com.iitrab.hrtool.contract.api.ContractProvider;
import com.iitrab.hrtool.contract.api.ContractService;
import com.iitrab.hrtool.department.api.Department;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
class ContractServiceImpl implements ContractService, ContractProvider {

    private final ContractRepository contractRepository;

    @Override
    public List<Contract> getContractsInDepartmentAtDate(Department department, LocalDate date) {
        return contractRepository.findAllActiveInDepartmentAtDate(department, date);
    }

    @Override
    public Contract createContract(Contract contract) {
        log.info("Creating the contract {}", contract);
        if (contract.getId() != null) {
            throw new IllegalArgumentException("Contract has already DB ID, update is not permitted!");
        }
        return contractRepository.save(contract);
    }

    /**
     * Returns all contracts.
     *
     * @return list of all contracts
     */
    List<Contract> findAllContracts() {
        return contractRepository.findAll();
    }

}
