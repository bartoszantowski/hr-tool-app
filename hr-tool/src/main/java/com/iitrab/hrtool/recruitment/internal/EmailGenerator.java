package com.iitrab.hrtool.recruitment.internal;

import com.iitrab.hrtool.employee.api.Employee;
import com.iitrab.hrtool.employee.api.EmployeeProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Class for generating an email address.
 */
@Component
@RequiredArgsConstructor
class EmailGenerator {
    private final EmployeeProvider employeeProvider;

    /**
     * Generates a business email address of the candidate based on their first and last name.
     * Checks if a default e-mail address already exists in the database.
     * If so, it adds an appropriate unique value.
     *
     * @param name name of the candidate
     * @param lastName last name of the candidate
     * @return generated email address
     */
    String generateEmail(String name, String lastName) {
        final String DOMAIN = "@company.com";
        int counter = checkIfMailExists(name, lastName, DOMAIN);
        String generatedEmail;

        if (counter == 0) {
            generatedEmail = name + "." + lastName + DOMAIN;
        } else {
            generatedEmail = name + "." + lastName + counter + DOMAIN;
        }
        return generatedEmail;
    }

    private int checkIfMailExists(String name, String lastName, String domain) {
        int counter = 0;
        boolean isNextMail = true;
        String tempPrefix = name + "." + lastName;
        Optional<Employee> tempEmployee;

        do {
            if (counter == 0) {
                tempEmployee = employeeProvider.getEmployeeByEmail(tempPrefix + domain);
                if (tempEmployee.isPresent()) {
                    counter++;
                } else {
                    isNextMail = false;
                }
            }else {
                tempEmployee = employeeProvider.getEmployeeByEmail(tempPrefix + counter + domain);
                if (tempEmployee.isPresent()) {
                    counter++;
                } else {
                    isNextMail = false;
                }
            }
        } while(isNextMail);
        return counter;
    }
}
