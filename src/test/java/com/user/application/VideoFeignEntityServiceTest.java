package com.user.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.user.application.exceptions.EntityObjectAlreadyExistsException;
import com.user.application.exceptions.EntityObjectNotFoundException;
import com.user.domain.entity.VideoFeignEntity;
import com.user.domain.repository.VideoFeignEntityRepository;

@ExtendWith(MockitoExtension.class)
public class VideoFeignEntityServiceTest {

    @InjectMocks
    private VideoFeignEntityService videoFeignEntityService;

    @Mock
    private VideoFeignEntityRepository videoFeignEntityRepository;

    private void whenVideoFeignEntityRepositorySaveThenAnswer() {
        when(videoFeignEntityRepository.save(Mockito.any(VideoFeignEntity.class)))
                .thenAnswer(i -> i.getArgument(0));
    }

    private void whenVideoFeignEntityRepositoryFindByVideoIdThenReturn(
            Optional<VideoFeignEntity> videoFeign) {
        when(videoFeignEntityRepository.findByVideoId(Mockito.anyLong()))
                .thenReturn(videoFeign);
    }

    private void videoFeignEntityExistsByVideoId(boolean bool) {
        when(videoFeignEntityRepository.existsByVideoId(Mockito.anyLong()))
                .thenReturn(bool);
    }

    @Test
    void shouldAddVideoFeignEntityIfNotExists() throws EntityObjectAlreadyExistsException {
        whenVideoFeignEntityRepositorySaveThenAnswer();
        videoFeignEntityExistsByVideoId(false);

        videoFeignEntityService.addVideoFeignEntity(1L);

        verify(videoFeignEntityRepository, times(1)).save(Mockito.any(VideoFeignEntity.class));
    }

    @Test
    void shouldNotAddVideoFeignEntityIfAlreadyExists() {
        videoFeignEntityExistsByVideoId(true);

        assertThrows(EntityObjectAlreadyExistsException.class,
                () -> videoFeignEntityService.addVideoFeignEntity(1L),
                "VideoFeignEntity already exists.");
        verify(videoFeignEntityRepository, never()).save(Mockito.any(VideoFeignEntity.class));
    }

    @Test
    void shouldFindByVideoId() throws EntityObjectNotFoundException {
        whenVideoFeignEntityRepositoryFindByVideoIdThenReturn(Optional.of(VideoFeignEntity.create(1L)));

        assertThat(videoFeignEntityService.findByVideoId(1L)).isNotNull();
    }

    @Test
    void shouldThrowExceptionIfNotFoundByVideoId() {
        whenVideoFeignEntityRepositoryFindByVideoIdThenReturn(Optional.empty());

        assertThrows(EntityObjectNotFoundException.class,
                () -> videoFeignEntityService.findByVideoId(1L),
                "VideoFeignEntity not found.");
    }

    @Test
    void shouldDeleteByVideoId() throws EntityObjectNotFoundException {
        videoFeignEntityExistsByVideoId(true);

        videoFeignEntityService.deleteByVideoId(1L);

        verify(videoFeignEntityRepository, times(1)).deleteByVideoId(Mockito.anyLong());
    }

    @Test
    void shouldNotDeleteByVideoIdIfNotExists() {
        videoFeignEntityExistsByVideoId(false);

        assertThrows(EntityObjectNotFoundException.class,
                () -> videoFeignEntityService.deleteByVideoId(1L),
                "VideoFeignEntity not found.");
        verify(videoFeignEntityRepository, never()).deleteByVideoId(Mockito.anyLong());
    }
}
