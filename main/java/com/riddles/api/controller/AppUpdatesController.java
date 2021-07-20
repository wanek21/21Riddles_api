package com.riddles.api.controller;

import com.riddles.api.dto.ServerResponse;
import com.riddles.api.service.AppUpdatesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/updates")
public class AppUpdatesController {

    private AppUpdatesService appUpdatesService;

    public AppUpdatesController(AppUpdatesService appUpdatesService) {
        this.appUpdatesService = appUpdatesService;
    }

    @GetMapping("")
    public ResponseEntity<ServerResponse> checkUpdates(@RequestParam(value = "version") int version) {
        return appUpdatesService.checkUpdates(version);
    }
}
