package com.iitrab.hrtool.loader;

import com.iitrab.hrtool.IntegrationTest;
import com.iitrab.hrtool.IntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@Transactional
@ActiveProfiles("loadInitialData")
class InitialDataLoaderIntegrationTest extends IntegrationTestBase {

    @Test
    void shouldLoadInitialData_whenApplicationStarts_withLoadInitialDataProfile() {
        assertThat(getAllClients()).isNotEmpty();
        assertThat(getAllDepartments()).isNotEmpty();
        assertThat(getAllEmployees()).isNotEmpty();
        assertThat(getAllContracts()).isNotEmpty();
        assertThat(getAllProjects()).isNotEmpty();
        assertThat(getAllProjectAssignments()).isNotEmpty();
        assertThat(getAllCandidates()).isNotEmpty();
    }

}