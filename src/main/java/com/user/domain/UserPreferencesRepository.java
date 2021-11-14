package com.user.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPreferencesRepository extends JpaRepository<UserPreferences, Long> {

    Optional<UserPreferences> findByUserId(Long userId);

}
