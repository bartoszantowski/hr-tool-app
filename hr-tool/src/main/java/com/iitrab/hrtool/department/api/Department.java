package com.iitrab.hrtool.department.api;

import com.iitrab.hrtool.employee.api.Employee;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.eclipse.jdt.annotation.Nullable;

import javax.persistence.*;

@Entity
@Table(name = "departments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Nullable
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;
    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "manager_id")
    @Nullable
    private Employee manager;

    public Department(String name, Employee manager) {
        this.name = name;
        this.manager = manager;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }

}
