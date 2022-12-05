package com.iitrab.hrtool.employee.internal;

import com.iitrab.hrtool.employee.api.Employee;
import com.iitrab.hrtool.employee.api.EmployeeAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.iitrab.hrtool.SampleTestDataFactory.employee;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Mock
    private EmployeeRepository employeeRepositoryMock;
    @Captor
    private ArgumentCaptor<Employee> employeeCaptor;

    @Test
    void shouldReturnAllEmployees_whenGettingAllEmployees() {
        Employee employee = employee();
        when(employeeRepositoryMock.findAll()).thenReturn(List.of(employee));

        List<Employee> foundEmployees = employeeService.findAllEmployees();

        assertThat(foundEmployees).hasSize(1)
                                  .containsExactly(employee);
    }

    @Test
    void shouldReturnEmpty_whenGettingEmployeeById_andHeDoesNotExist() {
        long employeeId = 1L;
        when(employeeRepositoryMock.findById(employeeId)).thenReturn(Optional.empty());

        Optional<Employee> foundEmployees = employeeService.getEmployee(employeeId);

        assertThat(foundEmployees).isEmpty();
    }

    @Test
    void shouldReturnFoundEmployee_whenGettingEmployeeById() {
        Employee employee = employee();
        long employeeId = 1L;
        when(employeeRepositoryMock.findById(employeeId)).thenReturn(Optional.of(employee));

        Optional<Employee> foundEmployees = employeeService.getEmployee(employeeId);

        assertThat(foundEmployees).contains(employee);
    }

    @Test
    void shouldReturnEmpty_whenGettingEmployeeByEmail_andHeDoesNotExist() {
        String email = "email";
        when(employeeRepositoryMock.findByEmail(email)).thenReturn(Optional.empty());

        Optional<Employee> foundEmployees = employeeService.getEmployeeByEmail(email);

        assertThat(foundEmployees).isEmpty();
    }

    @Test
    void shouldReturnFoundEmployee_whenGettingEmployeeByEmail() {
        Employee employee = employee();
        String email = "email";
        when(employeeRepositoryMock.findByEmail(email)).thenReturn(Optional.of(employee));

        Optional<Employee> foundEmployees = employeeService.getEmployeeByEmail(email);

        assertThat(foundEmployees).contains(employee);
    }

    @Test
    void shouldReturnPersistEmployee_whenCreatingEmployee() {
        Employee employeeToCreate = employee();
        when(employeeRepositoryMock.save(any())).then(returnsFirstArg());

        Employee createdEmployee = employeeService.createEmployee(employeeToCreate);

        assertThat(createdEmployee).isSameAs(employeeToCreate);
    }

    @Test
    void shouldReturnCreatedEmployee_whenCreatingEmployee() {
        Employee employeeToCreate = employee();
        when(employeeRepositoryMock.save(any())).then(returnsFirstArg());

        employeeService.createEmployee(employeeToCreate);

        verify(employeeRepositoryMock).save(employeeCaptor.capture());

        EmployeeAssert.assertThatEmployee(employeeCaptor.getValue()).hasName(employeeToCreate.getName())
                                                     .hasLastName(employeeToCreate.getLastName())
                                                     .wasBornOn(employeeToCreate.getBirthdate())
                                                     .hasEmail(employeeToCreate.getEmail());
    }

    @Test
    void shouldThrowIllegalArgumentException_whenCreatingEmployee_withIdAlreadySet() {
        Employee employeeMock = mock(Employee.class);
        when(employeeMock.getId()).thenReturn(1L);

        assertThatThrownBy(() -> employeeService.createEmployee(employeeMock)).isInstanceOf(IllegalArgumentException.class);
    }

}