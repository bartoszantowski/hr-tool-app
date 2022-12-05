package com.iitrab.hrtool.projectmessage.internal;

import javax.validation.constraints.NotNull;
import java.util.List;

record ProjectMessageDto (@NotNull List<String> recipients,
                          @NotNull String messageContent,
                          @NotNull String messageSubject){
}
