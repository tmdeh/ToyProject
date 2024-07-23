package com.fc.toyproeject2.domain.itinerary.controller;

import com.fc.toyproeject2.domain.auth.model.userdetails.CustomUserDetails;
import com.fc.toyproeject2.domain.itinerary.model.request.BaseCampRequest;
import com.fc.toyproeject2.domain.itinerary.service.BaseCampService;
import com.fc.toyproeject2.global.util.APIUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/itinerary/basecamp")
public class BaseCampController {

    private final BaseCampService baseCampService;

    @PostMapping("/{tripId}")
    public ResponseEntity createBaseCamp(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @PathVariable(name = "tripId") Long tripId,
        @RequestBody BaseCampRequest baseCampRequestDTO) {
        baseCampService.saveBaseCamp(tripId, baseCampRequestDTO, customUserDetails);
        return APIUtil.OK("숙박 일정이 저장되었습니다.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteBaseCamp(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @PathVariable(name = "id") Long id) {
        baseCampService.deleteBaseCamp(id, customUserDetails);
        return APIUtil.OK("숙박 일정이 삭제되었습니다.");
    }

    @PutMapping("/{id}")
    public ResponseEntity updateBaseCamp(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @PathVariable(name = "id") Long id, @RequestBody BaseCampRequest baseCampRequest) {
        baseCampService.updateBaseCamp(id, baseCampRequest, customUserDetails);
        return APIUtil.OK("숙박 일정이 업데이트되었습니다.");
    }
}
