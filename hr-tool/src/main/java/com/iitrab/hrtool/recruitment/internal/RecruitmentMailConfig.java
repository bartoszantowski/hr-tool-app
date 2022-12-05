package com.iitrab.hrtool.recruitment.internal;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RecruitmentMailProperties.class)
class RecruitmentMailConfig {
}
