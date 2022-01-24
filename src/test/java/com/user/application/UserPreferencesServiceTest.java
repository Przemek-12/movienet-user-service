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
import com.user.domain.entity.UserPreferences;
import com.user.domain.entity.VideoFeignEntity;
import com.user.domain.repository.UserPreferencesRepository;

@ExtendWith(MockitoExtension.class)
public class UserPreferencesServiceTest {

    @InjectMocks
    private UserPreferencesService userPreferencesService;

    @Mock
    private UserPreferencesRepository userPreferencesRepository;

    @Mock
    private VideoFeignEntityService videoFeignEntityService;

    private void whenUserPreferencesRepositorySaveThenAnswer() {
        when(userPreferencesRepository.save(Mockito.any(UserPreferences.class)))
                .thenAnswer(i -> i.getArgument(0));
    }

    private void userPreferencesExistsByUserId(boolean bool) {
        when(userPreferencesRepository.existsByUserId(Mockito.anyLong()))
                .thenReturn(bool);
    }

    private void whenUserPreferencesRepositoryFindByUserIdThenReturn(
            Optional<UserPreferences> userPreferences) {
        when(userPreferencesRepository.findByUserId(Mockito.anyLong()))
                .thenReturn(userPreferences);
    }

    private void whenVideoFeignEntityServiceFindByVideoIdThenAnswerMock() throws EntityObjectNotFoundException {
        when(videoFeignEntityService.findByVideoId(Mockito.anyLong()))
                .thenAnswer(i -> UserPreferencesMocks.videoFeignEntityMock(i.getArgument(0)));
    }

    @Test
    void shouldAddUserPreferences() throws EntityObjectAlreadyExistsException {
        whenUserPreferencesRepositorySaveThenAnswer();
        userPreferencesExistsByUserId(false);

        userPreferencesService.add(1L);

        verify(userPreferencesRepository, times(1)).save(Mockito.any(UserPreferences.class));
    }

    @Test
    void shouldNotAddUserPreferencesIfAlreadyExists() {
        userPreferencesExistsByUserId(true);

        assertThrows(EntityObjectAlreadyExistsException.class,
                () -> userPreferencesService.add(1L),
                "UserPreferences already exists.");
    }

    @Test
    void shouldGetUserPreferencesByUserId() throws EntityObjectNotFoundException {
        whenUserPreferencesRepositoryFindByUserIdThenReturn(
                Optional.of(UserPreferencesMocks.userPreferencesMock()));

        assertThat(userPreferencesService.getUserPreferences(1L)).isNotNull();
    }

    @Test
    void shouldThrowExceptionIfUserPreferencesNotFoundByUserId() {
        whenUserPreferencesRepositoryFindByUserIdThenReturn(Optional.empty());

        assertThrows(EntityObjectNotFoundException.class,
                () -> userPreferencesService.getUserPreferences(1L),
                "UserPreferences not found.");
    }

    @Test
    void shouldDeleteUserPreferencesByUserId() throws EntityObjectNotFoundException {
        userPreferencesExistsByUserId(true);

        userPreferencesService.deleteByUserId(1L);

        verify(userPreferencesRepository, times(1)).deleteByUserId(1L);
    }

    @Test
    void shouldNotDeleteUserPreferencesByUserIdIfNotExists() {
        userPreferencesExistsByUserId(false);

        assertThrows(EntityObjectNotFoundException.class,
                () -> userPreferencesService.deleteByUserId(1L),
                "UserPreferences not found.");

        verify(userPreferencesRepository, never()).deleteByUserId(1L);
    }

    @Test
    void shouldAddToWatch() throws EntityObjectNotFoundException {
        final Long VIDEO_TO_ADD = 2L;

        whenUserPreferencesRepositorySaveThenAnswer();
        whenUserPreferencesRepositoryFindByUserIdThenReturn(
                Optional.of(UserPreferences.create(1L)));
        whenVideoFeignEntityServiceFindByVideoIdThenAnswerMock();

        userPreferencesService.addToWatch(1L, VIDEO_TO_ADD);

        verify(userPreferencesRepository, times(1))
                .save(Mockito.argThat(arg -> arg.getToWatchVideos().stream()
                        .anyMatch(feign -> feign.getVideoId().equals(VIDEO_TO_ADD) == true)));
    }

    @Test
    void shouldRemoveToWatch() throws EntityObjectNotFoundException {
        final Long VIDEO_TO_REMOVE = 2L;

        UserPreferences userPreferences = UserPreferences.create(1L);
        userPreferences.addToWatch(VideoFeignEntity.create(VIDEO_TO_REMOVE));

        whenUserPreferencesRepositorySaveThenAnswer();
        whenUserPreferencesRepositoryFindByUserIdThenReturn(Optional.of(userPreferences));
        whenVideoFeignEntityServiceFindByVideoIdThenAnswerMock();

        userPreferencesService.removeToWatch(1L, VIDEO_TO_REMOVE);

        verify(userPreferencesRepository, times(1))
                .save(Mockito.argThat(arg -> arg.getToWatchVideos().stream()
                        .noneMatch(feign -> feign.getVideoId().equals(VIDEO_TO_REMOVE) == true)));
    }

    @Test
    void shouldReturnTrueIfVideoIsOnToWatchList() throws EntityObjectNotFoundException {
        final Long VIDEO = 2L;

        UserPreferences userPreferences = UserPreferences.create(1L);
        userPreferences.addToWatch(VideoFeignEntity.create(VIDEO));

        whenUserPreferencesRepositoryFindByUserIdThenReturn(Optional.of(userPreferences));

        assertThat(userPreferencesService.videoIsOnToWatchList(1L, VIDEO)).isTrue();
    }

    @Test
    void shouldReturnFalseIfVideoIsNotOnToWatchList() throws EntityObjectNotFoundException {
        UserPreferences userPreferences = UserPreferences.create(1L);

        whenUserPreferencesRepositoryFindByUserIdThenReturn(Optional.of(userPreferences));

        assertThat(userPreferencesService.videoIsOnToWatchList(1L, 1L)).isFalse();
    }
}
