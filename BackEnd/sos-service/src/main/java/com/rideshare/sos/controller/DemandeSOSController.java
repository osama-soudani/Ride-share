package com.rideshare.sos.controller;

import com.rideshare.sos.dto.request.DemandeSOSRequest;
import com.rideshare.sos.dto.request.DemandeSOSUpdateRequest;
import com.rideshare.sos.dto.response.DemandeSOSResponse;
import com.rideshare.sos.service.DemandeSOSService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/demandes-sos")
@Tag(name = "Demandes SOS", description = "Manage roadside assistance requests")
public class DemandeSOSController {

    private final DemandeSOSService service;

    public DemandeSOSController(DemandeSOSService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Create an SOS request", description = "Status starts at EN_ATTENTE.")
    @ApiResponse(responseCode = "201", description = "Request created")
    @ApiResponse(responseCode = "400", description = "Invalid input",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public ResponseEntity<DemandeSOSResponse> create(@Valid @RequestBody DemandeSOSRequest request) {
        DemandeSOSResponse created = service.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping
    @Operation(summary = "List all SOS requests")
    public List<DemandeSOSResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an SOS request by id")
    @ApiResponse(responseCode = "404", description = "Request not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public DemandeSOSResponse findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping("/chauffeur/{idChauffeur}")
    @Operation(summary = "List the SOS requests of a driver")
    public List<DemandeSOSResponse> findByChauffeur(@PathVariable String idChauffeur) {
        return service.findByChauffeur(idChauffeur);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Replace the editable fields of an SOS request")
    @ApiResponse(responseCode = "400", description = "Invalid input",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Request or mechanic not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public DemandeSOSResponse update(@PathVariable Long id, @Valid @RequestBody DemandeSOSUpdateRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an SOS request")
    @ApiResponse(responseCode = "204", description = "Deleted")
    @ApiResponse(responseCode = "404", description = "Request not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "409", description = "Request already has an intervention",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}