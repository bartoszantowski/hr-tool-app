package com.iitrab.hrtool.candidate.api;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.eclipse.jdt.annotation.Nullable;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "candidates")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Nullable
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "lastName", nullable = false)
    private String lastName;
    @Column(name = "birthdate", nullable = false)
    private LocalDate birthdate;
    @Column(name = "recruitment_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private RecruitmentStatus recruitmentStatus;
    @Column(name = "private_email", nullable = false)
    private String privateEmail;

    public Candidate(String name, String lastName, LocalDate birthdate, String privateEmail) {
        this(name, lastName, birthdate, privateEmail, RecruitmentStatus.NEW);
    }

    public Candidate(String name, String lastName, LocalDate birthdate, String privateEmail, RecruitmentStatus status) {
        this.name = name;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.recruitmentStatus = status;
        this.privateEmail = privateEmail;
    }

    public void setRecruitmentStatus(RecruitmentStatus recruitmentStatus) {
        this.recruitmentStatus = recruitmentStatus;
    }

}
