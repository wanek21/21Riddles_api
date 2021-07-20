package com.riddles.api.dto;

import javax.persistence.*;
import java.time.Instant;

public class UserDTO {

    private String nickname;
    private int currentRiddle;
    private Instant completeRiddleDate; // время, когда игрок прошел предыдущий уровень

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getCurrentRiddle() {
        return currentRiddle;
    }

    public void setCurrentRiddle(int currentRiddle) {
        this.currentRiddle = currentRiddle;
    }

    public Instant getCompleteRiddleDate() {
        return completeRiddleDate;
    }

    public void setCompleteRiddleDate(Instant completeRiddleDate) {
        this.completeRiddleDate = completeRiddleDate;
    }
}
