package com.goats.api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad PlayerStats para estadísticas detalladas de jugadores
 * Relación OneToOne con Player
 */
@Entity
@Table(name = "player_stats")
public class PlayerStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "player_id", nullable = false, unique = true)
    private Player player;

    @Column(nullable = false)
    private Integer goals = 0;

    @Column(nullable = false)
    private Integer assists = 0;

    @Column(nullable = false)
    private Integer matchesPlayed = 0;

    @Column(nullable = false)
    private Integer trophies = 0;

    @Column(nullable = false)
    private Integer yellowCards = 0;

    @Column(nullable = false)
    private Integer redCards = 0;

    @Column
    private Double minutesPlayed = 0.0;

    @Column
    private Integer ballonDOrWins = 0;

    @Column
    private Integer championsLeagueWins = 0;

    @Column
    private Integer worldCupWins = 0;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        
        // Inicializar valores nulos a 0
        if (goals == null) goals = 0;
        if (assists == null) assists = 0;
        if (matchesPlayed == null) matchesPlayed = 0;
        if (trophies == null) trophies = 0;
        if (yellowCards == null) yellowCards = 0;
        if (redCards == null) redCards = 0;
        if (minutesPlayed == null) minutesPlayed = 0.0;
        if (ballonDOrWins == null) ballonDOrWins = 0;
        if (championsLeagueWins == null) championsLeagueWins = 0;
        if (worldCupWins == null) worldCupWins = 0;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Constructores
    public PlayerStats() {
    }

    public PlayerStats(Player player) {
        this.player = player;
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

    public Integer getGoals() {
        return goals;
    }

    public void setGoals(Integer goals) {
        this.goals = goals;
    }

    public Integer getAssists() {
        return assists;
    }

    public void setAssists(Integer assists) {
        this.assists = assists;
    }

    public Integer getMatchesPlayed() {
        return matchesPlayed;
    }

    public void setMatchesPlayed(Integer matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
    }

    public Integer getTrophies() {
        return trophies;
    }

    public void setTrophies(Integer trophies) {
        this.trophies = trophies;
    }

    public Integer getYellowCards() {
        return yellowCards;
    }

    public void setYellowCards(Integer yellowCards) {
        this.yellowCards = yellowCards;
    }

    public Integer getRedCards() {
        return redCards;
    }

    public void setRedCards(Integer redCards) {
        this.redCards = redCards;
    }

    public Double getMinutesPlayed() {
        return minutesPlayed;
    }

    public void setMinutesPlayed(Double minutesPlayed) {
        this.minutesPlayed = minutesPlayed;
    }

    public Integer getBallonDOrWins() {
        return ballonDOrWins;
    }

    public void setBallonDOrWins(Integer ballonDOrWins) {
        this.ballonDOrWins = ballonDOrWins;
    }

    public Integer getChampionsLeagueWins() {
        return championsLeagueWins;
    }

    public void setChampionsLeagueWins(Integer championsLeagueWins) {
        this.championsLeagueWins = championsLeagueWins;
    }

    public Integer getWorldCupWins() {
        return worldCupWins;
    }

    public void setWorldCupWins(Integer worldCupWins) {
        this.worldCupWins = worldCupWins;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "PlayerStats{" +
                "id=" + id +
                ", playerId=" + (player != null ? player.getId() : null) +
                ", goals=" + goals +
                ", assists=" + assists +
                ", matchesPlayed=" + matchesPlayed +
                ", trophies=" + trophies +
                '}';
    }
}
