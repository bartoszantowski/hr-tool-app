package com.iitrab.hrtool.recruitment.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/v1/recruitment")
@RequiredArgsConstructor
class RecruitmentController {

    private final RecruitmentService recruitmentService;

    @PostMapping
    @ResponseStatus(CREATED)
    public FinalizedRecruitmentDto finalizationRecruitmentProcess(@RequestBody @Validated FinalizeRecruitmentRequest finalizeRecruitmentRequest) {
        return recruitmentService.finalizeRecruitment(finalizeRecruitmentRequest);
    }

}
