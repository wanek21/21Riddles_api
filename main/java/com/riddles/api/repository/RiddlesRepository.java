package com.riddles.api.repository;

import com.riddles.api.dao.Riddle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RiddlesRepository extends JpaRepository<Riddle, Integer> {

    @Query(value = "SELECT answer FROM answers WHERE riddle_id=?1", nativeQuery = true)
    List<String> getAnswers(int riddle);
}
