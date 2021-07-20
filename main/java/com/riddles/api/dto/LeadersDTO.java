package com.riddles.api.dto;

public class LeadersDTO {

    private int riddle;
    private String nickname;
    private int countUsersOnThisRiddle;
    private boolean completeGame;

    public int getRiddle() {
        return riddle;
    }

    public void setRiddle(int riddle) {
        this.riddle = riddle;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getCountUsersOnThisRiddle() {
        return countUsersOnThisRiddle;
    }

    public void setCountUsersOnThisRiddle(int countUsersOnThisRiddle) {
        this.countUsersOnThisRiddle = countUsersOnThisRiddle;
    }

    public boolean isCompleteGame() {
        return completeGame;
    }

    public void setCompleteGame(boolean completeGame) {
        this.completeGame = completeGame;
    }
}
