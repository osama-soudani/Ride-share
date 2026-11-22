package com.rideshare.rating.dto;

import jakarta.validation.constraints.*;

public record SignalementUpdateRequest(
        @NotBlank @Size(min = 5, max = 1000) String motif
) {}
