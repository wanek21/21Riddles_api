package com.riddles.api.controller;

import com.riddles.api.dto.CheckAnswerDTO;
import com.riddles.api.dto.GetRiddleDTO;
import com.riddles.api.dto.RiddleDTO;
import com.riddles.api.service.RiddlesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/riddles")
public class RiddlesController {

    private RiddlesService riddlesService;

    public RiddlesController(RiddlesService riddlesService) {
        this.riddlesService = riddlesService;
    }

    @PostMapping("")
    public ResponseEntity<RiddleDTO> getRiddle(@RequestBody GetRiddleDTO getRiddle) {
        RiddleDTO riddle = riddlesService.getRiddle(getRiddle);
        if(riddle == null) return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(riddle,HttpStatus.OK);
    }

    @PostMapping("/answer")
    public ResponseEntity<Integer> checkAnswer(@RequestBody CheckAnswerDTO checkAnswer) {
        Integer result = riddlesService.checkAnswer(checkAnswer);
        if(result == null) return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        else return new ResponseEntity<>(result,HttpStatus.OK);
    }
}
