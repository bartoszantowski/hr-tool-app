package com.iitrab.hrtool.client.api;

import com.iitrab.hrtool.exception.api.NotFoundException;

/**
 * Exception indicating that the {@link Client} was not found.
 */
@SuppressWarnings("squid:S110")
public class ClientNotFoundException extends NotFoundException {

    private ClientNotFoundException(String message) {
        super(message);
    }

    public ClientNotFoundException(Long id) {
        this("Client with ID=%s was not found".formatted(id));
    }

}
