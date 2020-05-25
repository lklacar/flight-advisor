package com.htecgroup.flightadvisor.service;

import com.htecgroup.flightadvisor.service.dto.CommentDto;

public interface CommentService {
    /**
     * Create a comment for a given city.
     *
     * @param cityId  City id of city for which the comment is intended
     * @param comment {@link CommentDto} object describing the comment that needs to be created
     * @return CommentDto created object
     */
    CommentDto createComment(Long cityId, CommentDto comment);

    /**
     * Delete a comment by comment id.
     *
     * @param commentId Id of the comment that needs to be deleted
     */
    void deleteComment(Long commentId);

    /**
     * Update a comment
     *
     * @param commentId Id of a comment that needs to be updated
     * @param comment   {@link CommentDto} object describing the comment that needs to be updated
     * @return Updated comment
     */
    CommentDto updateComment(Long commentId, CommentDto comment);
}
