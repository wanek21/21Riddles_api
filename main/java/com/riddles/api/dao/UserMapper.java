package com.riddles.api.dao;

import com.riddles.api.dto.UserDTO;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {

    public static UserDTO userToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setCurrentRiddle(user.getCurrentRiddle().getId());
        userDTO.setNickname(user.getNickname());
        userDTO.setCompleteRiddleDate(user.getCompleteRiddleDate());
        return userDTO;
    }

    public static List<UserDTO> listUsersToUsersDTO(List<User> users) {
        List<UserDTO> userDTO = new ArrayList<>();
        for (User user:
                users) {
            userDTO.add(UserMapper.userToUserDTO(user));
        }
        return userDTO;
    }
}
