package com.iitrab.hrtool.client.internal;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.*;

record CreateClientRequest(@JsonProperty("name")
                           @NotBlank
                           String name,
                           @JsonProperty("contactNumber")
                           @Pattern(regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
                                   + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
                                   + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$")
                           String contactNumber,
                           @JsonProperty("contactEmail")
                           @Email
                           String contactEmail,
                           @JsonProperty("managerId")
                           @NotNull
                           @Positive
                           Long managerId) {

}
