package com.riddles.api.controller;

import com.riddles.api.dto.GetEmail;
import com.riddles.api.dto.PrizeDTO;
import com.riddles.api.service.GeneralInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/main")
public class MainController {

    private GeneralInfoService infoService;

    public MainController(GeneralInfoService infoService) {
        this.infoService = infoService;
    }

    @GetMapping("/prize")
    public ResponseEntity<PrizeDTO> getPrize(@RequestParam("prize") String key, @RequestParam("locale") String locale) {
        if(key.equals("chair")) {
            return new ResponseEntity<>(infoService.getPrize(locale), HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/email")
    public ResponseEntity<String> getEmail(@RequestBody GetEmail getEmail) {
        String result = infoService.getEmail(getEmail);
        if(result == null) return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        else return new ResponseEntity<>(result,HttpStatus.OK);
    }
}
