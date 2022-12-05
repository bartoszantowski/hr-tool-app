package com.iitrab.hrtool.department.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@ToString
@EqualsAndHashCode
class CreateDepartmentRequest {

    @NotBlank
    private final String name;

    @NotNull
    @Positive
    private final Long managerId;

    @JsonCreator
    public CreateDepartmentRequest(@JsonProperty("name") String name,
                                   @JsonProperty("managerId") Long managerId) {
        this.name = name;
        this.managerId = managerId;
    }
}
