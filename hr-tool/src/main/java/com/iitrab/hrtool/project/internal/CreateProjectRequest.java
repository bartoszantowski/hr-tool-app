package com.iitrab.hrtool.project.internal;

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
final class CreateProjectRequest {

    @NotNull
    @Positive
    private final Long clientId;
    @NotBlank
    private final String name;

    @JsonCreator
    CreateProjectRequest(@JsonProperty("clientId") Long clientId, @JsonProperty("name") String name) {
        this.clientId = clientId;
        this.name = name;
    }

}
