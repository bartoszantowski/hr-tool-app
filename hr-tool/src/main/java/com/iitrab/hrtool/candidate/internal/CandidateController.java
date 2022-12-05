package com.iitrab.hrtool.candidate.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/candidates")
@RequiredArgsConstructor
class CandidateController {

    private final CandidateServiceImpl candidateService;
    private final CandidateMapper candidateMapper;

    @GetMapping
    public List<CandidateDto> getAllCandidates(CandidateSearchCriteria searchCriteria) {
        return candidateService.findMatchingCandidates(searchCriteria)
                               .stream()
                               .map(candidateMapper::toDto)
                               .toList();
    }

}
