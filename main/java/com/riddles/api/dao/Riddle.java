package com.riddles.api.dao;

import javax.persistence.*;

@Entity(name = "riddles")
public class Riddle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String riddle; // text of riddle
    @JoinColumn(name = "riddle_ru")
    private String riddleRu; // text of riddle

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRiddle() {
        return riddle;
    }

    public String getRiddleRu() {
        return riddleRu;
    }

    public void setRiddleRu(String riddleRu) {
        this.riddleRu = riddleRu;
    }

    public void setRiddle(String riddle) {
        this.riddle = riddle;
    }

}
