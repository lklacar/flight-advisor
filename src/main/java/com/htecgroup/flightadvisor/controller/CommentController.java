package com.htecgroup.flightadvisor.controller;

import com.htecgroup.flightadvisor.service.CommentService;
import com.htecgroup.flightadvisor.service.dto.CommentDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/api/cities/{cityId}/comments")
    public ResponseEntity<CommentDto> createComment(@PathVariable Long cityId, @RequestBody @Valid CommentDto comment) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.createComment(cityId, comment));
    }

    @DeleteMapping("/api/comments/{commentId}")
    public ResponseEntity<CommentDto> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PutMapping("/api/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable Long commentId,
            @RequestBody @Valid CommentDto comment
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(commentService.updateComment(commentId, comment));
    }
}
