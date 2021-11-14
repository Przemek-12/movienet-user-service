package com.user.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.user.application.dto.UserPreferencesDTO;
import com.user.application.exceptions.EntityObjectNotFoundException;
import com.user.domain.UserPreferences;
import com.user.domain.UserPreferencesRepository;

@Service
public class UserPreferencesService {

    private final UserPreferencesRepository userPreferencesRepository;

    @Autowired
    public UserPreferencesService(UserPreferencesRepository userPreferencesRepository) {
        this.userPreferencesRepository = userPreferencesRepository;
    }

    public UserPreferencesDTO add(Long userId) {
        UserPreferences userPreferences = UserPreferences.create(userId);
        return mapToDTO(userPreferencesRepository.save(userPreferences));
    }

    public UserPreferencesDTO addToWatch(Long userId, Long videoId) throws EntityObjectNotFoundException {
        UserPreferences userPreferences = findByUserId(userId).addToWatch(videoId);
        return mapToDTO(userPreferencesRepository.save(userPreferences));
    }

    public UserPreferencesDTO removeToWatch(Long userId, Long videoId) throws EntityObjectNotFoundException {
        UserPreferences userPreferences = findByUserId(userId).removeToWatch(videoId);
        return mapToDTO(userPreferencesRepository.save(userPreferences));
    }

    private UserPreferences findByUserId(Long userId) throws EntityObjectNotFoundException {
        return userPreferencesRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityObjectNotFoundException(UserPreferences.class.getSimpleName()));
    }

    private UserPreferencesDTO mapToDTO(UserPreferences userPreferences) {
        return UserPreferencesDTO.builder()
                .id(userPreferences.getId())
                .toWatchVideos(userPreferences.getToWatchVideos())
                .build();
    }
}
