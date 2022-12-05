package com.iitrab.hrtool.department.internal;

import com.iitrab.hrtool.department.api.DepartmentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/v1/departments")
@RequiredArgsConstructor
class DepartmentController {

    private final DepartmentServiceImpl departmentService;
    private final DepartmentMapper departmentMapper;

    @GetMapping
    public List<BasicDepartmentDto> getAllClients() {
        return departmentService.getAllDepartments()
                .stream()
                .map(departmentMapper::toDtoPrimary)
                .toList();
    }

    @GetMapping("/{departmentId}")
    public DepartmentDto getDepartmentById(@PathVariable("departmentId") Long departmentId) {
        return departmentService.getDepartment(departmentId)
                .map(departmentMapper::toDto)
                .orElseThrow(() -> new DepartmentNotFoundException(departmentId));
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public DepartmentDto createDepartment(@RequestBody @Validated CreateDepartmentRequest createDepartmentRequest) {
        return departmentMapper.toDto(departmentService.create(createDepartmentRequest));
    }

    @PatchMapping(value = "/{departmentId}", params = "managerId")
    public DepartmentDto updateDepartmentManager(@PathVariable("departmentId") Long departmentId,
                                                 @RequestParam Long managerId) {

        return departmentMapper.toDto(departmentService.updateDepartmentManager(departmentId, managerId));
    }

    @DeleteMapping("/{departmentId}")
    @ResponseStatus(NO_CONTENT)
    public void deleteDepartment(@PathVariable("departmentId") Long departmentId) {
        departmentService.deleteDepartment(departmentId);
    }

    @GetMapping(params = "name")
    public List<BasicDepartmentDto> getDepartmentsByName(@RequestParam("name") String departmentName) {
        return departmentService.getDepartmentsByName(departmentName)
                .stream()
                .map(departmentMapper::toDtoPrimary)
                .toList();
    }

    @GetMapping(params = "managerId")
    public List<BasicDepartmentDto> getDepartmentsByManager(@RequestParam(required=false) Long managerId) {
        return departmentService.getDepartmentsByManager(managerId)
                .stream()
                .map(departmentMapper::toDtoPrimary)
                .toList();
    }
}
