package com.iitrab.hrtool.client.internal;

import com.fasterxml.jackson.annotation.JsonProperty;

record SearchingForm(@JsonProperty("name") String name,
                     @JsonProperty("managerId") Long managerId) {

}
