package com.htecgroup.flightadvisor.service.impl;

import com.htecgroup.flightadvisor.domain.ApplicationUser;
import com.htecgroup.flightadvisor.exception.NotFoundException;
import com.htecgroup.flightadvisor.repository.CityRepository;
import com.htecgroup.flightadvisor.repository.CommentRepository;
import com.htecgroup.flightadvisor.repository.UserRepository;
import com.htecgroup.flightadvisor.service.CommentService;
import com.htecgroup.flightadvisor.service.dto.CommentDto;
import com.htecgroup.flightadvisor.service.mapper.CommentMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CityRepository cityRepository;
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;

    public CommentServiceImpl(
            CommentRepository commentRepository,
            CityRepository cityRepository,
            CommentMapper commentMapper,
            UserRepository userRepository
    ) {
        this.commentRepository = commentRepository;
        this.cityRepository = cityRepository;
        this.commentMapper = commentMapper;
        this.userRepository = userRepository;
    }

    @Override
    public CommentDto createComment(Long cityId, CommentDto comment) {
        var city = cityRepository.findById(cityId)
                .orElseThrow(() -> new NotFoundException("City not found"));
        var commentEntity = commentMapper.fromDto(comment);
        commentEntity.setCity(city);
        commentEntity.setAuthor(getCurrentUser());
        commentEntity = commentRepository.save(commentEntity);
        return commentMapper.toDto(commentEntity);
    }

    @Override
    public void deleteComment(Long commentId) {
        var currentUser = getCurrentUser();
        var comment = commentRepository.findByIdAndAuthorId(commentId, currentUser.getId())
                .orElseThrow(() -> new NotFoundException("Comment not found"));
        commentRepository.delete(comment);
    }

    @Override
    public CommentDto updateComment(Long commentId, CommentDto commentDto) {
        var comment = commentRepository.findByIdAndAuthorId(commentId, getCurrentUser().getId())
                .orElseThrow(() -> new NotFoundException("Comment not found"));

        comment.setText(commentDto.getText());
        comment = commentRepository.save(comment);
        return commentMapper.toDto(comment);
    }

    private ApplicationUser getCurrentUser() {
        Object principal = SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        String username = ((UserDetails) principal).getUsername();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
}
