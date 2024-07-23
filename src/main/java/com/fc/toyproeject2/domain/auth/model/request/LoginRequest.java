package com.fc.toyproeject2.domain.auth.model.request;

public record LoginRequest(
    String email,
    String password
) {
}
