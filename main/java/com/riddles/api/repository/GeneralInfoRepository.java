package com.riddles.api.repository;

import com.riddles.api.dao.GeneralInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GeneralInfoRepository extends JpaRepository<GeneralInfo, Integer> {
    Optional<GeneralInfo> findByLocale(String locale);
}
