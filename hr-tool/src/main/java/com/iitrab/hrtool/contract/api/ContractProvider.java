package com.iitrab.hrtool.contract.api;

import com.iitrab.hrtool.department.api.Department;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

/**
 * API interface for query operations on the {@link Contract} entities.
 * Implementation does not have to open new DB transaction. If the returned data has to be managed by the {@link EntityManager},
 * then the caller must open the transaction itself.
 */
public interface ContractProvider {

    /**
     * Returns all contracts, that were active in given {@link Department} at the provided {@link LocalDate}.
     *
     * @param department department to check
     * @param date       date to check
     * @return {@link List} with departments contracts active at date
     */
    List<Contract> getContractsInDepartmentAtDate(Department department, LocalDate date);

}
