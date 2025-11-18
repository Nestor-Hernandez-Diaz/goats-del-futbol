package com.goats.api.model;

import com.goats.api.model.AchievementType;
import com.goats.api.model.ModerationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para Models (constructores, getters/setters, métodos de negocio)
 * Ayuda a aumentar coverage de package model
 */
class ModelTests {

    // ==================== Player Tests ====================
    @Test
    void testPlayer_GettersSetters() {
        Player player = new Player();
        player.setId(1L);
        player.setName("Lionel Messi");
        player.setNickname("Leo");
        player.setCountry("Argentina");
        player.setPosition("Delantero");
        player.setBiography("El mejor jugador del mundo");

        assertEquals(1L, player.getId());
        assertEquals("Lionel Messi", player.getName());
        assertEquals("Leo", player.getNickname());
        assertEquals("Argentina", player.getCountry());
        assertEquals("Delantero", player.getPosition());
        assertEquals("El mejor jugador del mundo", player.getBiography());
    }

    @Test
    void testPlayer_RelationshipsInitialization() {
        Player player = new Player();
        assertNotNull(player.getAchievements());
        assertNotNull(player.getComments());
        assertNotNull(player.getSubscriptions());
        assertTrue(player.getAchievements().isEmpty());
        assertTrue(player.getComments().isEmpty());
        assertTrue(player.getSubscriptions().isEmpty());
    }

    @Test
    void testPlayer_StatsRelationship() {
        Player player = new Player();
        PlayerStats stats = new PlayerStats(player);
        player.setStats(stats);

        assertNotNull(player.getStats());
        assertEquals(player, player.getStats().getPlayer());
    }

    @Test
    void testPlayer_AddRemoveAchievement() {
        Player player = new Player();
        player.setId(1L);
        Achievement achievement = new Achievement();
        achievement.setTitle("Ballon d'Or");
        
        player.addAchievement(achievement);
        assertTrue(player.getAchievements().contains(achievement));
        assertEquals(player, achievement.getPlayer());
        
        player.removeAchievement(achievement);
        assertFalse(player.getAchievements().contains(achievement));
        assertNull(achievement.getPlayer());
    }

    @Test
    void testPlayer_ToString() {
        Player player = new Player();
        player.setId(7L);
        player.setName("Cristiano Ronaldo");
        player.setCountry("Portugal");
        player.setPosition("Delantero");
        
        String result = player.toString();
        
        assertTrue(result.contains("id=7"));
        assertTrue(result.contains("name='Cristiano Ronaldo'"));
        assertTrue(result.contains("country='Portugal'"));
        assertTrue(result.contains("position='Delantero'"));
    }

    // ==================== PlayerStats Tests ====================
    @Test
    void testPlayerStats_Constructor() {
        Player player = new Player();
        player.setId(1L);
        
        PlayerStats stats = new PlayerStats(player);

        assertNotNull(stats);
        assertEquals(player, stats.getPlayer());
        assertEquals(0, stats.getGoals());
        assertEquals(0, stats.getAssists());
    }

    @Test
    void testPlayerStats_GettersSetters() {
        Player player = new Player();
        PlayerStats stats = new PlayerStats(player);
        
        stats.setId(1L);
        stats.setGoals(850);
        stats.setAssists(400);
        stats.setMatchesPlayed(1050);
        stats.setTrophies(45);
        stats.setYellowCards(95);
        stats.setRedCards(3);
        stats.setMinutesPlayed(90000.0);
        stats.setBallonDOrWins(8);
        stats.setChampionsLeagueWins(4);
        stats.setWorldCupWins(1);

        assertEquals(1L, stats.getId());
        assertEquals(850, stats.getGoals());
        assertEquals(400, stats.getAssists());
        assertEquals(1050, stats.getMatchesPlayed());
        assertEquals(45, stats.getTrophies());
        assertEquals(95, stats.getYellowCards());
        assertEquals(3, stats.getRedCards());
        assertEquals(90000.0, stats.getMinutesPlayed());
        assertEquals(8, stats.getBallonDOrWins());
        assertEquals(4, stats.getChampionsLeagueWins());
        assertEquals(1, stats.getWorldCupWins());
    }

    // ==================== Achievement Tests ====================
    @Test
    void testAchievement_GettersSetters() {
        Player player = new Player();
        player.setId(1L);
        
        Achievement achievement = new Achievement();
        achievement.setId(1L);
        achievement.setPlayer(player);
        achievement.setTitle("Copa del Mundo");
        achievement.setDescription("Campeón mundial con Argentina");
        achievement.setYear(2022);
        achievement.setType(AchievementType.NATIONAL_TEAM);
        achievement.setOrganization("FIFA");

        assertEquals(1L, achievement.getId());
        assertEquals(player, achievement.getPlayer());
        assertEquals("Copa del Mundo", achievement.getTitle());
        assertEquals("Campeón mundial con Argentina", achievement.getDescription());
        assertEquals(2022, achievement.getYear());
        assertEquals(AchievementType.NATIONAL_TEAM, achievement.getType());
        assertEquals("FIFA", achievement.getOrganization());
    }

    @Test
    void testAchievement_AllTypes() {
        Achievement club = new Achievement();
        club.setType(AchievementType.CLUB);
        assertEquals(AchievementType.CLUB, club.getType());

        Achievement individual = new Achievement();
        individual.setType(AchievementType.INDIVIDUAL);
        assertEquals(AchievementType.INDIVIDUAL, individual.getType());

        Achievement nationalTeam = new Achievement();
        nationalTeam.setType(AchievementType.NATIONAL_TEAM);
        assertEquals(AchievementType.NATIONAL_TEAM, nationalTeam.getType());

        Achievement record = new Achievement();
        record.setType(AchievementType.RECORD);
        assertEquals(AchievementType.RECORD, record.getType());

        Achievement other = new Achievement();
        other.setType(AchievementType.OTHER);
        assertEquals(AchievementType.OTHER, other.getType());
    }

    // ==================== Comment Tests ====================
    @Test
    void testComment_GettersSetters() {
        User user = new User();
        user.setId(1L);
        
        Player player = new Player();
        player.setId(10L);
        
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setUser(user);
        comment.setPlayer(player);
        comment.setContent("Gran jugador");
        comment.setStatus(ModerationStatus.PENDING);

        assertEquals(1L, comment.getId());
        assertEquals(user, comment.getUser());
        assertEquals(player, comment.getPlayer());
        assertEquals("Gran jugador", comment.getContent());
        assertEquals(ModerationStatus.PENDING, comment.getStatus());
        assertNull(comment.getCreatedAt()); // @PrePersist solo se ejecuta al persistir
        assertNull(comment.getUpdatedAt()); // @PreUpdate solo se ejecuta al actualizar
    }

    @Test
    void testComment_StatusApprove() {
        Comment comment = new Comment();
        comment.setStatus(ModerationStatus.PENDING);
        assertEquals(ModerationStatus.PENDING, comment.getStatus());

        comment.setStatus(ModerationStatus.APPROVED);
        assertEquals(ModerationStatus.APPROVED, comment.getStatus());
    }

    @Test
    void testComment_StatusReject() {
        Comment comment = new Comment();
        comment.setStatus(ModerationStatus.PENDING);

        comment.setStatus(ModerationStatus.REJECTED);
        assertEquals(ModerationStatus.REJECTED, comment.getStatus());
    }

    @Test
    void testComment_AllStatuses() {
        Comment pending = new Comment();
        pending.setStatus(ModerationStatus.PENDING);
        assertEquals(ModerationStatus.PENDING, pending.getStatus());

        Comment approved = new Comment();
        approved.setStatus(ModerationStatus.APPROVED);
        assertEquals(ModerationStatus.APPROVED, approved.getStatus());

        Comment rejected = new Comment();
        rejected.setStatus(ModerationStatus.REJECTED);
        assertEquals(ModerationStatus.REJECTED, rejected.getStatus());
        
        Comment edited = new Comment();
        edited.setStatus(ModerationStatus.EDITED);
        assertEquals(ModerationStatus.EDITED, edited.getStatus());
    }

    @Test
    void testComment_ModerationFields() {
        Comment comment = new Comment();
        User moderator = new User();
        moderator.setId(999L);
        LocalDateTime now = LocalDateTime.now();
        
        comment.setModerationReason("Contenido inapropiado");
        comment.setModeratedBy(moderator);
        comment.setModeratedAt(now);
        
        assertEquals("Contenido inapropiado", comment.getModerationReason());
        assertEquals(moderator, comment.getModeratedBy());
        assertEquals(now, comment.getModeratedAt());
    }

    @Test
    void testComment_Constructor() {
        User user = new User();
        user.setId(1L);
        Player player = new Player();
        player.setId(10L);
        
        Comment comment = new Comment(user, player, "Excelente jugador");
        
        assertEquals(user, comment.getUser());
        assertEquals(player, comment.getPlayer());
        assertEquals("Excelente jugador", comment.getContent());
    }

    @Test
    void testComment_ToString() {
        User user = new User();
        user.setId(5L);
        Player player = new Player();
        player.setId(3L);
        
        Comment comment = new Comment();
        comment.setId(100L);
        comment.setUser(user);
        comment.setPlayer(player);
        comment.setStatus(ModerationStatus.APPROVED);
        comment.setCreatedAt(LocalDateTime.now());
        
        String result = comment.toString();
        
        assertTrue(result.contains("id=100"));
        assertTrue(result.contains("userId=5"));
        assertTrue(result.contains("playerId=3"));
        assertTrue(result.contains("APPROVED"));
    }

    // ==================== User Tests ====================
    @Test
    void testUser_GettersSetters() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@test.com");
        user.setPasswordHash("hashedpassword");
        user.setEnabled(true);

        assertEquals(1L, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("test@test.com", user.getEmail());
        assertEquals("hashedpassword", user.getPasswordHash());
        assertTrue(user.getEnabled());
        assertNull(user.getCreatedAt()); // Solo se setea con @PrePersist
    }

    @Test
    void testUser_RolesRelationship() {
        User user = new User();
        Set<Role> roles = new HashSet<>();
        
        Role role1 = new Role();
        role1.setName("ROLE_USER");
        roles.add(role1);
        
        user.setRoles(roles);

        assertNotNull(user.getRoles());
        assertEquals(1, user.getRoles().size());
        assertTrue(user.getRoles().contains(role1));
    }

    @Test
    void testUser_AddRemoveRole() {
        User user = new User();
        user.setId(1L);
        Role role = new Role("ROLE_ADMIN");
        role.setUsers(new HashSet<>());
        
        user.addRole(role);
        assertTrue(user.getRoles().contains(role));
        assertTrue(role.getUsers().contains(user));
        
        user.removeRole(role);
        assertFalse(user.getRoles().contains(role));
        assertFalse(role.getUsers().contains(user));
    }

    @Test
    void testUser_Equals() {
        User user1 = new User();
        user1.setUsername("user1");
        
        User user2 = new User();
        user2.setUsername("user1");
        
        User user3 = new User();
        user3.setUsername("user3");
        
        assertEquals(user1, user2); // Mismo username
        assertNotEquals(user1, user3); // Diferente username
        assertEquals(user1, user1); // Mismo objeto
    }

    @Test
    void testUser_HashCode() {
        User user = new User();
        user.setUsername("testuser");
        
        int hashCode1 = user.hashCode();
        int hashCode2 = user.hashCode();
        
        assertEquals(hashCode1, hashCode2); // Consistente
    }

    @Test
    void testUser_ToString() {
        User user = new User();
        user.setId(10L);
        user.setUsername("johndoe");
        user.setEmail("john@example.com");
        user.setEnabled(true);
        user.setCreatedAt(LocalDateTime.now());
        
        String result = user.toString();
        
        assertTrue(result.contains("id=10"));
        assertTrue(result.contains("username='johndoe'"));
        assertTrue(result.contains("email='john@example.com'"));
        assertTrue(result.contains("enabled=true"));
    }

    // ==================== Role Tests ====================
    @Test
    void testRole_GettersSetters() {
        Role role = new Role();
        role.setId(1L);
        role.setName("ROLE_ADMIN");

        assertEquals(1L, role.getId());
        assertEquals("ROLE_ADMIN", role.getName());
    }

    @Test
    void testRole_Constructor() {
        Role role = new Role("ROLE_USER");
        assertEquals("ROLE_USER", role.getName());
    }

    @Test
    void testRole_UsersRelationship() {
        Role role = new Role();
        Set<User> users = new HashSet<>();
        
        User user1 = new User();
        user1.setUsername("admin1");
        users.add(user1);
        
        role.setUsers(users);

        assertNotNull(role.getUsers());
        assertEquals(1, role.getUsers().size());
        assertTrue(role.getUsers().contains(user1));
    }

    @Test
    void testRole_Equals() {
        Role role1 = new Role("ROLE_ADMIN");
        Role role2 = new Role("ROLE_ADMIN");
        Role role3 = new Role("ROLE_USER");
        
        assertEquals(role1, role2); // Mismo name
        assertNotEquals(role1, role3); // Diferente name
        assertEquals(role1, role1); // Mismo objeto
    }

    @Test
    void testRole_HashCode() {
        Role role = new Role("ROLE_MODERATOR");
        
        int hashCode1 = role.hashCode();
        int hashCode2 = role.hashCode();
        
        assertEquals(hashCode1, hashCode2); // Consistente
    }

    @Test
    void testRole_ToString() {
        Role role = new Role("ROLE_GUEST");
        role.setId(5L);
        
        String result = role.toString();
        
        assertTrue(result.contains("id=5"));
        assertTrue(result.contains("name='ROLE_GUEST'"));
    }

    // ==================== Subscription Tests ====================
    @Test
    void testSubscription_GettersSetters() {
        User user = new User();
        user.setId(1L);
        
        Player player = new Player();
        player.setId(10L);
        
        Subscription subscription = new Subscription();
        subscription.setId(1L);
        subscription.setUser(user);
        subscription.setPlayer(player);

        assertEquals(1L, subscription.getId());
        assertEquals(user, subscription.getUser());
        assertEquals(player, subscription.getPlayer());
        assertNull(subscription.getSubscribedAt()); // @PrePersist solo se ejecuta al persistir
    }

    @Test
    void testSubscription_SubscribedAtAutoSet() {
        Subscription subscription = new Subscription();
        LocalDateTime before = LocalDateTime.now().minusSeconds(1);
        
        // Simular @PrePersist manualmente para testing
        subscription.setSubscribedAt(LocalDateTime.now());
        
        LocalDateTime after = LocalDateTime.now().plusSeconds(1);
        
        assertNotNull(subscription.getSubscribedAt());
        assertTrue(subscription.getSubscribedAt().isAfter(before));
        assertTrue(subscription.getSubscribedAt().isBefore(after));
    }
}
