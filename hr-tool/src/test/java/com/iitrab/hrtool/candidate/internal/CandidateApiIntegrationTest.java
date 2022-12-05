package com.iitrab.hrtool.candidate.internal;

import com.iitrab.hrtool.IntegrationTest;
import com.iitrab.hrtool.IntegrationTestBase;
import com.iitrab.hrtool.candidate.api.Candidate;
import com.iitrab.hrtool.SampleTestDataFactory;
import com.iitrab.hrtool.candidate.api.RecruitmentStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static java.time.format.DateTimeFormatter.ISO_DATE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@Transactional
class CandidateApiIntegrationTest extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnAllFoundCandidates_whenGettingAllCandidates_withoutSpecifiedSearchCriteria() throws Exception {
        Candidate candidate1 = existingCandidate(SampleTestDataFactory.candidate(RecruitmentStatus.ACCEPTED));
        Candidate candidate2 = existingCandidate(SampleTestDataFactory.candidate(RecruitmentStatus.REJECTED));

        mockMvc.perform(get("/v1/candidates"))
               .andDo(log())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$[0].id").value(candidate1.getId()))
               .andExpect(jsonPath("$[0].name").value(candidate1.getName()))
               .andExpect(jsonPath("$[0].lastName").value(candidate1.getLastName()))
               .andExpect(jsonPath("$[0].birthDay").value(ISO_DATE.format(candidate1.getBirthdate())))
               .andExpect(jsonPath("$[0].recruitmentStatus").value(candidate1.getRecruitmentStatus()
                                                                             .name()))
               .andExpect(jsonPath("$[0].privateEmail").value(candidate1.getPrivateEmail()))
               .andExpect(jsonPath("$[1].id").value(candidate2.getId()))
               .andExpect(jsonPath("$[1].name").value(candidate2.getName()))
               .andExpect(jsonPath("$[1].lastName").value(candidate2.getLastName()))
               .andExpect(jsonPath("$[1].birthDay").value(ISO_DATE.format(candidate2.getBirthdate())))
               .andExpect(jsonPath("$[1].recruitmentStatus").value(candidate2.getRecruitmentStatus()
                                                                             .name()))
               .andExpect(jsonPath("$[1].privateEmail").value(candidate2.getPrivateEmail()))
               .andExpect(jsonPath("$[2]").doesNotExist());
    }

    @Test
    void shouldReturnAllFoundCandidates_whenGettingCandidates_withGivenRecruitmentStatus() throws Exception {
        Candidate candidate = existingCandidate(SampleTestDataFactory.candidate(RecruitmentStatus.ACCEPTED));
        existingCandidate(SampleTestDataFactory.candidate(RecruitmentStatus.REJECTED));

        mockMvc.perform(get("/v1/candidates").param("status", RecruitmentStatus.ACCEPTED.name()))
               .andDo(log())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$[0].id").value(candidate.getId()))
               .andExpect(jsonPath("$[0].name").value(candidate.getName()))
               .andExpect(jsonPath("$[0].lastName").value(candidate.getLastName()))
               .andExpect(jsonPath("$[0].birthDay").value(ISO_DATE.format(candidate.getBirthdate())))
               .andExpect(jsonPath("$[0].recruitmentStatus").value(candidate.getRecruitmentStatus()
                                                                            .name()))
               .andExpect(jsonPath("$[1]").doesNotExist());
    }

}