package com.user.domain.entity;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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

    @ManyToMany(fetch = FetchType.LAZY)
    @Getter
    private Set<VideoFeignEntity> toWatchVideos = new LinkedHashSet<>();

    private UserPreferences(Long userId) {
        this.userId = userId;
    }

    public static UserPreferences create(Long userId) {
        return new UserPreferences(userId);
    }

    public UserPreferences addToWatch(VideoFeignEntity video) {
        toWatchVideos.add(video);
        return this;
    }

    public UserPreferences removeToWatch(VideoFeignEntity video) {
        toWatchVideos.remove(video);
        return this;
    }

}
