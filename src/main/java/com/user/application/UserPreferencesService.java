package com.user.application;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.user.application.dto.UserPreferencesDTO;
import com.user.application.exceptions.EntityObjectNotFoundException;
import com.user.domain.entity.UserPreferences;
import com.user.domain.entity.VideoFeignEntity;
import com.user.domain.repository.UserPreferencesRepository;

@Service
public class UserPreferencesService {

    private final UserPreferencesRepository userPreferencesRepository;
    private final VideoFeignEntityService videoFeignEntityService;

    @Autowired
    public UserPreferencesService(UserPreferencesRepository userPreferencesRepository,
            VideoFeignEntityService videoFeignEntityService) {
        this.userPreferencesRepository = userPreferencesRepository;
        this.videoFeignEntityService = videoFeignEntityService;
    }

    public UserPreferencesDTO add(Long userId) {
        UserPreferences userPreferences = UserPreferences.create(userId);
        return mapToDTO(userPreferencesRepository.save(userPreferences));
    }

    public UserPreferencesDTO getUserPreferences(Long userId) throws EntityObjectNotFoundException {
        return mapToDTO(findByUserId(userId));
    }

    @Transactional
    public void deleteByUserId(Long userId) throws EntityObjectNotFoundException {
        checkIfPreferencesExistByUserId(userId);
        userPreferencesRepository.deleteByUserId(userId);
    }

    public UserPreferencesDTO addToWatch(Long userId, Long videoId) throws EntityObjectNotFoundException {
        UserPreferences userPreferences = findByUserId(userId).addToWatch(getVideoFeignEntity(videoId));
        return mapToDTO(userPreferencesRepository.save(userPreferences));
    }

    public UserPreferencesDTO removeToWatch(Long userId, Long videoId) throws EntityObjectNotFoundException {
        UserPreferences userPreferences = findByUserId(userId).removeToWatch(getVideoFeignEntity(videoId));
        return mapToDTO(userPreferencesRepository.save(userPreferences));
    }

    public boolean videoIsOnToWatchList(Long userId, Long videoId) throws EntityObjectNotFoundException {
        return findByUserId(userId).getToWatchVideos().stream()
                .anyMatch(videoFeign -> videoFeign.getVideoId().equals(videoId));
    }

    private UserPreferences findByUserId(Long userId) throws EntityObjectNotFoundException {
        return userPreferencesRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityObjectNotFoundException(UserPreferences.class.getSimpleName()));
    }

    private UserPreferencesDTO mapToDTO(UserPreferences userPreferences) {
        return UserPreferencesDTO.builder()
                .id(userPreferences.getId())
                .toWatchVideos(userPreferences.getToWatchVideos().stream()
                        .map(video -> video.getVideoId())
                        .collect(Collectors.toSet()))
                .build();
    }

    private VideoFeignEntity getVideoFeignEntity(Long videoId) throws EntityObjectNotFoundException {
        return videoFeignEntityService.findByVideoId(videoId);
    }

    private void checkIfPreferencesExistByUserId(Long userId) throws EntityObjectNotFoundException {
        if (!userPreferencesRepository.existsByUserId(userId)) {
            throw new EntityObjectNotFoundException(UserPreferences.class.getSimpleName());
        }
    }
}
