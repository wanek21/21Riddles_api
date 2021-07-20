package com.riddles.api.dao;

import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nickname;

    @ManyToOne
    @JoinColumn(name = "current_riddle")
    private Riddle currentRiddle;

    @JoinColumn(name = "complete_riddle_date")
    @UpdateTimestamp
    private Instant completeRiddleDate; // время, когда игрок прошел предыдущий уровень

    @JoinColumn(name = "token")
    private String token;

    @JoinColumn(name = "complete_game")
    private boolean completeGame;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Riddle getCurrentRiddle() {
        return currentRiddle;
    }

    public void setCurrentRiddle(Riddle currentRiddle) {
        this.currentRiddle = currentRiddle;
    }

    public Instant getCompleteRiddleDate() {
        return completeRiddleDate;
    }

    public void setCompleteRiddleDate(Instant completeRiddleDate) {
        this.completeRiddleDate = completeRiddleDate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isCompleteGame() {
        return completeGame;
    }

    public void setCompleteGame(boolean completeGame) {
        this.completeGame = completeGame;
    }
}
