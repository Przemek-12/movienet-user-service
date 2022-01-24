package com.user.application.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.user.application.UserPreferencesService;
import com.user.application.VideoFeignEntityService;
import com.user.application.exceptions.EntityObjectAlreadyExistsException;
import com.user.application.exceptions.EntityObjectNotFoundException;

@Service
public class KafkaConsumerService {

    private final VideoFeignEntityService videoFeignEntityService;
    private final UserPreferencesService userPreferencesService;

    @Autowired
    public KafkaConsumerService(VideoFeignEntityService videoFeignEntityService,
            UserPreferencesService userPreferencesService) {
        this.videoFeignEntityService = videoFeignEntityService;
        this.userPreferencesService = userPreferencesService;
    }

    @KafkaListener(topics = "user-added", groupId = "video-service")
    void onUserAdded(String message) throws NumberFormatException, EntityObjectAlreadyExistsException {
        userPreferencesService.add(Long.parseLong(message));
    }

    @KafkaListener(topics = "user-deleted", groupId = "video-service")
    void onUserDeleted(String message) throws NumberFormatException, EntityObjectNotFoundException {
        userPreferencesService.deleteByUserId(Long.parseLong(message));
    }

    @KafkaListener(topics = "video-added", groupId = "video-service")
    void onVideoAdded(String message) throws NumberFormatException, EntityObjectAlreadyExistsException {
        videoFeignEntityService.addVideoFeignEntity(Long.parseLong(message));
    }

    @KafkaListener(topics = "video-deleted", groupId = "video-service")
    void onVideoDeleted(String message) throws NumberFormatException, EntityObjectNotFoundException {
        videoFeignEntityService.deleteByVideoId(Long.parseLong(message));
    }

}
