package com.iitrab.hrtool.department.internal;

import com.iitrab.hrtool.department.api.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface DepartmentRepository extends JpaRepository<Department, Long> {

    /**
     * Query for all clients, that are signed with provided name/fragment of name.
     *
     * @param departmentName value to check
     * @return all matching departments
     */
    default List<Department> findByName(String departmentName) {
        String lowerCaseDepartmentName = departmentName.toLowerCase();
        return findAll().stream()
                .filter(department -> department.getName().toLowerCase().contains(lowerCaseDepartmentName))
                .toList();
    }

    /**
     * Query for all departments, that are signed with provided manager.
     *
     * @param managerId value to check
     * @return all matching departments
     */
    default List<Department> findByManager(Long managerId) {
        if (managerId == null)
            return findAll().stream().toList();

        return findAll().stream()
                .filter(department -> department.getManager() != null &&
                                        department.getManager().getId().equals(managerId))
                                        .toList();
    }
}
