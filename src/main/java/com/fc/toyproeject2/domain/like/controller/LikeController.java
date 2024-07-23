package com.fc.toyproeject2.domain.like.controller;

import com.fc.toyproeject2.domain.auth.model.userdetails.CustomUserDetails;
import com.fc.toyproeject2.domain.like.service.LikeService;
import com.fc.toyproeject2.global.util.APIUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/like")
public class LikeController {

  private final LikeService likeService;

  @PostMapping("/{tripId}")
  public ResponseEntity<?> likeTrip(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      @PathVariable Long tripId) {
    likeService.likeTrip(customUserDetails, tripId);
    return APIUtil.OK();
  }

  @GetMapping("/count/{tripId}")
  public ResponseEntity<?> countLikes(@PathVariable Long tripId) {
    return APIUtil.OK(likeService.countLikes(tripId));
  }
}
