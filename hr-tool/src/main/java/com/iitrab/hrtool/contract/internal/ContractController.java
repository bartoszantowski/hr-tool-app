package com.iitrab.hrtool.contract.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/contracts")
@RequiredArgsConstructor
class ContractController {

    private final ContractServiceImpl contractService;
    private final ContractMapper contractMapper;

    @GetMapping
    public List<ContractDto> getAllContracts() {
        return contractService.findAllContracts()
                              .stream()
                              .map(contractMapper::toDto)
                              .toList();
    }

}
