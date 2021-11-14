package com.user.domain;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPreferences {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private Long id;

    @Column(unique = true)
    @NonNull
    @NotNull
    private Long userId;

    @ElementCollection
    @Getter
    private Set<Long> toWatchVideos = new LinkedHashSet<>();

    private UserPreferences(Long userId) {
        this.userId = userId;
    }

    public static UserPreferences create(Long userId) {
        return new UserPreferences(userId);
    }

    public UserPreferences addToWatch(Long videoId) {
        toWatchVideos.add(videoId);
        return this;
    }

    public UserPreferences removeToWatch(Long videoId) {
        toWatchVideos.remove(videoId);
        return this;
    }

}
