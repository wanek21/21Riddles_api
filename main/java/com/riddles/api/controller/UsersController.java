package com.riddles.api.controller;

import com.riddles.api.dto.GetPlace;
import com.riddles.api.dto.LeadersDTO;
import com.riddles.api.dto.RegisterUserDTO;
import com.riddles.api.dto.ServerResponse;
import com.riddles.api.service.RequestService;
import com.riddles.api.service.RequestServiceIml;
import com.riddles.api.service.UsersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

    private UsersService usersService;
    private RequestService requestService;

    public UsersController(UsersService usersService, RequestServiceIml requestService) {
        this.usersService = usersService;
        this.requestService = requestService;
    }

    @PostMapping("/add")
    public ResponseEntity<ServerResponse> registerNewUser(@RequestBody RegisterUserDTO registerUser, HttpServletRequest request) {
        System.out.println(requestService.getClientIp(request));
        System.out.println("User name: " + registerUser.getNickname());
        System.out.println("User id: " + registerUser.getUniqueString());
        return usersService.registerNewUser(registerUser, requestService.getClientIp(request));
    }

    @GetMapping("/leaders")
    public ResponseEntity<List<LeadersDTO>> getLeaders(@RequestParam(value = "lead") String keyWord) {
        if(keyWord.equals("please")) return usersService.getLeaders();
        else return null;
    }

    @PostMapping("/place")
    public ResponseEntity<Integer> getPlace(@RequestBody GetPlace getPlace) {
        Integer result = usersService.getPlace(getPlace);
        if(result == null) return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        else return new ResponseEntity<>(result,HttpStatus.OK);
    }
}
