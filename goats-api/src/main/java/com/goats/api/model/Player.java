package com.goats.api.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Player para jugadores del sistema
 * Incluye relaciones con PlayerStats, Achievement, Comment y Subscription
 */
@Entity
@Table(name = "players")
public class Player {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  private String nickname;
  private String country;
  private String position;

  @Column(columnDefinition = "TEXT")
  private String biography;

  // Relación OneToOne con PlayerStats
  @OneToOne(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private PlayerStats stats;

  // Relación OneToMany con Achievement
  @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<Achievement> achievements = new ArrayList<>();

  // Relación OneToMany con Comment
  @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<Comment> comments = new ArrayList<>();

  // Relación OneToMany con Subscription
  @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<Subscription> subscriptions = new ArrayList<>();

  // Getters y Setters básicos
  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
  public String getNickname() { return nickname; }
  public void setNickname(String nickname) { this.nickname = nickname; }
  public String getCountry() { return country; }
  public void setCountry(String country) { this.country = country; }
  public String getPosition() { return position; }
  public void setPosition(String position) { this.position = position; }
  public String getBiography() { return biography; }
  public void setBiography(String biography) { this.biography = biography; }

  // Getters y Setters de relaciones
  public PlayerStats getStats() { return stats; }
  public void setStats(PlayerStats stats) { 
    this.stats = stats;
    if (stats != null) {
      stats.setPlayer(this);
    }
  }

  public List<Achievement> getAchievements() { return achievements; }
  public void setAchievements(List<Achievement> achievements) { this.achievements = achievements; }

  public List<Comment> getComments() { return comments; }
  public void setComments(List<Comment> comments) { this.comments = comments; }

  public List<Subscription> getSubscriptions() { return subscriptions; }
  public void setSubscriptions(List<Subscription> subscriptions) { this.subscriptions = subscriptions; }

  // Métodos de utilidad para manejar relaciones
  public void addAchievement(Achievement achievement) {
    achievements.add(achievement);
    achievement.setPlayer(this);
  }

  public void removeAchievement(Achievement achievement) {
    achievements.remove(achievement);
    achievement.setPlayer(null);
  }

  @Override
  public String toString() {
    return "Player{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", country='" + country + '\'' +
            ", position='" + position + '\'' +
            '}';
  }
}