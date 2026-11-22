package com.rideshare.rating.dto;

import jakarta.validation.constraints.*;

public record EvaluationUpdateRequest(
        @Min(1) @Max(5) int note,
        @Size(max = 500) String commentaire
) {}
