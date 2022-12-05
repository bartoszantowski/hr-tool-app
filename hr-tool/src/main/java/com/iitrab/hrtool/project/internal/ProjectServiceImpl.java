package com.iitrab.hrtool.project.internal;

import com.iitrab.hrtool.client.api.Client;
import com.iitrab.hrtool.client.api.ClientNotFoundException;
import com.iitrab.hrtool.client.api.ClientProvider;
import com.iitrab.hrtool.project.api.Project;
import com.iitrab.hrtool.project.api.ProjectProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
class ProjectServiceImpl implements ProjectProvider {

    private final ProjectRepository projectRepository;
    private final ClientProvider clientProvider;

    @Override
    public Optional<Project> getProject(Long id) {
        return projectRepository.findById(id);
    }

    /**
     * Returns all projects.
     *
     * @return list of all projects
     */
    List<Project> findAllProjects() {
        return projectRepository.findAll();
    }

    /**
     * Creates and persists the project, based on the provided creation data.
     *
     * @param createProjectRequest data of the created project
     * @return created project
     */
    Project create(CreateProjectRequest createProjectRequest) {
        Long clientId = createProjectRequest.getClientId();
        Client client = clientProvider.getClient(clientId)
                                      .orElseThrow(() -> new ClientNotFoundException(clientId));
        Project project = new Project(createProjectRequest.getName(), client);
        log.info("Creating the project {}", project);
        return projectRepository.save(project);
    }

}
