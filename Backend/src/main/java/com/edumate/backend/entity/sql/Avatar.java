package com.edumate.backend.entity.sql;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "avatars")
public class Avatar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "avatar_id")
    private Integer avatarId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank(message = "Avatar name is required")
    @Column(name = "avatar_name", nullable = false)
    private String avatarName;

    @Column(name = "avatar_url")
    private String avatarUrl;

    // Constructors
    public Avatar() {}

    public Avatar(User user, String avatarName, String avatarUrl) {
        this.user = user;
        this.avatarName = avatarName;
        this.avatarUrl = avatarUrl;
    }

    // Getters and Setters
    public Integer getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(Integer avatarId) {
        this.avatarId = avatarId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAvatarName() {
        return avatarName;
    }

    public void setAvatarName(String avatarName) {
        this.avatarName = avatarName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
