package com.rideshare.rating.dto;

import jakarta.validation.constraints.*;

public record SignalementRequest(
        @NotBlank @Pattern(regexp = "^[0-9a-fA-F]{24}$", message = "ObjectId invalide") String idCible,
        @NotBlank @Size(min = 5, max = 1000) String motif
) {}
