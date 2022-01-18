package com.user.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.user.application.UserPreferencesService;
import com.user.application.dto.UserPreferencesDTO;
import com.user.application.exceptions.EntityObjectNotFoundException;
import com.user.infrastructure.security.SecurityUtils;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/user-preferences")
@Slf4j
public class UserPreferencesController {

    private final UserPreferencesService userPreferencesService;

    @Autowired
    public UserPreferencesController(UserPreferencesService userPreferencesService) {
        this.userPreferencesService = userPreferencesService;
    }

    @GetMapping
    public UserPreferencesDTO getUserPreferences() {
        try {
            return userPreferencesService.getUserPreferences(SecurityUtils.getUserId());
        } catch (EntityObjectNotFoundException e) {
            throw new ResponseStatusException(e.getStatus(), e.getMessage(), e);
        }
    }

    @GetMapping("/to-watch/contains")
    public boolean videoIsOnToWatchList(@RequestParam Long videoId) {
        try {
            return userPreferencesService.videoIsOnToWatchList(SecurityUtils.getUserId(), videoId);
        } catch (EntityObjectNotFoundException e) {
            throw new ResponseStatusException(e.getStatus(), e.getMessage(), e);
        }
    }

    @PutMapping("/to-watch/add")
    public UserPreferencesDTO addToWatch(@RequestParam Long videoId) {
        try {
            return userPreferencesService.addToWatch(SecurityUtils.getUserId(), videoId);
        } catch (EntityObjectNotFoundException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(e.getStatus(), e.getMessage(), e);
        }
    }

    @PutMapping("/to-watch/remove")
    public UserPreferencesDTO removeToWatch(@RequestParam Long videoId) {
        try {
            return userPreferencesService.removeToWatch(SecurityUtils.getUserId(), videoId);
        } catch (EntityObjectNotFoundException e) {
            throw new ResponseStatusException(e.getStatus(), e.getMessage(), e);
        }
    }

}
