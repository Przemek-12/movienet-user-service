package com.user.application;

import com.user.domain.entity.UserPreferences;
import com.user.domain.entity.VideoFeignEntity;

public class UserPreferencesMocks {

    public static UserPreferences userPreferencesMock() {
        return UserPreferences.create(1L);
    }

    public static VideoFeignEntity videoFeignEntityMock(long videoId) {
        return VideoFeignEntity.create(videoId);
    }

}
