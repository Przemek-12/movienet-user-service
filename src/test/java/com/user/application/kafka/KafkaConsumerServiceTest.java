package com.user.application.kafka;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.user.application.UserPreferencesService;
import com.user.application.VideoFeignEntityService;
import com.user.application.exceptions.EntityObjectAlreadyExistsException;
import com.user.application.exceptions.EntityObjectNotFoundException;

@ExtendWith(MockitoExtension.class)
public class KafkaConsumerServiceTest {

    @InjectMocks
    private KafkaConsumerService kafkaConsumerService;

    @Mock
    private VideoFeignEntityService videoFeignEntityService;

    @Mock
    private UserPreferencesService userPreferencesService;

    @Test
    void shouldAddUserPreferencesOnUserAdded() {
        kafkaConsumerService.onUserAdded("1");
        verify(userPreferencesService, times(1)).add(1L);
    }

    @Test
    void shouldDeleteUserPreferencesOnUserDeleted() throws NumberFormatException,
            EntityObjectNotFoundException {
        kafkaConsumerService.onUserDeleted("1");
        verify(userPreferencesService, times(1)).deleteByUserId(1L);
    }

    @Test
    void shouldAddVideoFeignEntityOnVideoAdded() throws NumberFormatException,
            EntityObjectAlreadyExistsException {
        kafkaConsumerService.onVideoAdded("1");
        verify(videoFeignEntityService, times(1)).addVideoFeignEntity(1L);
    }

    @Test
    void shouldDeleteVideoFeignEntityOnVideoDeleted() throws NumberFormatException,
            EntityObjectNotFoundException {
        kafkaConsumerService.onVideoDeleted("1");
        verify(videoFeignEntityService, times(1)).deleteByVideoId(1L);
    }

}
