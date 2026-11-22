package com.rideshare.sos.controller;

import com.rideshare.sos.dto.request.MecanicienRequest;
import com.rideshare.sos.dto.response.MecanicienResponse;
import com.rideshare.sos.service.MecanicienService;
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
@RequestMapping("/api/mecaniciens")
@Tag(name = "Mecaniciens", description = "Manage mechanics")
public class MecanicienController {

    private final MecanicienService service;

    public MecanicienController(MecanicienService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Create a mechanic")
    @ApiResponse(responseCode = "201", description = "Mechanic created")
    @ApiResponse(responseCode = "400", description = "Invalid input",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public ResponseEntity<MecanicienResponse> create(@Valid @RequestBody MecanicienRequest request) {
        MecanicienResponse created = service.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping
    @Operation(summary = "List all mechanics")
    public List<MecanicienResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a mechanic by id")
    @ApiResponse(responseCode = "404", description = "Mechanic not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public MecanicienResponse findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Replace a mechanic (full update)")
    @ApiResponse(responseCode = "400", description = "Invalid input",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Mechanic not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public MecanicienResponse update(@PathVariable Long id, @Valid @RequestBody MecanicienRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a mechanic")
    @ApiResponse(responseCode = "204", description = "Deleted")
    @ApiResponse(responseCode = "404", description = "Mechanic not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "409", description = "Mechanic still referenced",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}