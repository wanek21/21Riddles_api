package com.riddles.api.repository;

import com.riddles.api.dao.AppUpdates;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUpdatesRepository extends JpaRepository<AppUpdates, Integer> {
    Optional<AppUpdates> findById(Integer id);
}
