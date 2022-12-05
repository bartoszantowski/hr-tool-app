package com.iitrab.hrtool.projectassigning.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/projects/{projectId:\\d+}/assignments")
@RequiredArgsConstructor
class AssignmentController {

    private final AssignmentService assignmentService;
    private final AssignmentMapper assignmentMapper;

    @PostMapping
    public AssignmentDto assignEmployeeToProject(@PathVariable("projectId") Long projectId,
                                                 @RequestBody @Validated AssignToProjectRequest request) {
        return assignmentMapper.toDto(assignmentService.assignToProject(projectId, request));
    }

}
