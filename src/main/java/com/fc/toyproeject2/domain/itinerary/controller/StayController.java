package com.fc.toyproeject2.domain.itinerary.controller;

import com.fc.toyproeject2.domain.auth.model.userdetails.CustomUserDetails;
import com.fc.toyproeject2.domain.itinerary.model.request.StayRequest;
import com.fc.toyproeject2.domain.itinerary.service.StayService;
import com.fc.toyproeject2.global.util.APIUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/itinerary/stay")
public class StayController {

    private final StayService stayService;

    @PostMapping("/{tripId}")
    public ResponseEntity createStay(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @PathVariable(name = "tripId") Long tripId,
        @RequestBody StayRequest stayRequest) {
        stayService.saveStay(tripId, stayRequest, customUserDetails);
        return APIUtil.OK("체류 일정이 저장되었습니다.");
    }

    @PutMapping("/{id}")
    public ResponseEntity updateStay(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @PathVariable(name = "id") Long id, @RequestBody StayRequest stayRequest) {
        stayService.updateStay(id, stayRequest, customUserDetails);
        return APIUtil.OK("체류 일정이 업데이트되었습니다.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteStay(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @PathVariable(name = "id") Long id) {
        stayService.deleteStay(id, customUserDetails);
        return APIUtil.OK("체류 일정이 삭제되었습니다.");
    }

}
