package com.iitrab.hrtool;

import com.iitrab.hrtool.assignment.api.ProjectAssignment;
import com.iitrab.hrtool.candidate.api.Candidate;
import com.iitrab.hrtool.candidate.api.RecruitmentStatus;
import com.iitrab.hrtool.client.api.Client;
import com.iitrab.hrtool.contract.api.Contract;
import com.iitrab.hrtool.contract.api.Grade;
import com.iitrab.hrtool.department.api.Department;
import com.iitrab.hrtool.employee.api.Employee;
import com.iitrab.hrtool.project.api.Project;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

import static java.time.LocalDate.now;
import static java.util.UUID.randomUUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SampleTestDataFactory {

    public static final LocalDate NOW = now();
    public static final LocalDate DATE_IN_PAST = NOW.minusDays(1L);
    public static final LocalDate DATE_IN_FUTURE = NOW.plusDays(1L);
    public static final String DEPARTMENT_NAME = "HR";
    public static final String CLIENT_NAME = "Staplers";
    public static final String CLIENT_CONTACT_NUMBER = "123456789";
    public static final String CLIENT_CONTACT_EMAIL = "contact@stapler.com";

    public static ProjectAssignment activeAssignment(Employee employee, Project project) {
        return new ProjectAssignment(employee, project, DATE_IN_PAST, DATE_IN_FUTURE, randomUUID().toString());
    }

    public static Employee employee() {
        return new Employee(randomUUID().toString(), randomUUID().toString(), DATE_IN_PAST, randomUUID().toString());
    }

    public static Contract activeContract(Employee employee, Department department) {
        return new Contract(employee,
                            DATE_IN_PAST,
                            DATE_IN_FUTURE,
                            department,
                            new BigDecimal("10"),
                            Grade.B,
                            randomUUID().toString());
    }

    public static Client client(Employee manager) {
        return new Client(randomUUID().toString(), randomUUID().toString(), randomUUID().toString(), manager);
    }

    public static Project project(Client client) {
        return new Project(randomUUID().toString(), client);
    }

    public static Department department(Employee manager) {
        return new Department(randomUUID().toString(), manager);
    }

    public static Candidate candidate(RecruitmentStatus status) {
        return new Candidate(randomUUID().toString(),
                             randomUUID().toString(),
                             DATE_IN_PAST,
                             randomUUID().toString(),
                             status);
    }

}
