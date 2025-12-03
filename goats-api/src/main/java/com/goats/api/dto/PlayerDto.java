package com.goats.api.dto;

import com.goats.api.model.Player;

public class PlayerDto {
  private Long id;
  private String name;
  private String nickname;
  private String country;
  private String position;
  private String biography;
  
  // Campos JSON extendidos
  private String heroInfo;
  private String profileImage;
  private String profileStats;
  private String careerHighlights;
  private String playingStyle;
  private String achievements;
  private String statsData;
  private String stats;
  private String seasonStats;
  private String gallery;
  private String legacy;
  private String videos;

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
  
  // Getters y Setters de campos JSON extendidos
  public String getHeroInfo() { return heroInfo; }
  public void setHeroInfo(String heroInfo) { this.heroInfo = heroInfo; }
  public String getProfileImage() { return profileImage; }
  public void setProfileImage(String profileImage) { this.profileImage = profileImage; }
  public String getProfileStats() { return profileStats; }
  public void setProfileStats(String profileStats) { this.profileStats = profileStats; }
  public String getCareerHighlights() { return careerHighlights; }
  public void setCareerHighlights(String careerHighlights) { this.careerHighlights = careerHighlights; }
  public String getPlayingStyle() { return playingStyle; }
  public void setPlayingStyle(String playingStyle) { this.playingStyle = playingStyle; }
  public String getAchievements() { return achievements; }
  public void setAchievements(String achievements) { this.achievements = achievements; }
  public String getStatsData() { return statsData; }
  public void setStatsData(String statsData) { this.statsData = statsData; }
  public String getStats() { return stats; }
  public void setStats(String stats) { this.stats = stats; }
  public String getSeasonStats() { return seasonStats; }
  public void setSeasonStats(String seasonStats) { this.seasonStats = seasonStats; }
  public String getGallery() { return gallery; }
  public void setGallery(String gallery) { this.gallery = gallery; }
  public String getLegacy() { return legacy; }
  public void setLegacy(String legacy) { this.legacy = legacy; }
  public String getVideos() { return videos; }
  public void setVideos(String videos) { this.videos = videos; }

  public static PlayerDto from(Player p) {
    PlayerDto dto = new PlayerDto();
    dto.setId(p.getId());
    dto.setName(p.getName());
    dto.setNickname(p.getNickname());
    dto.setCountry(p.getCountry());
    dto.setPosition(p.getPosition());
    dto.setBiography(p.getBiography());
    
    // Copiar campos JSON extendidos
    dto.setHeroInfo(p.getHeroInfo());
    dto.setProfileImage(p.getProfileImage());
    dto.setProfileStats(p.getProfileStats());
    dto.setCareerHighlights(p.getCareerHighlights());
    dto.setPlayingStyle(p.getPlayingStyle());
    dto.setAchievements(p.getAchievementsJson());
    dto.setStatsData(p.getStatsData());
    dto.setStats(p.getStatsJson());
    dto.setSeasonStats(p.getSeasonStats());
    dto.setGallery(p.getGallery());
    dto.setLegacy(p.getLegacy());
    dto.setVideos(p.getVideos());
    
    return dto;
  }
}