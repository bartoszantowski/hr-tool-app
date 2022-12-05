package com.iitrab.hrtool.contract.api;

import com.iitrab.hrtool.department.api.Department;
import com.iitrab.hrtool.employee.api.Employee;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.eclipse.jdt.annotation.Nullable;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "contracts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Nullable
    private Long id;

    @JoinColumn(name = "employee_id", nullable = false, updatable = false)
    @ManyToOne(cascade = CascadeType.DETACH)
    @Nullable
    private Employee employee;
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    @Column(name = "end_date")
    @Nullable
    private LocalDate endDate;
    @JoinColumn(name = "department_id")
    @ManyToOne(cascade = CascadeType.DETACH)
    private Department department;
    @Column(name = "salary", nullable = false, updatable = false)
    private BigDecimal salary;
    @Column(name = "grade", nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private Grade grade;

    @Column(name = "position", nullable = false, updatable = false)
    private String position;

    public Contract(Employee employee,
                    LocalDate startDate,
                    @Nullable LocalDate endDate,
                    Department department,
                    BigDecimal salary,
                    Grade grade,
                    String position) {
        this.employee = employee;
        this.startDate = startDate;
        this.endDate = endDate;
        this.department = department;
        this.salary = salary;
        this.grade = grade;
        this.position = position;
    }

}
