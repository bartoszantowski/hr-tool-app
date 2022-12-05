package com.iitrab.hrtool.contract.internal;

import com.iitrab.hrtool.contract.api.Contract;
import com.iitrab.hrtool.department.api.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

interface ContractRepository extends JpaRepository<Contract, Long> {

    /**
     * Query for all contracts, that are signed with provided department and were active on given date.
     *
     * @param department department to check
     * @param date       date to check
     * @return all matching contracts
     */
    default List<Contract> findAllActiveInDepartmentAtDate(Department department, LocalDate date) {
        Predicate<Contract> inDepartmentMatcher = isInDepartmentMatcher(department);
        Predicate<Contract> activeOnDateMatcher = isActiveOnDateMatcher(date);
        return findAll().stream()
                        .filter(inDepartmentMatcher)
                        .filter(activeOnDateMatcher)
                        .toList();
    }

    private Predicate<Contract> isInDepartmentMatcher(Department department) {
        return contract -> Objects.equals(contract.getDepartment()
                                                  .getId(), department.getId());
    }

    private Predicate<Contract> isActiveOnDateMatcher(LocalDate date) {
        return isStartedOn(date).and(isNotEndedOn(date));
    }

    private Predicate<Contract> isStartedOn(LocalDate date) {
        return contract -> contract.getStartDate() != null && contract.getStartDate()
                                                                      .isBefore(date);
    }

    private Predicate<Contract> isNotEndedOn(LocalDate date) {
        return contract -> contract.getEndDate() == null || contract.getEndDate()
                                                                    .isAfter(date);
    }

}
