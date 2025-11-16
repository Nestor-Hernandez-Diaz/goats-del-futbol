package com.goats.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para PlayerStats
 * Gestiona datos de estadísticas de jugadores
 */
public class PlayerStatsDto {

    private Long id;

    @NotNull(message = "Player ID is required")
    private Long playerId;

    @Min(value = 0, message = "Goals cannot be negative")
    private Integer goals = 0;

    @Min(value = 0, message = "Assists cannot be negative")
    private Integer assists = 0;

    @Min(value = 0, message = "Matches played cannot be negative")
    private Integer matchesPlayed = 0;

    @Min(value = 0, message = "Trophies cannot be negative")
    private Integer trophies = 0;

    @Min(value = 0, message = "Yellow cards cannot be negative")
    private Integer yellowCards = 0;

    @Min(value = 0, message = "Red cards cannot be negative")
    private Integer redCards = 0;

    @Min(value = 0, message = "Minutes played cannot be negative")
    private Double minutesPlayed = 0.0;

    @Min(value = 0, message = "Ballon d'Or wins cannot be negative")
    private Integer ballonDOrWins = 0;

    @Min(value = 0, message = "Champions League wins cannot be negative")
    private Integer championsLeagueWins = 0;

    @Min(value = 0, message = "World Cup wins cannot be negative")
    private Integer worldCupWins = 0;

    // Constructores
    public PlayerStatsDto() {
    }

    public PlayerStatsDto(Long id, Long playerId, Integer goals, Integer assists, Integer matchesPlayed,
                          Integer trophies, Integer yellowCards, Integer redCards, Double minutesPlayed,
                          Integer ballonDOrWins, Integer championsLeagueWins, Integer worldCupWins) {
        this.id = id;
        this.playerId = playerId;
        this.goals = goals;
        this.assists = assists;
        this.matchesPlayed = matchesPlayed;
        this.trophies = trophies;
        this.yellowCards = yellowCards;
        this.redCards = redCards;
        this.minutesPlayed = minutesPlayed;
        this.ballonDOrWins = ballonDOrWins;
        this.championsLeagueWins = championsLeagueWins;
        this.worldCupWins = worldCupWins;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
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
}
