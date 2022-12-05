package com.iitrab.hrtool.recruitment.internal;

import com.iitrab.hrtool.employee.api.Employee;
import com.iitrab.hrtool.employee.api.EmployeeProvider;
import com.iitrab.hrtool.SampleTestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailGeneratorTest {

    @Mock
    private EmployeeProvider employeeProvider;

    @InjectMocks
    private EmailGenerator emailGenerator;

    @Test
    void shouldReturnGeneratedEmail_whenGeneratingEmail_andTheSameDoesNotExist() {
        String name = "john";
        String lastName = "test";
        String expectedEmail = "john.test@company.com";

        when(employeeProvider.getEmployeeByEmail(expectedEmail)).thenReturn(Optional.empty());

        String resultEmail = emailGenerator.generateEmail(name, lastName);

        assertThat(resultEmail).isEqualTo(expectedEmail);
    }

    @Test
    void shouldReturnGeneratedEmail_whenGeneratingEmail_andTheSameExist() {
        String name = "john";
        String lastName = "test";
        String existedEmail = "john.test@company.com";
        String expectedEmail = "john.test1@company.com";
        Employee employee = SampleTestDataFactory.employee();

        when(employeeProvider.getEmployeeByEmail(existedEmail)).thenReturn(Optional.of(employee));

        String resultEmail = emailGenerator.generateEmail(name, lastName);

        assertThat(resultEmail).isEqualTo(expectedEmail);
    }

    @Test
    void shouldReturnGeneratedEmail_whenGeneratingEmail_andTheSameExistTwice() {
        String name = "john";
        String lastName = "test";
        String existedEmail = "john.test@company.com";
        String existedEmail2 = "john.test1@company.com";
        String expectedEmail = "john.test2@company.com";
        Employee employee = SampleTestDataFactory.employee();

        when(employeeProvider.getEmployeeByEmail(existedEmail)).thenReturn(Optional.of(employee));
        when(employeeProvider.getEmployeeByEmail(existedEmail2)).thenReturn(Optional.of(employee));

        String resultEmail = emailGenerator.generateEmail(name, lastName);

        assertThat(resultEmail).isEqualTo(expectedEmail);
    }
}
