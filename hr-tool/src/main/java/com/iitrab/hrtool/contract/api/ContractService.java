package com.iitrab.hrtool.contract.api;

/**
 * API interface for performing modifying operations on {@link Contract} entities.
 * Implementation should perform the change in the database transaction either by continuing the existing one or creating new if needed.
 */
public interface ContractService {

    /**
     * Persists the passed contract.
     * If the contract has already DB ID assigned, then the implementation might throw an {@link IllegalArgumentException}.
     *
     * @param contract contract to create
     * @return created contract
     */
    Contract createContract(Contract contract);

}
