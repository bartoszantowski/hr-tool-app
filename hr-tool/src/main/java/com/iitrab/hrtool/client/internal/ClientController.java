package com.iitrab.hrtool.client.internal;

import com.iitrab.hrtool.client.api.ClientNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/v1/clients")
@RequiredArgsConstructor
class ClientController {

    private final ClientServiceImpl clientService;
    private final ClientMapper clientMapper;

    @GetMapping
    public List<BasicClientDto> getAllClients() {
        return clientService.findAllClients()
                .stream()
                .map(clientMapper::toDtoPrimary)
                .toList();
    }

    @GetMapping("/{clientId}")
    public ClientDto getClientById(@PathVariable("clientId") Long clientId) {
        return clientService.getClient(clientId)
                            .map(clientMapper::toDto)
                            .orElseThrow(() -> new ClientNotFoundException(clientId));
    }

    @GetMapping(params = "name")
    public List<BasicClientDto> getClientsByName(@RequestParam("name") String clientName) {
        return clientService.getClientsByName(clientName)
                .stream()
                .map(clientMapper::toDtoPrimary)
                .toList();
    }

    @GetMapping(params = "managerId")
    public List<BasicClientDto> getClientsByManager(@RequestParam(required=false) Long managerId) {
        return clientService.getClientsByManager(managerId)
                .stream()
                .map(clientMapper::toDtoPrimary)
                .toList();
    }

    @PostMapping("/search")
    @ResponseStatus(OK)
    public List<BasicClientDto> getClientsByBody(@RequestBody @Validated SearchingForm searchingForm) {
        return clientService.getClientsByCriteria(searchingForm)
                .stream()
                .map(clientMapper::toDtoPrimary)
                .toList();
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public ClientDto createClient(@RequestBody @Validated CreateClientRequest createClientRequest) {
        return clientMapper.toDto(clientService.create(createClientRequest));
    }

    @DeleteMapping("/{clientId}")
    @ResponseStatus(NO_CONTENT)
    public void deleteClient(@PathVariable("clientId") Long clientId) {
        clientService.deleteClient(clientId);
    }
}
