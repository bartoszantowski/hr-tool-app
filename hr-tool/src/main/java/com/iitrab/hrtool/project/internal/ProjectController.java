package com.iitrab.hrtool.project.internal;

import com.iitrab.hrtool.project.api.ProjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/v1/projects")
@RequiredArgsConstructor
class ProjectController {

    private final ProjectServiceImpl projectService;
    private final ProjectMapper projectMapper;

    @GetMapping
    public List<ProjectDto> getAllProjects() {
        return projectService.findAllProjects()
                             .stream()
                             .map(projectMapper::toDto)
                             .toList();
    }

    @GetMapping("/{projectId}")
    public ProjectDto getProjectById(@PathVariable("projectId") Long projectId) {
        return projectService.getProject(projectId)
                             .map(projectMapper::toDto)
                             .orElseThrow(() -> new ProjectNotFoundException(projectId));
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public ProjectDto createProject(@RequestBody @Validated CreateProjectRequest createProjectRequest) {
        return projectMapper.toDto(projectService.create(createProjectRequest));
    }

}
