package com.iitrab.hrtool.projectassigning.internal;

import com.iitrab.hrtool.exception.api.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception indicating that the employee cannot be assigned to the project, due to the business reasons.
 */
@ResponseStatus(HttpStatus.CONFLICT)
class AssignmentNotPossibleException extends BusinessException {

    public AssignmentNotPossibleException(String message) {
        super(message);
    }

}
