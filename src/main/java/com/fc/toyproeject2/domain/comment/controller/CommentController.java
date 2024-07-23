package com.fc.toyproeject2.domain.comment.controller;

import com.fc.toyproeject2.domain.auth.model.userdetails.CustomUserDetails;
import com.fc.toyproeject2.domain.comment.model.request.CommentRequest;
import com.fc.toyproeject2.domain.comment.model.request.UpdateCommentRequest;
import com.fc.toyproeject2.domain.comment.service.CommentService;
import com.fc.toyproeject2.global.util.APIUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity createComment(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody CommentRequest request
    ) {
        return APIUtil.OK(commentService.addComment(request, customUserDetails));
    }

    @GetMapping("/trip/{tripId}")
    public ResponseEntity getCommentsByTripId(@PathVariable Long tripId) {
        return APIUtil.OK(commentService.getCommentsByTripId(tripId));
    }

    @PutMapping("/{id}")
    public ResponseEntity updateComment(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userId, @RequestBody UpdateCommentRequest content) {
        return APIUtil.OK(commentService.updateComment(id, userId, content));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity deleteComment(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userId) {
        commentService.deleteComment(id, userId);
        return APIUtil.OK();
    }
}
