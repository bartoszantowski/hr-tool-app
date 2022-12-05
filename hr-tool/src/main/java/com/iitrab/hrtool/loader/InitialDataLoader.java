package com.iitrab.hrtool.loader;

import com.iitrab.hrtool.assignment.api.ProjectAssignment;
import com.iitrab.hrtool.candidate.api.Candidate;
import com.iitrab.hrtool.candidate.api.RecruitmentStatus;
import com.iitrab.hrtool.client.api.Client;
import com.iitrab.hrtool.contract.api.Contract;
import com.iitrab.hrtool.contract.api.Grade;
import com.iitrab.hrtool.department.api.Department;
import com.iitrab.hrtool.employee.api.Employee;
import com.iitrab.hrtool.project.api.Project;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static java.time.LocalDate.now;
import static java.util.Objects.isNull;

/**
 * Sample data loader. If the application is run with `loadInitialData` profile, then on application startup it will fill the database with dummy data,
 * for the manual testing purposes.
 */
@Component
@Profile("loadInitialData")
@Slf4j
@ToString
class InitialDataLoader {

    // Field injection used as a hack - Spring will autowire expected repositories based on their bean names if they match the field names
    @Autowired
    private JpaRepository<ProjectAssignment, Long> projectAssignmentRepository;
    @Autowired
    private JpaRepository<Candidate, Long> candidateRepository;
    @Autowired
    private JpaRepository<Client, Long> clientRepository;
    @Autowired
    private JpaRepository<Contract, Long> contractRepository;
    @Autowired
    private JpaRepository<Department, Long> departmentRepository;
    @Autowired
    private JpaRepository<Employee, Long> employeeRepository;
    @Autowired
    private JpaRepository<Project, Long> projectRepository;

    @EventListener
    @Transactional
    @SuppressWarnings({"squid:S1854", "squid:S1481", "squid:S1192", "unused"})
    public void loadInitialData(ContextRefreshedEvent event) {
        verifyDependenciesAutowired();

        log.info("Loading initial data to the database");

        Employee manager1 = companyEmployee("Bob", "Vance", 40);
        Employee manager2 = companyEmployee("Jane", "Cullen", 37);
        Employee manager3 = companyEmployee("Bill", "Striker", 62);

        Department department1 = companyDepartment("HR", manager1);
        Department department2 = companyDepartment("Accounting", manager2);
        Department department3 = companyDepartment("Engineering", manager3);
        Department department4 = companyDepartment("Customer Relations", manager3);

        Contract contract1 = activeContract(manager1, department1, "HE Head", Grade.D, "10000");
        Contract contract2 = activeContract(manager2, department2, "Accounting Head", Grade.D, "10000");
        Contract contract3 = activeContract(manager3, department3, "Engineering Head", Grade.E, "15000");
        Contract contract4 = activeContract(manager3, department4, "PR Head", Grade.E, "15000");

        Employee employee1 = companyEmployee("Gil", "Gale", 22);
        Employee employee2 = companyEmployee("John", "Smith", 43);
        Employee employee3 = companyEmployee("Lee", "Bruce", 82);
        Employee employee4 = companyEmployee("Steve", "Wonder", 39);
        Employee employee5 = companyEmployee("Barbara", "Toll", 52);
        Employee employee6 = companyEmployee("Michael", "Scott", 25);
        Employee employee7 = companyEmployee("Tori", "Smith", 19);
        Employee employee8 = companyEmployee("Anna", "Kraft", 45);
        Employee employee9 = companyEmployee("Sam", "Kovalsky", 31);
        Employee employee10 = companyEmployee("Matthew", "Wirbelwind", 27);
        Employee employee11 = companyEmployee("Matthias", "Linde", 36);

        Contract contract11 = activeContract(employee1, department1, "Specialist", Grade.B, "5000");
        Contract contract12 = expiredContract(employee1, department1, "Junior Specialist", Grade.A2, "3000");
        Contract contract13 = activeContract(employee2, department2, "Senior Accountant", Grade.B, "4000");
        Contract contract14 = futureContract(employee2, department2, "Lead Accountant", Grade.C, "6000");
        Contract contract15 = expiredContract(employee3, department3, "Engineering Head", Grade.E, "12000");
        Contract contract16 = activeContract(employee4, department3, "Lead Engineer", Grade.C, "7000");
        Contract contract17 = expiredContract(employee4, department3, "Senior Engineer", Grade.B, "5000");
        Contract contract18 = expiredContract(employee4, department3, "Engineer", Grade.A2, "4000");
        Contract contract19 = expiredContract(employee4, department3, "Junior Engineer", Grade.A1, "3000");
        Contract contract110 = activeContract(employee5, department4, "Company Representative", Grade.C, "6000");
        Contract contract111 = activeContract(employee6, department4, "Tech Support Specialist", Grade.B, "5000");
        Contract contract112 = expiredContract(employee6, department3, "Engineer", Grade.A2, "4000");
        Contract contract113 = activeContract(employee7, department3, "Junior Engineer", Grade.A2, "3000");
        Contract contract114 = futureContract(employee7, department1, "Specialist", Grade.B, "4500");
        Contract contract115 = activeContract(employee8, department4, "Tech Support Specialist", Grade.B, "5500");
        Contract contract116 = expiredContract(employee9, department3, "Junior Engineer", Grade.A1, "3200");
        Contract contract117 = expiredContract(employee10, department3, "Senior Engineer", Grade.B, "5200");
        Contract contract118 = expiredContract(employee11, department3, "Senior Engineer", Grade.B, "5200");

        Client client1 = client("Pear", manager3);
        Client client2 = client("BigSoft", employee4);

        Project project1 = project("Glacier", client1);
        Project project2 = project("Iceberg", client1);
        Project project3 = project("Inferno", client2);

        ProjectAssignment assignment1 = activeAssignment(manager3, project1, "PM");
        ProjectAssignment assignment2 = activeAssignment(employee4, project1, "Team Lead");
        ProjectAssignment assignment3 = activeAssignment(employee7, project1, "Developer");
        ProjectAssignment assignment4 = activeAssignment(employee8, project1, "QA");
        ProjectAssignment assignment5 = activeAssignment(employee1, project1, "PMO");
        ProjectAssignment assignment6 = pastAssignment(manager3, project2, "PM");
        ProjectAssignment assignment7 = pastAssignment(employee4, project2, "Team Lead");
        ProjectAssignment assignment8 = pastAssignment(employee7, project2, "Developer");
        ProjectAssignment assignment9 = pastAssignment(employee9, project2, "Developer");
        ProjectAssignment assignment10 = activeAssignment(manager2, project3, "PM");
        ProjectAssignment assignment11 = activeAssignment(employee1, project3, "PMO");
        ProjectAssignment assignment12 = futureAssignment(employee4, project3, "Team Lead");
        ProjectAssignment assignment13 = futureAssignment(employee7, project3, "Team Lead");
        ProjectAssignment assignment14 = futureAssignment(employee8, project3, "QA");
        ProjectAssignment assignment15 = activeAssignment(employee10, project3, "Developer");
        ProjectAssignment assignment16 = activeAssignment(employee11, project3, "Developer");

        Candidate candidate1 = candidate("Steve", "Wonder", 39, RecruitmentStatus.HIRED);
        Candidate candidate2 = candidate("Sam", "Kovalsky", 31, RecruitmentStatus.HIRED);
        Candidate candidate3 = candidate("Michael", "Scott", 25, RecruitmentStatus.HIRED);
        Candidate candidate4 = candidate("Robert", "Howard", 59, RecruitmentStatus.ACCEPTED);
        Candidate candidate5 = candidate("Paul", "Atreides", 19, RecruitmentStatus.ACCEPTED);
        Candidate candidate6 = candidate("Tom", "Thetank", 41, RecruitmentStatus.WAITING_FOR_INTERVIEW);
        Candidate candidate7 = candidate("Kim", "Pattison", 19, RecruitmentStatus.WAITING_FOR_INTERVIEW);
        Candidate candidate8 = candidate("Angela", "Tuna", 58, RecruitmentStatus.NEW);
        Candidate candidate9 = candidate("Mel", "Gibson", 39, RecruitmentStatus.REJECTED);

        log.info("Finished loading initial data");
    }

    private Employee companyEmployee(String name, String lastName, int age) {
        Employee employee = new Employee(name,
                                         lastName,
                                         now().minusYears(age),
                                         "%s.%s@company.com".formatted(name, lastName));
        return employeeRepository.save(employee);
    }

    private Department companyDepartment(String name, Employee manager) {
        Department department = new Department(name, manager);
        return departmentRepository.save(department);
    }

    private Contract activeContract(Employee employee,
                                    Department department,
                                    String position,
                                    Grade grade,
                                    String salary) {
        return contract(employee, department, now().minusDays(100), null, position, grade, salary);
    }

    private Contract expiredContract(Employee employee,
                                     Department department,
                                     String position,
                                     Grade grade,
                                     String salary) {
        return contract(employee, department, now().minusDays(100), now().minusDays(50), position, grade, salary);
    }

    private Contract futureContract(Employee employee,
                                    Department department,
                                    String position,
                                    Grade grade,
                                    String salary) {
        return contract(employee, department, now().plusDays(100), null, position, grade, salary);
    }

    private Contract contract(Employee employee,
                              Department department,
                              LocalDate startDate,
                              LocalDate endDate,
                              String position,
                              Grade grade,
                              String salary) {
        Contract contract = new Contract(employee,
                                         startDate,
                                         endDate,
                                         department,
                                         new BigDecimal(salary),
                                         grade,
                                         position);
        return contractRepository.save(contract);
    }

    private Client client(String name, Employee manager) {
        Client client = new Client(name, "12345123", "office@%s.com".formatted(name), manager);
        return clientRepository.save(client);
    }

    private Project project(String name, Client client) {
        Project project = new Project(name, client);
        return projectRepository.save(project);
    }

    private ProjectAssignment activeAssignment(Employee employee, Project project, String role) {
        return projectAssignment(employee, project, now().minusDays(100), null, role);
    }

    private ProjectAssignment pastAssignment(Employee employee, Project project, String role) {
        return projectAssignment(employee,
                                 project,
                                 now().minusDays(100),
                                 now().minusDays(50),
                                 role);
    }

    private ProjectAssignment futureAssignment(Employee employee, Project project, String role) {
        return projectAssignment(employee, project, now().plusDays(50), null, role);
    }

    private ProjectAssignment projectAssignment(Employee employee,
                                                Project project,
                                                LocalDate startDate,
                                                LocalDate endDate,
                                                String role) {
        ProjectAssignment projectAssignment = new ProjectAssignment(employee, project, startDate, endDate, role);
        return projectAssignmentRepository.save(projectAssignment);
    }

    private Candidate candidate(String name, String lastName, int age, RecruitmentStatus status) {
        Candidate candidate = new Candidate(name,
                                            lastName,
                                            now().minusYears(age),
                                            "%s.%s@private.com".formatted(name, lastName),
                                            status);
        return candidateRepository.save(candidate);
    }

    private void verifyDependenciesAutowired() {
        if (isNull(projectAssignmentRepository)
                || isNull(candidateRepository)
                || isNull(clientRepository)
                || isNull(contractRepository)
                || isNull(departmentRepository)
                || isNull(employeeRepository)
                || isNull(projectRepository)) {
            throw new IllegalStateException("Initial data loader was not autowired correctly " + this);
        }
    }

}
