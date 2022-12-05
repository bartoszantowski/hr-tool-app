package com.iitrab.hrtool.candidate.internal;

import com.iitrab.hrtool.candidate.api.RecruitmentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.eclipse.jdt.annotation.Nullable;

import java.time.LocalDate;

record CandidateDto(@Nullable Long id, String name, String lastName,
                    @JsonFormat(pattern = "yyyy-MM-dd") LocalDate birthDay,
                    RecruitmentStatus recruitmentStatus, String privateEmail) {

}
