package com.iitrab.hrtool.client.internal;

import com.iitrab.hrtool.client.api.Client;
import org.eclipse.jdt.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.function.Predicate;

interface ClientRepository extends JpaRepository<Client, Long> {

    /**
     * Query for all clients, that are signed with provided name/fragment of name.
     *
     * @param clientName value to check
     * @return all matching clients
     */
    default List<Client> findByName(String clientName) {
        String lowerCaseClientName = clientName.toLowerCase();
        return findAll().stream()
                .filter(client -> client.getName().toLowerCase().contains(lowerCaseClientName))
                .toList();
    }

    /**
     * Query for all clients, that are signed with provided manager.
     *
     * @param managerId value to check
     * @return all matching clients
     */
    default List<Client> findByManager(Long managerId) {

        if (managerId.equals(null))
            return findAll().stream().toList();

        return findAll().stream()
                .filter(client -> client.getManager() != null &&
                        client.getManager().getId().equals(managerId))
                .toList();
    }


    /**
     * Query for all clients, that are signed with provided name and/or with provided manager.
     *
     * @param searchingForm values to check
     * @return all matching clients
     */
    default List<Client> findAllBySearchCriteria(SearchingForm searchingForm) {
        Predicate<Client> inNameMatcher = nameMatcher(searchingForm.name());
        Predicate<Client> inManagerMatcher = managerMatcher(searchingForm.managerId());

        return findAll().stream()
                .filter(inNameMatcher)
                .filter(inManagerMatcher)
                .toList();
    }

    private Predicate<Client> managerMatcher(@Nullable Long managerId) {
        return client -> managerId == null || client.getManager().getId().equals(managerId);
    }

    private Predicate<Client> nameMatcher(@Nullable String clientName) {
        return client -> clientName == null || client.getName().equals(clientName);
    }
}
