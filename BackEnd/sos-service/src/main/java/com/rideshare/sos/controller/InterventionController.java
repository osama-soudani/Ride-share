package com.rideshare.sos.controller;

import com.rideshare.sos.dto.request.InterventionRequest;
import com.rideshare.sos.dto.response.InterventionResponse;
import com.rideshare.sos.service.InterventionService;
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
@RequestMapping("/api/interventions")
@Tag(name = "Interventions", description = "Repairs performed by mechanics")
public class InterventionController {

    private final InterventionService service;

    public InterventionController(InterventionService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Create an intervention", description = "At most one intervention per SOS request.")
    @ApiResponse(responseCode = "201", description = "Intervention created")
    @ApiResponse(responseCode = "400", description = "Invalid input",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Request or mechanic not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "409", description = "Request already has an intervention",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public ResponseEntity<InterventionResponse> create(@Valid @RequestBody InterventionRequest request) {
        InterventionResponse created = service.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping
    @Operation(summary = "List all interventions")
    public List<InterventionResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an intervention by id")
    @ApiResponse(responseCode = "404", description = "Intervention not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public InterventionResponse findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping("/demande/{idDemande}")
    @Operation(summary = "Get the intervention of an SOS request")
    @ApiResponse(responseCode = "404", description = "No intervention for this request",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public InterventionResponse findByDemande(@PathVariable Long idDemande) {
        return service.findByDemande(idDemande);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Replace an intervention (full update)")
    @ApiResponse(responseCode = "400", description = "Invalid input",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Intervention not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "409", description = "Request already has an intervention",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public InterventionResponse update(@PathVariable Long id, @Valid @RequestBody InterventionRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an intervention")
    @ApiResponse(responseCode = "204", description = "Deleted")
    @ApiResponse(responseCode = "404", description = "Intervention not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}