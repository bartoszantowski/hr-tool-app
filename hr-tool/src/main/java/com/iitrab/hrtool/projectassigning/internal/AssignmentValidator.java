package com.iitrab.hrtool.projectassigning.internal;

import com.iitrab.hrtool.employee.api.Employee;
import com.iitrab.hrtool.project.api.Project;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Sample class to point out, that there could be some business rule validation, that is not "data" related, but still important for the process.
 */
@Component
@Slf4j
class AssignmentValidator {

    void canBeAssigned(Employee employee, Project project) throws AssignmentNotPossibleException {
        log.debug("Checking if employee {} can be assigned to the project {}", employee, project);
        /*
         no validation now - it is for the future use, to show that validation does not have to be only "data-related" (like with javax validation),
          but can be also "business-related" and then have to be coded specifically into the process
         */
    }

}
