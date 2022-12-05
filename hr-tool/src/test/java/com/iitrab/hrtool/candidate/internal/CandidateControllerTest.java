package com.iitrab.hrtool.candidate.internal;

import com.iitrab.hrtool.candidate.api.Candidate;
import com.iitrab.hrtool.SampleTestDataFactory;
import com.iitrab.hrtool.candidate.api.RecruitmentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static java.time.format.DateTimeFormatter.ISO_DATE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CandidateControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CandidateServiceImpl candidateServiceMock;

    @BeforeEach
    void setUp() {
        CandidateController controller = new CandidateController(candidateServiceMock, new CandidateMapper());
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                                 .build();
    }

    @Test
    void shouldReturnAllFoundCandidates_whenGettingCandidates_withoutSpecifiedSearchCriteria() throws Exception {
        Candidate candidate1 = SampleTestDataFactory.candidate(RecruitmentStatus.ACCEPTED);
        Candidate candidate2 = SampleTestDataFactory.candidate(RecruitmentStatus.REJECTED);
        CandidateSearchCriteria expectedCriteria = new CandidateSearchCriteria(null);
        when(candidateServiceMock.findMatchingCandidates(expectedCriteria)).thenReturn(List.of(candidate1, candidate2));

        mockMvc.perform(get("/v1/candidates"))
               .andDo(log())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$[0].name").value(candidate1.getName()))
               .andExpect(jsonPath("$[0].lastName").value(candidate1.getLastName()))
               .andExpect(jsonPath("$[0].birthDay").value(ISO_DATE.format(candidate1.getBirthdate())))
               .andExpect(jsonPath("$[0].recruitmentStatus").value(candidate1.getRecruitmentStatus()
                                                                             .name()))
               .andExpect(jsonPath("$[0].privateEmail").value(candidate1.getPrivateEmail()))
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
        Candidate candidate = SampleTestDataFactory.candidate(RecruitmentStatus.ACCEPTED);
        CandidateSearchCriteria expectedCriteria = new CandidateSearchCriteria(RecruitmentStatus.ACCEPTED);
        when(candidateServiceMock.findMatchingCandidates(expectedCriteria)).thenReturn(List.of(candidate));

        mockMvc.perform(get("/v1/candidates").param("status", RecruitmentStatus.ACCEPTED.name()))
               .andDo(log())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$[0].name").value(candidate.getName()))
               .andExpect(jsonPath("$[0].lastName").value(candidate.getLastName()))
               .andExpect(jsonPath("$[0].birthDay").value(ISO_DATE.format(candidate.getBirthdate())))
               .andExpect(jsonPath("$[0].recruitmentStatus").value(candidate.getRecruitmentStatus()
                                                                            .name()))
               .andExpect(jsonPath("$[1]").doesNotExist());
    }

    @Test
    void shouldReturnEmptyList_whenGettingCandidates_andNoneIsPresent() throws Exception {
        when(candidateServiceMock.findMatchingCandidates(any())).thenReturn(List.of());

        mockMvc.perform(get("/v1/candidates").param("status", RecruitmentStatus.ACCEPTED.name()))
               .andDo(log())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$[0]").doesNotExist());
    }
}