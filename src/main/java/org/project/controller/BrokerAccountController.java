package org.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project.dto.BrokerAccountDTO;
import org.project.service.BrokerAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class BrokerAccountController {

    private final BrokerAccountService brokerAccountService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BrokerAccountDTO>> getAllBrokerAccounts() {
        return ResponseEntity.ok(brokerAccountService.getAllBrokerAccounts());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @clientSecurityService.isAccountOwner(#id, authentication)")
    public ResponseEntity<BrokerAccountDTO> getBrokerAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(brokerAccountService.getBrokerAccountById(id));
    }

    @GetMapping("/client/{clientId}")
    @PreAuthorize("hasRole('ADMIN') or @clientSecurityService.isClientOwner(#clientId, authentication)")
    public ResponseEntity<List<BrokerAccountDTO>> getBrokerAccountsByClientId(@PathVariable Long clientId) {
        return ResponseEntity.ok(brokerAccountService.getBrokerAccountsByClientId(clientId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or @clientSecurityService.isClientOwner(#brokerAccountDTO.clientId, authentication)")
    public ResponseEntity<BrokerAccountDTO> createBrokerAccount(
            @Valid @RequestBody BrokerAccountDTO brokerAccountDTO) {
        return new ResponseEntity<>(brokerAccountService.createBrokerAccount(brokerAccountDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @clientSecurityService.isAccountOwner(#id, authentication)")
    public ResponseEntity<BrokerAccountDTO> updateBrokerAccount(
            @PathVariable Long id,
            @Valid @RequestBody BrokerAccountDTO brokerAccountDTO) {
        return ResponseEntity.ok(brokerAccountService.updateBrokerAccount(id, brokerAccountDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @clientSecurityService.isAccountOwner(#id, authentication)")
    public ResponseEntity<Void> deleteBrokerAccount(@PathVariable Long id) {
        brokerAccountService.deleteBrokerAccount(id);
        return ResponseEntity.noContent().build();
    }
}