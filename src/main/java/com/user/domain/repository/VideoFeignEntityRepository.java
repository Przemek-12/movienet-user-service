package com.user.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.user.domain.entity.VideoFeignEntity;

@Repository
public interface VideoFeignEntityRepository extends JpaRepository<VideoFeignEntity, Long> {

    Optional<VideoFeignEntity> findByVideoId(Long videoId);

    boolean existsByVideoId(Long videoId);

    void deleteByVideoId(Long videoId);

}
