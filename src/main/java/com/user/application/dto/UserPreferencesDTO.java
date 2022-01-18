package com.user.application.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
public class UserPreferencesDTO {

    private Long id;
    private Set<Long> toWatchVideos;
}
