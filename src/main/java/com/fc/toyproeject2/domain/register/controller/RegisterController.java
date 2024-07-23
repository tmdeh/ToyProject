package com.fc.toyproeject2.domain.register.controller;

import com.fc.toyproeject2.domain.register.model.request.EmailCheckRequest;
import com.fc.toyproeject2.domain.register.model.request.EmailSendRequest;
import com.fc.toyproeject2.domain.register.model.request.SingUpRequest;
import com.fc.toyproeject2.domain.register.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sign-up")
public class RegisterController {


    private final RegisterService registerService;

    @PostMapping
    public ResponseEntity singUp(
            @RequestBody SingUpRequest request
    ) {
        return registerService.singUp(request);
    }

    @PostMapping("/email-send")
    public ResponseEntity emailSend(
            @RequestBody EmailSendRequest request
    ) {
        return registerService.emailSend(request);
    }
    @PostMapping("/email-check")
    public ResponseEntity emailCheck(
            @RequestBody EmailCheckRequest request
    ) {
        return registerService.emailCheck(request);
    }
}
