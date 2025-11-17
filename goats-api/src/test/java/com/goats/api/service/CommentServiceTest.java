package com.goats.api.service;

import com.goats.api.dto.CommentDto;
import com.goats.api.model.Comment;
import com.goats.api.model.Comment.ModerationStatus;
import com.goats.api.model.Player;
import com.goats.api.model.User;
import com.goats.api.repository.CommentRepository;
import com.goats.api.repository.PlayerRepository;
import com.goats.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para CommentService
 */
@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private CommentService commentService;

    private User testUser;
    private User testModerator;
    private Player testPlayer;
    private Comment testComment;
    private CommentDto testCommentDto;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        // Setup test user
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@test.com");

        // Setup test moderator
        testModerator = new User();
        testModerator.setId(2L);
        testModerator.setUsername("admin");
        testModerator.setEmail("admin@test.com");

        // Setup test player
        testPlayer = new Player();
        testPlayer.setId(1L);
        testPlayer.setName("Lionel Messi");

        // Setup test comment
        testComment = new Comment();
        testComment.setId(1L);
        testComment.setUser(testUser);
        testComment.setPlayer(testPlayer);
        testComment.setContent("Great player!");
        testComment.setStatus(ModerationStatus.PENDING);
        testComment.setCreatedAt(LocalDateTime.now());

        // Setup test DTO
        testCommentDto = new CommentDto();
        testCommentDto.setPlayerId(1L);
        testCommentDto.setContent("Great player!");

        // Setup pageable
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void testGetAll_WithoutStatus_ReturnsAllComments() {
        // Given
        List<Comment> comments = Arrays.asList(testComment);
        Page<Comment> commentPage = new PageImpl<>(comments);
        when(commentRepository.findAll(pageable)).thenReturn(commentPage);

        // When
        Page<CommentDto> result = commentService.getAll(null, pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(commentRepository, times(1)).findAll(pageable);
        verify(commentRepository, never()).findByStatus(any(), any());
    }

    @Test
    void testGetAll_WithStatus_ReturnsFilteredComments() {
        // Given
        List<Comment> comments = Arrays.asList(testComment);
        Page<Comment> commentPage = new PageImpl<>(comments);
        when(commentRepository.findByStatus(ModerationStatus.PENDING, pageable)).thenReturn(commentPage);

        // When
        Page<CommentDto> result = commentService.getAll(ModerationStatus.PENDING, pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(commentRepository, times(1)).findByStatus(ModerationStatus.PENDING, pageable);
        verify(commentRepository, never()).findAll(any(Pageable.class));
    }

    @Test
    void testGetByPlayerId_WithoutStatus_ReturnsPlayerComments() {
        // Given
        List<Comment> comments = Arrays.asList(testComment);
        Page<Comment> commentPage = new PageImpl<>(comments);
        when(commentRepository.findByPlayerId(1L, pageable)).thenReturn(commentPage);

        // When
        Page<CommentDto> result = commentService.getByPlayerId(1L, null, pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(commentRepository, times(1)).findByPlayerId(1L, pageable);
    }

    @Test
    void testGetByPlayerId_WithStatus_ReturnsFilteredPlayerComments() {
        // Given
        testComment.setStatus(ModerationStatus.APPROVED);
        List<Comment> comments = Arrays.asList(testComment);
        Page<Comment> commentPage = new PageImpl<>(comments);
        when(commentRepository.findByPlayerIdAndStatus(1L, ModerationStatus.APPROVED, pageable))
                .thenReturn(commentPage);

        // When
        Page<CommentDto> result = commentService.getByPlayerId(1L, ModerationStatus.APPROVED, pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(commentRepository, times(1)).findByPlayerIdAndStatus(1L, ModerationStatus.APPROVED, pageable);
    }

    @Test
    void testGetByUserId_ReturnsUserComments() {
        // Given
        List<Comment> comments = Arrays.asList(testComment);
        Page<Comment> commentPage = new PageImpl<>(comments);
        when(commentRepository.findByUserId(1L, pageable)).thenReturn(commentPage);

        // When
        Page<CommentDto> result = commentService.getByUserId(1L, pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(commentRepository, times(1)).findByUserId(1L, pageable);
    }

    @Test
    void testGetById_Success() {
        // Given
        when(commentRepository.findById(1L)).thenReturn(Optional.of(testComment));

        // When
        CommentDto result = commentService.getById(1L);

        // Then
        assertNotNull(result);
        assertEquals(testComment.getContent(), result.getContent());
        verify(commentRepository, times(1)).findById(1L);
    }

    @Test
    void testGetById_NotFound_ThrowsException() {
        // Given
        when(commentRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> commentService.getById(999L));
        verify(commentRepository, times(1)).findById(999L);
    }

    @Test
    void testCreate_Success() {
        // Given
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(playerRepository.findById(1L)).thenReturn(Optional.of(testPlayer));
        when(commentRepository.save(any(Comment.class))).thenReturn(testComment);

        // When
        CommentDto result = commentService.create(testCommentDto, "testuser");

        // Then
        assertNotNull(result);
        assertEquals(testCommentDto.getContent(), result.getContent());
        verify(userRepository, times(1)).findByUsername("testuser");
        verify(playerRepository, times(1)).findById(1L);
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void testCreate_UserNotFound_ThrowsException() {
        // Given
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> 
            commentService.create(testCommentDto, "nonexistent"));
        verify(commentRepository, never()).save(any());
    }

    @Test
    void testCreate_PlayerNotFound_ThrowsException() {
        // Given
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(playerRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> 
            commentService.create(testCommentDto, "testuser"));
        verify(commentRepository, never()).save(any());
    }

    @Test
    void testApprove_Success() {
        // Given
        when(commentRepository.findById(1L)).thenReturn(Optional.of(testComment));
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(testModerator));
        when(commentRepository.save(any(Comment.class))).thenReturn(testComment);

        // When
        CommentDto result = commentService.approve(1L, "admin");

        // Then
        assertNotNull(result);
        verify(commentRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findByUsername("admin");
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void testReject_Success() {
        // Given
        when(commentRepository.findById(1L)).thenReturn(Optional.of(testComment));
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(testModerator));
        when(commentRepository.save(any(Comment.class))).thenReturn(testComment);

        // When
        CommentDto result = commentService.reject(1L, "Inappropriate content", "admin");

        // Then
        assertNotNull(result);
        verify(commentRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findByUsername("admin");
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void testDelete_Success() {
        // Given
        when(commentRepository.findById(1L)).thenReturn(Optional.of(testComment));
        doNothing().when(commentRepository).delete(any(Comment.class));

        // When
        commentService.delete(1L, "testuser", false);

        // Then
        verify(commentRepository, times(1)).findById(1L);
        verify(commentRepository, times(1)).delete(any(Comment.class));
    }

    @Test
    void testDelete_Unauthorized_ThrowsException() {
        // Given
        when(commentRepository.findById(1L)).thenReturn(Optional.of(testComment));

        // When & Then
        assertThrows(RuntimeException.class, () -> 
            commentService.delete(1L, "otheruser", false));
        verify(commentRepository, never()).delete(any());
    }

    @Test
    void testDelete_AsAdmin_Success() {
        // Given
        when(commentRepository.findById(1L)).thenReturn(Optional.of(testComment));
        doNothing().when(commentRepository).delete(any(Comment.class));

        // When
        commentService.delete(1L, "admin", true);

        // Then
        verify(commentRepository, times(1)).findById(1L);
        verify(commentRepository, times(1)).delete(any(Comment.class));
    }

    @Test
    void testGetPendingComments_Success() {
        // Given
        List<Comment> comments = Arrays.asList(testComment);
        when(commentRepository.findPendingComments(any(PageRequest.class))).thenReturn(comments);

        // When
        List<CommentDto> result = commentService.getPendingComments(10);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(commentRepository, times(1)).findPendingComments(any(PageRequest.class));
    }

    @Test
    void testCountByStatus_Success() {
        // Given
        when(commentRepository.countByStatus(ModerationStatus.PENDING)).thenReturn(5L);

        // When
        long result = commentService.countByStatus(ModerationStatus.PENDING);

        // Then
        assertEquals(5L, result);
        verify(commentRepository, times(1)).countByStatus(ModerationStatus.PENDING);
    }

    @Test
    void testGetRecentApprovedComments_Success() {
        // Given
        testComment.setStatus(ModerationStatus.APPROVED);
        List<Comment> comments = Arrays.asList(testComment);
        when(commentRepository.findRecentApprovedComments(eq(1L), any(PageRequest.class)))
                .thenReturn(comments);

        // When
        List<CommentDto> result = commentService.getRecentApprovedComments(1L, 10);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(commentRepository, times(1)).findRecentApprovedComments(eq(1L), any(PageRequest.class));
    }
}
