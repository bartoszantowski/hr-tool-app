package com.iitrab.hrtool.department.internal;

import com.iitrab.hrtool.IntegrationTestBase;
import com.iitrab.hrtool.department.api.Department;
import com.iitrab.hrtool.employee.api.Employee;
import com.iitrab.hrtool.SampleTestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.iitrab.hrtool.department.api.DepartmentAssert.assertThatDepartment;
import static org.assertj.core.api.Assertions.assertThat;

class DepartmentRepositoryTest extends IntegrationTestBase {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Test
    void shouldReturnEmptyList_whenSearchingDepartmentsByName_andThereAreNone() {
        List<Department> found = departmentRepository.findByName("AbCd");

        assertThat(found).isEmpty();
    }

    @Test
    void shouldReturnMatchingDepartments_whenSearchingDepartmentsByName_withEmptySearchRequest() {
        Department department1 = existingDepartment(new Department("Booby", null));
        Department department2 = existingDepartment(new Department("arob", null));

        List<Department> foundDepartments = departmentRepository.findByName("");

        assertThat(foundDepartments).hasSize(2)
                .anySatisfy(department -> assertThatDepartment(department).hasId(department1.getId()))
                .anySatisfy(department -> assertThatDepartment(department).hasId(department2.getId()));
    }

    @Test
    void shouldReturnMatchingDepartments_whenSearchingDepartmentsByName() {
        Department department1 = existingDepartment(new Department("Booby", null));
        Department department2 = existingDepartment(new Department("arob", null));

        List<Department> foundDepartments = departmentRepository.findByName("Ob");

        assertThat(foundDepartments).hasSize(2)
                .anySatisfy(department -> assertThatDepartment(department).hasId(department1.getId()))
                .anySatisfy(department -> assertThatDepartment(department).hasId(department2.getId()));
    }

    @Test
    void shouldReturnEmptyList_whenSearchingDepartmentsByManager_andThereAreNone() {
        List<Department> found = departmentRepository.findByManager(2L);

        assertThat(found).isEmpty();
    }

    @Test
    void shouldReturnMatchingDepartments_whenSearchingDepartmentsByManager_withEmptySearchRequest() {
        Employee manger = existingEmployee(SampleTestDataFactory.employee());
        Department department1 = existingDepartment(new Department("Booby", manger));
        Department department2 = existingDepartment(new Department("Al", manger));

        List<Department> foundDepartments = departmentRepository.findByManager(null);

        assertThat(foundDepartments).hasSize(2)
                .anySatisfy(department -> assertThatDepartment(department).hasId(department1.getId()))
                .anySatisfy(department -> assertThatDepartment(department).hasId(department2.getId()));
    }

    @Test
    void shouldReturnMatchingDepartments_whenSearchingDepartmentsByManager() {
        Employee manger1 = existingEmployee(SampleTestDataFactory.employee());
        Employee manger2 = existingEmployee(SampleTestDataFactory.employee());
        Department department1 = existingDepartment(new Department("Ba", manger1));
        Department department2 = existingDepartment(new Department("Al", manger2));
        Department department3 = existingDepartment(new Department("Weq", manger2));

        List<Department> foundDepartments = departmentRepository.findByManager(manger2.getId());

        assertThat(foundDepartments).hasSize(2)
                .anySatisfy(department -> assertThatDepartment(department).hasManagerThat(employeeAssert -> employeeAssert.hasId(department2.getManager().getId())))
                .anySatisfy(department -> assertThatDepartment(department).hasManagerThat(employeeAssert -> employeeAssert.hasId(department3.getManager().getId())));
    }

    @Test
    void shouldReturnMatchingDepartments_whenSearchingDepartmentsByManager_withNullManager() {
        Employee manager2 = existingEmployee(SampleTestDataFactory.employee());
        Department department1 = existingDepartment(new Department("Booby", null));
        Department department2 = existingDepartment(new Department("Al", manager2));

        List<Department> foundDepartments = departmentRepository.findByManager(manager2.getId());

        assertThat(foundDepartments).hasSize(1)
                .anySatisfy(department -> assertThatDepartment(department).hasManagerThat(employeeAssert -> employeeAssert.hasId(department2.getManager().getId())));
    }

    @Test
    void shouldReturnMatchingDepartments_whenSearchingDepartmentsByManager_withAllNullManagers() {
        Employee manger2 = existingEmployee(SampleTestDataFactory.employee());
        Department department1 = existingDepartment(new Department("Booby", null));
        Department department2 = existingDepartment(new Department("Al", null));

        List<Department> foundDepartments = departmentRepository.findByManager(manger2.getId());

        assertThat(foundDepartments).isEmpty();
    }
}
