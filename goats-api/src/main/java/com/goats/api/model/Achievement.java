package com.goats.api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad Achievement para logros y competiciones ganadas
 * Relación ManyToOne con Player
 */
@Entity
@Table(name = "achievements")
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private AchievementType type;

    @Column(length = 100)
    private String organization; // FIFA, UEFA, etc.

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Enum para tipos de logros
    public enum AchievementType {
        INDIVIDUAL,      // Balón de Oro, Bota de Oro, etc.
        CLUB,           // Champions League, Liga, etc.
        NATIONAL_TEAM,  // Mundial, Copa América, Eurocopa, etc.
        RECORD,         // Récords personales
        OTHER
    }

    // Constructores
    public Achievement() {
    }

    public Achievement(Player player, String title, Integer year, AchievementType type) {
        this.player = player;
        this.title = title;
        this.year = year;
        this.type = type;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public AchievementType getType() {
        return type;
    }

    public void setType(AchievementType type) {
        this.type = type;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Achievement{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", year=" + year +
                ", type=" + type +
                '}';
    }
}
