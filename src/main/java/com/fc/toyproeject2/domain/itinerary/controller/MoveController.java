package com.fc.toyproeject2.domain.itinerary.controller;

import com.fc.toyproeject2.domain.auth.model.userdetails.CustomUserDetails;
import com.fc.toyproeject2.domain.itinerary.model.request.MoveRequest;
import com.fc.toyproeject2.domain.itinerary.service.MoveService;
import com.fc.toyproeject2.global.util.APIUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/itinerary/move")
public class MoveController {

    private final MoveService moveService;

    @PostMapping("/{tripId}")
    public ResponseEntity createMove(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @PathVariable(name = "tripId") Long tripId,
        @RequestBody MoveRequest moveRequest) {
        moveService.saveMove(tripId, moveRequest, customUserDetails);
        return APIUtil.OK("이동 일정이 저장되었습니다.");
    }

    @PutMapping("/{id}")
    public ResponseEntity updateMove(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @PathVariable(name = "id") Long id, @RequestBody MoveRequest moveRequest) {
        moveService.updateMove(id, moveRequest, customUserDetails);
        return APIUtil.OK("이동 일정이 업데이트되었습니다.");
    }


    @DeleteMapping("/{id}")
    public ResponseEntity deleteMove(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @PathVariable(name = "id") Long id) {
        moveService.deleteMove(id, customUserDetails);
        return APIUtil.OK("이동 일정이 삭제되었습니다.");
    }

}
