package com.rideshare.rating.dto;

import jakarta.validation.constraints.*;

public record EvaluationRequest(
        @NotBlank @Pattern(regexp = "^[0-9a-fA-F]{24}$", message = "ObjectId invalide") String idCible,
        @NotBlank @Pattern(regexp = "^[0-9a-fA-F]{24}$", message = "ObjectId invalide") String idCourseRef,
        @Min(1) @Max(5) int note,
        @Size(max = 500) String commentaire
) {}
