package com.fc.toyproeject2.domain.register.model.request;

public record SingUpRequest(
        String email,
        String password,
        String successKey,
        String name,
        String phoneNumber

) {
}
