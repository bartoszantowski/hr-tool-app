package com.iitrab.hrtool.project.internal;

import com.iitrab.hrtool.project.api.Project;
import org.springframework.data.jpa.repository.JpaRepository;

interface ProjectRepository extends JpaRepository<Project, Long> {

}
