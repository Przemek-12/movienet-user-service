package com.user.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class VideoFeignEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private Long id;

    @Column(unique = true)
    @Getter
    @NonNull
    @NotNull
    private Long videoId;

    private VideoFeignEntity(@NonNull Long videoId) {
        this.videoId = videoId;
    }

    public static VideoFeignEntity create(Long videoId) {
        return new VideoFeignEntity(videoId);
    }

}
