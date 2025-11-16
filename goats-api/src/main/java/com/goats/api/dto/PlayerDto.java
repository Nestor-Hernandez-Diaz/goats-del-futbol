package com.goats.api.dto;

import com.goats.api.model.Player;

public class PlayerDto {
  private Long id;
  private String name;
  private String nickname;
  private String country;
  private String position;
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

  public static PlayerDto from(Player p) {
    PlayerDto dto = new PlayerDto();
    dto.setId(p.getId());
    dto.setName(p.getName());
    dto.setNickname(p.getNickname());
    dto.setCountry(p.getCountry());
    dto.setPosition(p.getPosition());
    dto.setBiography(p.getBiography());
    return dto;
  }
}