package com.iitrab.hrtool.projectmessage.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/projectmessage")
@RequiredArgsConstructor
class ProjectMessageController {

    private final ProjectMessageService projectMessageService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    void sendProjectMessage(@RequestBody @Validated SendProjectMessageRequest sendProjectMessageRequest) {
        projectMessageService.publishProjectMessageEvent(sendProjectMessageRequest);
    }

}
