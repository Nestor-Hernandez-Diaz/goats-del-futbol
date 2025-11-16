package com.goats.api.dto;

import com.goats.api.model.Achievement.AchievementType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO para Achievement
 * Gestiona datos de logros y competiciones
 */
public class AchievementDto {

    private Long id;

    @NotNull(message = "Player ID is required")
    private Long playerId;

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title cannot exceed 100 characters")
    private String title;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "Year is required")
    @Min(value = 1950, message = "Year must be at least 1950")
    @Max(value = 2100, message = "Year cannot exceed 2100")
    private Integer year;

    @NotNull(message = "Achievement type is required")
    private AchievementType type;

    @Size(max = 100, message = "Organization cannot exceed 100 characters")
    private String organization;

    // Constructores
    public AchievementDto() {
    }

    public AchievementDto(Long id, Long playerId, String title, String description,
                          Integer year, AchievementType type, String organization) {
        this.id = id;
        this.playerId = playerId;
        this.title = title;
        this.description = description;
        this.year = year;
        this.type = type;
        this.organization = organization;
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
}
