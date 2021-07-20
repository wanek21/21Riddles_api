package com.riddles.api.service;

import com.riddles.api.dao.AppUpdates;
import com.riddles.api.dto.ServerResponse;
import com.riddles.api.repository.AppUpdatesRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppUpdatesService {

    private AppUpdatesRepository appUpdatesRepository;

    public AppUpdatesService(AppUpdatesRepository appUpdatesRepository) {
        this.appUpdatesRepository = appUpdatesRepository;
    }

    public ResponseEntity<ServerResponse> checkUpdates(int usersVersion) {
        Optional<AppUpdates> appUpdates = appUpdatesRepository.findById(1);

        int currentVersion;
        int requiredVersion;
        if(appUpdates.isPresent()) {
            currentVersion = appUpdates.get().getCurrentVersion();
            requiredVersion = appUpdates.get().getRequiredVersion();
        } else return new ResponseEntity<>(new ServerResponse(ServerResponse.ResponseCode.SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);

        if(usersVersion < requiredVersion)
            return new ResponseEntity<>(new ServerResponse(ServerResponse.ResponseCode.FORCE_UPDATE), HttpStatus.OK);
        else if(usersVersion < currentVersion)
            return new ResponseEntity<>(new ServerResponse(ServerResponse.ResponseCode.SOFT_UPDATE), HttpStatus.OK);
        else
            return new ResponseEntity<>(new ServerResponse(ServerResponse.ResponseCode.OK), HttpStatus.OK);
    }
}
