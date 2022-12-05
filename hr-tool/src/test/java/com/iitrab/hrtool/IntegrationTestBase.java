package com.iitrab.hrtool;

import com.iitrab.hrtool.assignment.api.ProjectAssignment;
import com.iitrab.hrtool.candidate.api.Candidate;
import com.iitrab.hrtool.client.api.Client;
import com.iitrab.hrtool.contract.api.Contract;
import com.iitrab.hrtool.department.api.Department;
import com.iitrab.hrtool.employee.api.Employee;
import com.iitrab.hrtool.project.api.Project;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class IntegrationTestBase {

    @RegisterExtension
    protected static final GreenMailExtension GREEN_MAIL = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig()
                                                     .withUser("user", "pass"))
            .withPerMethodLifecycle(true);

    /*
    The code below (autowiring the repositories) is a hack to be able to use the repositories that are otherwise accessible only by their package.
    This solution works because fields have the same names as corresponding repository beans (lower camel case name of the class/interface).
     */
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

    @AfterEach
    void cleanUpDB() {
        projectAssignmentRepository.deleteAll();
        projectRepository.deleteAll();
        clientRepository.deleteAll();
        contractRepository.deleteAll();
        departmentRepository.deleteAll();
        employeeRepository.deleteAll();
        candidateRepository.deleteAll();
    }

    protected Employee existingEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    protected List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    protected Contract existingContract(Contract contract) {
        return contractRepository.save(contract);
    }

    protected List<Contract> getAllContracts() {
        return contractRepository.findAll();
    }

    protected Department existingDepartment(Department department) {
        return departmentRepository.save(department);
    }

    protected List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    protected Candidate existingCandidate(Candidate candidate) {
        return candidateRepository.save(candidate);
    }

    protected List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    protected Project existingProject(Project project) {
        return projectRepository.save(project);
    }

    protected List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    protected ProjectAssignment existingProjectAssignment(ProjectAssignment projectAssignment) {
        return projectAssignmentRepository.save(projectAssignment);
    }

    protected List<ProjectAssignment> getAllProjectAssignments() {
        return projectAssignmentRepository.findAll();
    }

    protected Client existingClient(Client client) {
        return clientRepository.save(client);
    }

    protected List<Client> getAllClients() {
        return clientRepository.findAll();
    }

}
