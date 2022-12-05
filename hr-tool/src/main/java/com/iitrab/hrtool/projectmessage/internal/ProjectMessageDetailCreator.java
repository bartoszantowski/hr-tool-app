package com.iitrab.hrtool.projectmessage.internal;

import com.iitrab.hrtool.assignment.api.ProjectAssignmentNotFoundException;
import com.iitrab.hrtool.assignment.api.ProjectAssignmentProvider;
import com.iitrab.hrtool.employee.api.Employee;
import com.iitrab.hrtool.employee.api.EmployeeNotFoundException;
import com.iitrab.hrtool.employee.api.EmployeeProvider;
import com.iitrab.hrtool.project.api.Project;
import com.iitrab.hrtool.project.api.ProjectNotFoundException;
import com.iitrab.hrtool.project.api.ProjectProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Class to create project message details.
 */
@Slf4j
@Component
@RequiredArgsConstructor
class ProjectMessageDetailCreator {

    private final ProjectAssignmentProvider projectAssignmentProvider;
    private final ProjectProvider projectProvider;
    private final EmployeeProvider employeeProvider;
    private final ProjectMessageMapper projectMessageMapper;

    /**
     * This method creates ProjectMessageDto.
     * Gets the author of event, project, checks if the author currently belongs to the project,
     * gets a list of employees belonging to this project,
     * creates the message content.
     * Collected data is sent to ProjectMessageMapper which returns ProjectMessageDto.
     *
     * @param projectMessageEvent event of project message
     * @return ProjectMessageDto
     */
    ProjectMessageDto create(ProjectMessageEvent projectMessageEvent) {
        Employee author = getAuthor(projectMessageEvent.getAuthorId());
        Project project = getProject(projectMessageEvent.getProjectId());

        isAuthorAssignedToProject(author, project);

        List<String> recipients = getRecipients(author, project);
        String messageContent = createMessageContent(author, projectMessageEvent.getContent(), project.getName());

        return projectMessageMapper.toDto(recipients, messageContent, projectMessageEvent);
    }

    private List<String> getRecipients(Employee author, Project project) {
        List<Employee> recipients= projectAssignmentProvider.getEmployeesAssignedToProject(project);

        return recipients.stream()
                .filter(employee -> employee != author)
                .map(Employee::getEmail)
                .toList();
    }

    private String createMessageContent(Employee author, String content, String projectName) {
        return "SENDER: " + author.getName() + " " + author.getEmail() +
                "\nPROJECT NAME: " + projectName +
                "\nMESSAGE:\n" + content;
    }

    private void isAuthorAssignedToProject(Employee author, Project project) {
        if (!projectAssignmentProvider.isAssignedToProject(author, project)) {
            log.info("Employee with ID=%s assignment to project with ID=%s was not found".formatted(author.getId(), project.getId()));
            throw new ProjectAssignmentNotFoundException(author.getId(), project.getId());
        }
    }

    private Project getProject(Long projectId) {
        return projectProvider.getProject(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
    }

    private Employee getAuthor(Long authorId) {
        return employeeProvider.getEmployee(authorId).orElseThrow(() -> new EmployeeNotFoundException(authorId));
    }
}
