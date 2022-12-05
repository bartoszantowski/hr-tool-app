package com.iitrab.hrtool.department.internal;

import com.iitrab.hrtool.department.api.Department;
import com.iitrab.hrtool.department.api.DepartmentNotFoundException;
import com.iitrab.hrtool.employee.api.Employee;
import com.iitrab.hrtool.employee.api.EmployeeNotFoundException;
import com.iitrab.hrtool.employee.api.EmployeeProvider;
import com.iitrab.hrtool.SampleTestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.iitrab.hrtool.department.api.DepartmentAssert.assertThatDepartment;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceImplTest {

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    @Mock
    private DepartmentRepository departmentRepositoryMock;

    @Mock
    private EmployeeProvider employeeProviderMock;

    @Captor
    private ArgumentCaptor<Department> departmentCaptor;
    @Captor
    private ArgumentCaptor<Long> departmentIdCaptor;

    @Test
    void shouldReturnAllDepartments_whenFindingAllDepartments() {
        Department department = SampleTestDataFactory.department(SampleTestDataFactory.employee());

        when(departmentRepositoryMock.findAll()).thenReturn(List.of(department));

        List<Department> foundDepartments = departmentService.getAllDepartments();

        assertThat(foundDepartments).hasSize(1)
                .contains(department);
    }

    @Test
    void shouldReturnMatchingDepartment_whenFindingDepartmentById() {
        Employee employee = SampleTestDataFactory.employee();
        Department department = SampleTestDataFactory.department(employee);
        long departmentId = 1L;

        when(departmentRepositoryMock.findById(departmentId)).thenReturn(Optional.of(department));

        Optional<Department> foundDepartment = departmentService.getDepartment(departmentId);

        assertThat(foundDepartment).contains(department);
    }

    @Test
    void shouldReturnEmptyOptional_whenFindingDepartmentById_andDoesNotExist() {
        long departmentId = 1L;

        when(departmentRepositoryMock.findById(departmentId)).thenReturn(Optional.empty());

        Optional<Department> foundDepartment = departmentService.getDepartment(departmentId);

        assertThat(foundDepartment).isEmpty();
    }

    @Test
    void shouldReturnNotFoundException_whenCreatingDepartment_withNoExistingManager() {
        long managerId = 1L;
        CreateDepartmentRequest createDepartmentRequest = new CreateDepartmentRequest("Test", managerId);

        when(employeeProviderMock.getEmployee(managerId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> departmentService.create(createDepartmentRequest)).isInstanceOf(
                EmployeeNotFoundException.class);
    }

    @Test
    void shouldPersistDepartment_whenCreatingDepartment() {
        Employee manager = SampleTestDataFactory.employee();
        String name = "NameTest";
        long managerId = 1L;

        CreateDepartmentRequest createDepartmentRequest = new CreateDepartmentRequest(name, managerId);

        when(employeeProviderMock.getEmployee(managerId)).thenReturn(Optional.of(manager));

        departmentService.create(createDepartmentRequest);

        verify(departmentRepositoryMock).save(departmentCaptor.capture());

        assertThatDepartment(departmentCaptor.getValue()).hasName(name)
                .hasManagerThat(employeeAssert -> employeeAssert.isSameAs(manager));
    }

    @Test
    void shouldReturnCreatedDepartment_whenCreatingDepartment() {
        Employee manager = SampleTestDataFactory.employee();
        String name = "NameTest";
        long managerId = 1L;
        CreateDepartmentRequest createDepartmentRequest = new CreateDepartmentRequest(name, managerId);

        when(employeeProviderMock.getEmployee(managerId)).thenReturn(Optional.of(manager));
        when(departmentRepositoryMock.save(any())).then(returnsFirstArg());

        Department department = departmentService.create(createDepartmentRequest);

        assertThatDepartment(department).hasName(name)
                .hasManagerThat(employeeAssert -> employeeAssert.isSameAs(manager));
    }

    @Test
    void shouldReturnUpdatedDepartment_whenUpdatingDepartment() {
        Employee manager1 = SampleTestDataFactory.employee();
        Employee manager2 = SampleTestDataFactory.employee();
        long manager2Id = 2L;

        Department department = SampleTestDataFactory.department(manager1);
        long departmentId = 1L;

        when(departmentRepositoryMock.saveAndFlush(any())).then(returnsFirstArg());
        when(departmentRepositoryMock.findById(departmentId)).thenReturn(Optional.of(department));
        when(employeeProviderMock.getEmployee(manager2Id)).thenReturn(Optional.of(manager2));

        departmentService.updateDepartmentManager(departmentId, manager2Id);
        verify(departmentRepositoryMock).saveAndFlush(departmentCaptor.capture());


        assertThatDepartment(departmentCaptor.getValue())
                .hasManagerThat(employeeAssert -> employeeAssert.isSameAs(manager2));
    }

    @Test
    void shouldReturnPersistDepartment_whenUpdatingDepartment() {
        Employee manager1 = SampleTestDataFactory.employee();
        Employee manager2 = SampleTestDataFactory.employee();
        long manager2Id = 2L;

        Department department = SampleTestDataFactory.department(manager1);
        long departmentId = 1L;

        when(departmentRepositoryMock.saveAndFlush(any())).then(returnsFirstArg());
        when(departmentRepositoryMock.findById(departmentId)).thenReturn(Optional.of(department));
        when(employeeProviderMock.getEmployee(manager2Id)).thenReturn(Optional.of(manager2));

        Department updatedDepartment = departmentService.updateDepartmentManager(departmentId, manager2Id);

        assertThat(updatedDepartment).isSameAs(department);
    }

    @Test
    void shouldRemoveDepartment_whenDeletingDepartment() {
        Employee employee = SampleTestDataFactory.employee();
        Department department = SampleTestDataFactory.department(employee);
        long departmentId = 1L;

        when(departmentRepositoryMock.findById(departmentId)).thenReturn(Optional.of(department));
        departmentService.deleteDepartment(departmentId);

        verify(departmentRepositoryMock).deleteById(departmentIdCaptor.capture());

        assertThat(departmentIdCaptor.getValue().equals(departmentId));
   }

    @Test
    void shouldRemoveDepartmentFromRepository_whenDeletingDepartment_andDoesNotExist() {
        long departmentId = 1L;

        when(departmentRepositoryMock.findById(departmentId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> departmentService.deleteDepartment(departmentId))
                .isInstanceOf(DepartmentNotFoundException.class);
    }

    @Test
    void shouldReturnMatchingDepartments_whenFindingDepartmentByName_withFragmentNameIgnoringCase() {
        Employee employee = SampleTestDataFactory.employee();
        Department department = new Department("Boobi", employee);

        when(departmentRepositoryMock.findByName("boo")).thenReturn(List.of(department));

        List<Department> foundDepartment = departmentService.getDepartmentsByName("boo");

        assertThat(foundDepartment).contains(department);
    }

    @Test
    void shouldReturnEmptyList_whenFindingDepartmentByName_withFragmentNameIgnoringCase_andNoneIsMatching() {
        when(departmentRepositoryMock.findByName("Ob")).thenReturn(List.of());

        List<Department> foundDepartment = departmentService.getDepartmentsByName("Ob");

        assertThat(foundDepartment).isEmpty();
    }

    @Test
    void shouldReturnAllDepartments_whenFindingDepartmentByName_withEmptyName() {
        Employee employee = SampleTestDataFactory.employee();
        Department department1 = new Department("Boobi", employee);
        Department department2 = new Department("Neaoby", employee);

        when(departmentRepositoryMock.findByName("")).thenReturn(List.of(department1, department2));

        List<Department> foundDepartment = departmentService.getDepartmentsByName("");

        assertThat(foundDepartment).contains(department1, department2);
    }

    @Test
    void shouldReturnMatchingDepartments_whenFindingDepartmentByManager_withNullManager() {
        Employee employee = SampleTestDataFactory.employee();
        Department department1 = new Department("Boobi", employee);
        Department department2 = new Department("Neaoby", employee);

        when(departmentRepositoryMock.findByManager(null)).thenReturn(List.of(department1, department2));

        List<Department> foundDepartment = departmentService.getDepartmentsByManager(null);

        assertThat(foundDepartment).contains(department1, department2);
    }

    @Test
    void shouldReturnMatchingDepartments_whenFindingDepartmentByManager() {
        Employee employee = SampleTestDataFactory.employee();
        Long managerId = 2L;
        Department department1 = new Department("Boobi", employee);
        Department department2 = new Department("Neaoby", employee);

        when(departmentRepositoryMock.findByManager(managerId)).thenReturn(List.of(department1, department2));

        List<Department> foundDepartment = departmentService.getDepartmentsByManager(managerId);

        assertThat(foundDepartment).contains(department1, department2);
    }

    @Test
    void shouldReturnMatchingDepartments_whenFindingDepartmentByManager_andNoneIsMatching() {
        Long managerId = 2L;

        when(departmentRepositoryMock.findByManager(managerId)).thenReturn(List.of());

        List<Department> foundDepartment = departmentService.getDepartmentsByManager(managerId);

        assertThat(foundDepartment).isEmpty();
    }

}
