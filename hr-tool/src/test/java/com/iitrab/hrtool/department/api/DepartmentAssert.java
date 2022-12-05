package com.iitrab.hrtool.department.api;

import com.iitrab.hrtool.employee.api.EmployeeAssert;
import org.assertj.core.api.AbstractAssert;
import org.eclipse.jdt.annotation.Nullable;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public final class DepartmentAssert extends AbstractAssert<DepartmentAssert, Department> {

    private DepartmentAssert(Department department) {
        super(department, DepartmentAssert.class);
    }

    public static DepartmentAssert assertThatDepartment(Department department) {
        return new DepartmentAssert(department);
    }

    public DepartmentAssert hasId(@Nullable Long id) {
        isNotNull();
        assertThat(actual.getId()).isEqualTo(id);
        return this;
    }

    public DepartmentAssert hasName(String name) {
        isNotNull();
        assertThat(actual.getName()).isEqualTo(name);
        return this;
    }

    public DepartmentAssert hasManagerThat(Consumer<EmployeeAssert> manager) {
        isNotNull();
        manager.accept(EmployeeAssert.assertThatEmployee(actual.getManager()));
        return this;
    }

}
