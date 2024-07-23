package com.fc.toyproeject2.domain.register.model.request;

public record EmailCheckRequest(
        String email,
        String successKey
) {
}
