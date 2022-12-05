package com.iitrab.hrtool.project.internal;

import com.iitrab.hrtool.client.api.Client;
import com.iitrab.hrtool.project.api.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
class ProjectMapper {

    ProjectDto toDto(Project project) {
        Long clientId = Optional.ofNullable(project.getClient())
                                .map(Client::getId)
                                .orElse(null);
        return new ProjectDto(project.getId(),
                              clientId,
                              project.getName());
    }

}
