package com.user.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.user.application.exceptions.EntityObjectAlreadyExistsException;
import com.user.application.exceptions.EntityObjectNotFoundException;
import com.user.domain.entity.VideoFeignEntity;
import com.user.domain.repository.VideoFeignEntityRepository;

@Service
public class VideoFeignEntityService {

    private final VideoFeignEntityRepository videoFeignEntityRepository;

    @Autowired
    public VideoFeignEntityService(VideoFeignEntityRepository videoFeignEntityRepository) {
        this.videoFeignEntityRepository = videoFeignEntityRepository;
    }

    public void addVideoFeignEntity(Long videoId) throws EntityObjectAlreadyExistsException {
        checkIfVideoFeignEntityAlreadyExists(videoId);
        VideoFeignEntity videoFeignEntity = VideoFeignEntity.create(videoId);
        videoFeignEntityRepository.save(videoFeignEntity);
    }

    public VideoFeignEntity findByVideoId(Long videoId) throws EntityObjectNotFoundException {
        return videoFeignEntityRepository.findByVideoId(videoId)
                .orElseThrow(() -> new EntityObjectNotFoundException(VideoFeignEntity.class.getSimpleName()));
    }
    
    @Transactional
    public void deleteByVideoId(Long videoId) throws EntityObjectNotFoundException {
        checkIfVideoFeignEntityExistsByVideoId(videoId);
        videoFeignEntityRepository.deleteByVideoId(videoId);
    }
    
    private void checkIfVideoFeignEntityAlreadyExists(Long videoId) throws EntityObjectAlreadyExistsException {
        if (videoFeignEntityRepository.existsByVideoId(videoId)) {
            throw new EntityObjectAlreadyExistsException(VideoFeignEntity.class.getSimpleName());
        }
    }

    private void checkIfVideoFeignEntityExistsByVideoId(Long videoId) throws EntityObjectNotFoundException {
        if (!videoFeignEntityRepository.existsByVideoId(videoId)) {
            throw new EntityObjectNotFoundException(VideoFeignEntity.class.getSimpleName());
        }
    }

}
