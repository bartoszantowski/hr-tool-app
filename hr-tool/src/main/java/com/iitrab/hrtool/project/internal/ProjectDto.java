package com.iitrab.hrtool.project.internal;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.eclipse.jdt.annotation.Nullable;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
final class ProjectDto {

    @Nullable
    private final Long id;
    @Nullable
    private final Long clientId;
    private final String name;

}
