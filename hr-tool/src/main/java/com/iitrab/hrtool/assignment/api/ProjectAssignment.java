package com.iitrab.hrtool.assignment.api;

import com.iitrab.hrtool.employee.api.Employee;
import com.iitrab.hrtool.project.api.Project;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.eclipse.jdt.annotation.Nullable;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "project_assignments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class ProjectAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Nullable
    private Long id;

    @JoinColumn(name = "employee_id", updatable = false)
    @ManyToOne(cascade = CascadeType.DETACH)
    @Nullable
    private Employee employee;
    @JoinColumn(name = "project_id", updatable = false)
    @ManyToOne(cascade = CascadeType.DETACH)
    @Nullable
    private Project project;
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    @Column(name = "end_date")
    @Nullable
    private LocalDate endDate;
    @Column(name = "role", nullable = false)
    private String role;

    public ProjectAssignment(Employee employee,
                             Project project,
                             LocalDate startDate,
                             @Nullable LocalDate endDate,
                             String role) {
        this.employee = employee;
        this.project = project;
        this.startDate = startDate;
        this.endDate = endDate;
        this.role = role;
    }

}
