package com.goats.api.service;

import com.goats.api.dto.CommentReplyDto;
import com.goats.api.exception.ResourceNotFoundException;
import com.goats.api.model.Comment;
import com.goats.api.model.CommentReply;
import com.goats.api.model.User;
import com.goats.api.repository.CommentReplyRepository;
import com.goats.api.repository.CommentRepository;
import com.goats.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests para CommentReplyService
 */
@ExtendWith(MockitoExtension.class)
class CommentReplyServiceTest {

    @Mock
    private CommentReplyRepository commentReplyRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CommentReplyService commentReplyService;

    private Comment comment;
    private User user;
    private CommentReply commentReply;

    @BeforeEach
    void setUp() {
        // Setup usuario
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        // Setup comentario
        comment = new Comment();
        comment.setId(1L);
        comment.setStatus(com.goats.api.model.ModerationStatus.APPROVED);

        // Setup respuesta
        commentReply = new CommentReply();
        commentReply.setId(1L);
        commentReply.setComment(comment);
        commentReply.setUser(user);
        commentReply.setContent("Esta es una respuesta de prueba");
        commentReply.setCreatedAt(LocalDateTime.now());
        commentReply.setIsDeleted(false);
    }

    @Test
    void createReply_Success() {
        // Arrange
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(commentReplyRepository.save(any(CommentReply.class))).thenReturn(commentReply);

        // Act
        CommentReplyDto result = commentReplyService.createReply(1L, "testuser", "Nueva respuesta");

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Esta es una respuesta de prueba", result.getContent());
        verify(commentReplyRepository, times(1)).save(any(CommentReply.class));
    }

    @Test
    void createReply_CommentNotFound_ThrowsException() {
        // Arrange
        when(commentRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            commentReplyService.createReply(999L, "testuser", "Nueva respuesta")
        );
    }

    @Test
    void createReply_CommentNotApproved_ThrowsException() {
        // Arrange
        comment.setStatus(com.goats.api.model.ModerationStatus.PENDING);
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> 
            commentReplyService.createReply(1L, "testuser", "Nueva respuesta")
        );
    }

    @Test
    void createReply_UserNotFound_ThrowsException() {
        // Arrange
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            commentReplyService.createReply(1L, "nonexistent", "Nueva respuesta")
        );
    }

    @Test
    void getRepliesByCommentId_Success() {
        // Arrange
        List<CommentReply> replies = Arrays.asList(commentReply);
        when(commentRepository.existsById(1L)).thenReturn(true);
        when(commentReplyRepository.findByCommentIdAndNotDeleted(1L)).thenReturn(replies);

        // Act
        List<CommentReplyDto> result = commentReplyService.getRepliesByCommentId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Esta es una respuesta de prueba", result.get(0).getContent());
    }

    @Test
    void getRepliesByCommentId_CommentNotFound_ThrowsException() {
        // Arrange
        when(commentRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            commentReplyService.getRepliesByCommentId(999L)
        );
    }

    @Test
    void getRepliesByUserId_Success() {
        // Arrange
        List<CommentReply> replies = Arrays.asList(commentReply);
        when(userRepository.existsById(1L)).thenReturn(true);
        when(commentReplyRepository.findByUserIdAndNotDeleted(1L)).thenReturn(replies);

        // Act
        List<CommentReplyDto> result = commentReplyService.getRepliesByUserId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getUserId());
    }

    @Test
    void getReplyById_Success() {
        // Arrange
        when(commentReplyRepository.findById(1L)).thenReturn(Optional.of(commentReply));

        // Act
        CommentReplyDto result = commentReplyService.getReplyById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Esta es una respuesta de prueba", result.getContent());
    }

    @Test
    void getReplyById_NotFound_ThrowsException() {
        // Arrange
        when(commentReplyRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            commentReplyService.getReplyById(999L)
        );
    }

    @Test
    void getReplyById_Deleted_ThrowsException() {
        // Arrange
        commentReply.setIsDeleted(true);
        when(commentReplyRepository.findById(1L)).thenReturn(Optional.of(commentReply));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            commentReplyService.getReplyById(1L)
        );
    }

    @Test
    void updateReply_Success() {
        // Arrange
        String newContent = "Contenido actualizado";
        when(commentReplyRepository.findById(1L)).thenReturn(Optional.of(commentReply));
        when(commentReplyRepository.save(any(CommentReply.class))).thenReturn(commentReply);

        // Act
        CommentReplyDto result = commentReplyService.updateReply(1L, "testuser", newContent);

        // Assert
        assertNotNull(result);
        verify(commentReplyRepository, times(1)).save(any(CommentReply.class));
    }

    @Test
    void updateReply_NotOwner_ThrowsException() {
        // Arrange
        when(commentReplyRepository.findById(1L)).thenReturn(Optional.of(commentReply));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> 
            commentReplyService.updateReply(1L, "otheruser", "Nuevo contenido")
        );
    }

    @Test
    void deleteReply_ByOwner_Success() {
        // Arrange
        when(commentReplyRepository.findById(1L)).thenReturn(Optional.of(commentReply));
        when(commentReplyRepository.save(any(CommentReply.class))).thenReturn(commentReply);

        // Act
        commentReplyService.deleteReply(1L, "testuser", false);

        // Assert
        verify(commentReplyRepository, times(1)).save(any(CommentReply.class));
    }

    @Test
    void deleteReply_ByAdmin_Success() {
        // Arrange
        when(commentReplyRepository.findById(1L)).thenReturn(Optional.of(commentReply));
        when(commentReplyRepository.save(any(CommentReply.class))).thenReturn(commentReply);

        // Act
        commentReplyService.deleteReply(1L, "adminuser", true);

        // Assert
        verify(commentReplyRepository, times(1)).save(any(CommentReply.class));
    }

    @Test
    void deleteReply_NotOwnerNotAdmin_ThrowsException() {
        // Arrange
        when(commentReplyRepository.findById(1L)).thenReturn(Optional.of(commentReply));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> 
            commentReplyService.deleteReply(1L, "otheruser", false)
        );
    }

    @Test
    void countRepliesByCommentId_Success() {
        // Arrange
        when(commentReplyRepository.countByCommentIdAndNotDeleted(1L)).thenReturn(5L);

        // Act
        Long count = commentReplyService.countRepliesByCommentId(1L);

        // Assert
        assertEquals(5L, count);
    }

    @Test
    void hasUserReplied_True() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(commentReplyRepository.existsByCommentIdAndUserId(1L, 1L)).thenReturn(true);

        // Act
        Boolean hasReplied = commentReplyService.hasUserReplied(1L, "testuser");

        // Assert
        assertTrue(hasReplied);
    }

    @Test
    void hasUserReplied_False() {
        // Arrange
        User otherUser = new User();
        otherUser.setId(999L);
        otherUser.setUsername("otheruser");
        when(userRepository.findByUsername("otheruser")).thenReturn(Optional.of(otherUser));
        when(commentReplyRepository.existsByCommentIdAndUserId(1L, 999L)).thenReturn(false);

        // Act
        Boolean hasReplied = commentReplyService.hasUserReplied(1L, "otheruser");

        // Assert
        assertFalse(hasReplied);
    }
}
