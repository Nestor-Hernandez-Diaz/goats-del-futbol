package com.goats.api.dto;

import com.goats.api.model.AchievementType;
import com.goats.api.model.ModerationStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para validar DTOs (constructores, getters/setters)
 * Ayuda a aumentar coverage de package dto
 */
class DtoTests {

    // ==================== PlayerDto Tests ====================
    @Test
    void testPlayerDto_NoArgsConstructor() {
        PlayerDto dto = new PlayerDto();
        assertNotNull(dto);
    }

    @Test
    void testPlayerDto_GettersSetters() {
        PlayerDto dto = new PlayerDto();
        dto.setId(1L);
        dto.setName("Lionel Messi");
        dto.setNickname("Leo");
        dto.setCountry("Argentina");
        dto.setPosition("Delantero");
        dto.setBiography("El mejor jugador");

        assertEquals(1L, dto.getId());
        assertEquals("Lionel Messi", dto.getName());
        assertEquals("Leo", dto.getNickname());
        assertEquals("Argentina", dto.getCountry());
        assertEquals("Delantero", dto.getPosition());
        assertEquals("El mejor jugador", dto.getBiography());
    }

    // ==================== PlayerStatsDto Tests ====================
    @Test
    void testPlayerStatsDto_NoArgsConstructor() {
        PlayerStatsDto dto = new PlayerStatsDto();
        assertNotNull(dto);
        assertEquals(0, dto.getGoals());
        assertEquals(0, dto.getAssists());
    }

    @Test
    void testPlayerStatsDto_AllArgsConstructor() {
        PlayerStatsDto dto = new PlayerStatsDto(
                1L, 10L, 850, 400, 1050, 45, 95, 3, 90000.0, 8, 4, 1
        );

        assertEquals(1L, dto.getId());
        assertEquals(10L, dto.getPlayerId());
        assertEquals(850, dto.getGoals());
        assertEquals(400, dto.getAssists());
        assertEquals(1050, dto.getMatchesPlayed());
        assertEquals(45, dto.getTrophies());
        assertEquals(95, dto.getYellowCards());
        assertEquals(3, dto.getRedCards());
        assertEquals(90000.0, dto.getMinutesPlayed());
        assertEquals(8, dto.getBallonDOrWins());
        assertEquals(4, dto.getChampionsLeagueWins());
        assertEquals(1, dto.getWorldCupWins());
    }

    @Test
    void testPlayerStatsDto_Setters() {
        PlayerStatsDto dto = new PlayerStatsDto();
        dto.setId(2L);
        dto.setPlayerId(20L);
        dto.setGoals(700);
        dto.setAssists(300);
        dto.setMatchesPlayed(900);
        dto.setTrophies(35);
        dto.setYellowCards(80);
        dto.setRedCards(2);
        dto.setMinutesPlayed(80000.0);
        dto.setBallonDOrWins(5);
        dto.setChampionsLeagueWins(5);
        dto.setWorldCupWins(0);

        assertEquals(2L, dto.getId());
        assertEquals(20L, dto.getPlayerId());
        assertEquals(700, dto.getGoals());
        assertEquals(300, dto.getAssists());
    }

    // ==================== AchievementDto Tests ====================
    @Test
    void testAchievementDto_NoArgsConstructor() {
        AchievementDto dto = new AchievementDto();
        assertNotNull(dto);
    }

    @Test
    void testAchievementDto_AllArgsConstructor() {
        AchievementDto dto = new AchievementDto(
                1L, 10L, "Copa del Mundo", "Campeón mundial",
                2022, AchievementType.NATIONAL_TEAM, "FIFA"
        );

        assertEquals(1L, dto.getId());
        assertEquals(10L, dto.getPlayerId());
        assertEquals("Copa del Mundo", dto.getTitle());
        assertEquals("Campeón mundial", dto.getDescription());
        assertEquals(2022, dto.getYear());
        assertEquals(AchievementType.NATIONAL_TEAM, dto.getType());
        assertEquals("FIFA", dto.getOrganization());
    }

    @Test
    void testAchievementDto_Setters() {
        AchievementDto dto = new AchievementDto();
        dto.setId(5L);
        dto.setPlayerId(15L);
        dto.setTitle("Champions League");
        dto.setDescription("Campeón de Europa");
        dto.setYear(2015);
        dto.setType(AchievementType.CLUB);
        dto.setOrganization("UEFA");

        assertEquals(5L, dto.getId());
        assertEquals(15L, dto.getPlayerId());
        assertEquals("Champions League", dto.getTitle());
        assertEquals(AchievementType.CLUB, dto.getType());
    }

    // ==================== CommentDto Tests ====================
    @Test
    void testCommentDto_NoArgsConstructor() {
        CommentDto dto = new CommentDto();
        assertNotNull(dto);
    }

    @Test
    void testCommentDto_AllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        CommentDto dto = new CommentDto(
                1L, 10L, "testuser", 20L, "Lionel Messi",
                "Gran jugador", ModerationStatus.APPROVED,
                null, null, null, now, now
        );

        assertEquals(1L, dto.getId());
        assertEquals(10L, dto.getUserId());
        assertEquals("testuser", dto.getUsername());
        assertEquals(20L, dto.getPlayerId());
        assertEquals("Lionel Messi", dto.getPlayerName());
        assertEquals("Gran jugador", dto.getContent());
        assertEquals(ModerationStatus.APPROVED, dto.getStatus());
        assertEquals(now, dto.getCreatedAt());
        assertEquals(now, dto.getUpdatedAt());
    }

    @Test
    void testCommentDto_Setters() {
        LocalDateTime created = LocalDateTime.now().minusDays(1);
        LocalDateTime updated = LocalDateTime.now();
        
        CommentDto dto = new CommentDto();
        dto.setId(3L);
        dto.setUserId(30L);
        dto.setUsername("admin");
        dto.setPlayerId(40L);
        dto.setPlayerName("Cristiano Ronaldo");
        dto.setContent("Increíble");
        dto.setStatus(ModerationStatus.PENDING);
        dto.setCreatedAt(created);
        dto.setUpdatedAt(updated);

        assertEquals(3L, dto.getId());
        assertEquals(30L, dto.getUserId());
        assertEquals("admin", dto.getUsername());
        assertEquals("Increíble", dto.getContent());
        assertEquals(ModerationStatus.PENDING, dto.getStatus());
    }

    // ==================== UserResponse Tests ====================
    @Test
    void testUserResponse_NoArgsConstructor() {
        UserResponse dto = new UserResponse();
        assertNotNull(dto);
    }

    @Test
    void testUserResponse_AllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        Set<String> roles = Set.of("ROLE_USER", "ROLE_ADMIN");
        
        UserResponse dto = new UserResponse(
                1L, "testuser", "test@test.com", true, now, roles
        );

        assertEquals(1L, dto.getId());
        assertEquals("testuser", dto.getUsername());
        assertEquals("test@test.com", dto.getEmail());
        assertTrue(dto.getEnabled());
        assertEquals(now, dto.getCreatedAt());
        assertEquals(2, dto.getRoles().size());
    }

    @Test
    void testUserResponse_Setters() {
        LocalDateTime created = LocalDateTime.now();
        Set<String> roles = Set.of("ROLE_USER");
        
        UserResponse dto = new UserResponse();
        dto.setId(5L);
        dto.setUsername("newuser");
        dto.setEmail("new@test.com");
        dto.setEnabled(false);
        dto.setCreatedAt(created);
        dto.setRoles(roles);

        assertEquals(5L, dto.getId());
        assertEquals("newuser", dto.getUsername());
        assertEquals("new@test.com", dto.getEmail());
        assertFalse(dto.getEnabled());
        assertEquals(1, dto.getRoles().size());
    }

    // ==================== AuthResponse Tests ====================
    @Test
    void testAuthResponse_NoArgsConstructor() {
        AuthResponse dto = new AuthResponse();
        assertNotNull(dto);
        assertEquals("Bearer", dto.getType());
    }

    @Test
    void testAuthResponse_AllArgsConstructor() {
        Set<String> roles = Set.of("ROLE_USER");
        AuthResponse dto = new AuthResponse(
                "jwt-token-123", 1L, "testuser", "test@test.com", roles
        );

        assertEquals("jwt-token-123", dto.getToken());
        assertEquals("Bearer", dto.getType());
        assertEquals(1L, dto.getId());
        assertEquals("testuser", dto.getUsername());
        assertEquals("test@test.com", dto.getEmail());
        assertEquals(1, dto.getRoles().size());
    }

    @Test
    void testAuthResponse_Setters() {
        Set<String> roles = Set.of("ROLE_ADMIN");
        AuthResponse dto = new AuthResponse();
        dto.setToken("new-token");
        dto.setType("Custom");
        dto.setId(10L);
        dto.setUsername("admin");
        dto.setEmail("admin@test.com");
        dto.setRoles(roles);

        assertEquals("new-token", dto.getToken());
        assertEquals("Custom", dto.getType());
        assertEquals(10L, dto.getId());
        assertEquals("admin", dto.getUsername());
        assertTrue(dto.getRoles().contains("ROLE_ADMIN"));
    }

    // ==================== RegisterRequest Tests ====================
    @Test
    void testRegisterRequest_GettersSetters() {
        RegisterRequest dto = new RegisterRequest();
        dto.setUsername("newuser");
        dto.setEmail("newuser@test.com");
        dto.setPassword("password123");

        assertEquals("newuser", dto.getUsername());
        assertEquals("newuser@test.com", dto.getEmail());
        assertEquals("password123", dto.getPassword());
    }

    // ==================== LoginRequest Tests ====================
    @Test
    void testLoginRequest_GettersSetters() {
        LoginRequest dto = new LoginRequest();
        dto.setUsername("testuser");
        dto.setPassword("testpass");

        assertEquals("testuser", dto.getUsername());
        assertEquals("testpass", dto.getPassword());
    }

    // ==================== SubscriptionDto Tests ====================
    @Test
    void testSubscriptionDto_NoArgsConstructor() {
        SubscriptionDto dto = new SubscriptionDto();
        assertNotNull(dto);
    }

    @Test
    void testSubscriptionDto_AllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        SubscriptionDto dto = new SubscriptionDto(
                1L, 10L, "testuser", 20L, "Lionel Messi",
                true, true, now, null
        );

        assertEquals(1L, dto.getId());
        assertEquals(10L, dto.getUserId());
        assertEquals("testuser", dto.getUsername());
        assertEquals(20L, dto.getPlayerId());
        assertEquals("Lionel Messi", dto.getPlayerName());
        assertTrue(dto.getActive());
        assertTrue(dto.getNotificationsEnabled());
        assertEquals(now, dto.getSubscribedAt());
    }

    @Test
    void testSubscriptionDto_Setters() {
        LocalDateTime subscribed = LocalDateTime.now();
        SubscriptionDto dto = new SubscriptionDto();
        dto.setId(3L);
        dto.setUserId(30L);
        dto.setUsername("fan1");
        dto.setPlayerId(40L);
        dto.setPlayerName("Neymar Jr");
        dto.setActive(true);
        dto.setNotificationsEnabled(false);
        dto.setSubscribedAt(subscribed);
        dto.setUnsubscribedAt(null);

        assertEquals(3L, dto.getId());
        assertEquals(30L, dto.getUserId());
        assertEquals("fan1", dto.getUsername());
        assertEquals(40L, dto.getPlayerId());
        assertEquals("Neymar Jr", dto.getPlayerName());
        assertTrue(dto.getActive());
        assertFalse(dto.getNotificationsEnabled());
        assertEquals(subscribed, dto.getSubscribedAt());
        assertNull(dto.getUnsubscribedAt());
    }
}
