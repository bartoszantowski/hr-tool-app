package com.iitrab.hrtool.projectmessage.internal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

record SendProjectMessageRequest(@NotNull @Positive Long authorId,
                                 @NotNull @Positive Long projectId,
                                 @NotNull String subject,
                                 @NotNull String content) {
}
