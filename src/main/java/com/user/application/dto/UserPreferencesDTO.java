package com.user.application.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserPreferencesDTO {

    private Long id;
    private Set<Long> toWatchVideos;
}
