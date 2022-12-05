package com.iitrab.hrtool.client.api;

import javax.persistence.EntityManager;
import java.util.Optional;

/**
 * API interface for query operations on the {@link Client} entities.
 * Implementation does not have to open new DB transaction. If the returned data has to be managed by the {@link EntityManager},
 * then the caller must open the transaction itself.
 */
public interface ClientProvider {

    /**
     * Returns the client based on its ID.
     * If the client with given ID is not found, then {@link Optional#empty()} will be returned.
     *
     * @param clientId id of the client to search
     * @return {@link Optional} containing found client or {@link Optional#empty()} if not found
     */
    Optional<Client> getClient(Long clientId);

}
