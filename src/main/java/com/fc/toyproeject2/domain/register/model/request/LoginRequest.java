package com.fc.toyproeject2.domain.register.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotNull
        @NotBlank
        String email,
        @NotNull
        @NotBlank
        String pw
) {
}
