package com.goats.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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
}