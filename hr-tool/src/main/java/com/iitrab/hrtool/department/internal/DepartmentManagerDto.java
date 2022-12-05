package com.iitrab.hrtool.department.internal;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.eclipse.jdt.annotation.Nullable;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
class DepartmentManagerDto {

    @Nullable
    private final Long id;
    private final String name;
    private final String lastName;
    private final String email;
}
