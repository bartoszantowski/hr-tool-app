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
final class BasicDepartmentDto {

    @Nullable
    private final Long id;

    private final String name;
}
