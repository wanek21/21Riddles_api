package com.riddles.api.service;

import com.riddles.api.dto.GetPlace;
import com.riddles.api.dto.LeadersDTO;
import com.riddles.api.dto.RegisterUserDTO;
import com.riddles.api.dao.*;
import com.riddles.api.dto.ServerResponse;
import com.riddles.api.repository.DBConnection;
import com.riddles.api.repository.RiddlesRepository;
import com.riddles.api.repository.UsersRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.riddles.api.dto.ServerResponse.ResponseCode.*;

@Service
public class UsersService {

    private UsersRepository usersRepository;
    private RiddlesRepository riddlesRepository;


    private final int MAX_COUNT_LOGINS_PER_DAY = 5;

    public UsersService(UsersRepository usersRepository, RiddlesRepository riddlesRepository) {
        this.usersRepository = usersRepository;
        this.riddlesRepository = riddlesRepository;
    }

    public ResponseEntity<ServerResponse> registerNewUser(RegisterUserDTO user, String ipAddress) {
        System.out.println();

        // проверяем наличие токена и его длинну
        if(user.getToken() == null || (user.getToken().length() < 42)) {
            return new ResponseEntity<>(new ServerResponse(SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(user.getUniqueString() != null)
            user.setUniqueString(ipAddress.concat(user.getUniqueString())); // объединяем в одну строку ip и deviceId
        else user.setUniqueString(ipAddress);

        // проверка на кол-во входов с одного ip
        if(!isAllowedNumbersOfLogins(user.getUniqueString()))
            return new ResponseEntity<>(new ServerResponse(TOO_MANY_LOGINS_FROM_IP), HttpStatus.OK);

        // проверка на секретные ключи в нике
        if(encodeNick(user.getNickname()) == null)
            return new ResponseEntity<>(new ServerResponse(TOO_MANY_LOGINS_FROM_IP), HttpStatus.OK);
        else user.setNickname(encodeNick(user.getNickname()));

        // проверка на валидность ника
        if(!isValidName(user.getNickname()))
            return new ResponseEntity<>(new ServerResponse(INVALID_NAME), HttpStatus.OK);


        User userDao = new User();
        userDao.setNickname(user.getNickname());
        userDao.setToken(user.getToken());
        // проверка на занятость ника
        if(usersRepository.findByNickname(user.getNickname()) == null) {
            Optional<Riddle> firstRiddle = riddlesRepository.findById(1);
            if(firstRiddle.isPresent()) {
                userDao.setCurrentRiddle(firstRiddle.get());
            } else return new ResponseEntity<>(new ServerResponse(SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);

            usersRepository.save(userDao);
            newLogin(user.getUniqueString());
            return new ResponseEntity<>(new ServerResponse(OK),HttpStatus.CREATED);
        } else return new ResponseEntity<>(
                new ServerResponse(NAME_IS_TAKEN),
                HttpStatus.OK);
    }

    public ResponseEntity<List<LeadersDTO>> getLeaders() {
        int recordsNumber = 4; // кол-во элементов в выходном массиве лидеров
        List<LeadersDTO> leaders = new ArrayList<>();
        Integer maxLevel = usersRepository.getMaxLevel();
        if(maxLevel == null) return new ResponseEntity<>(leaders,HttpStatus.OK);

        // если максимальный уровень игроков уже 21, то проверяем есть ли игроки, прошедшие игру
        // и заносим их массив с лидерами
        if(maxLevel == 21) {
            ArrayList<String> winnersNicknames = (ArrayList<String>) usersRepository.getUsersCompletedGame();
            if(winnersNicknames.size() > 0) {
                recordsNumber--;
                LeadersDTO leadersDTO = new LeadersDTO();
                leadersDTO.setRiddle(21);
                leadersDTO.setNickname(winnersNicknames.get(0));
                leadersDTO.setCountUsersOnThisRiddle(winnersNicknames.size());
                leadersDTO.setCompleteGame(true);
                leaders.add(leadersDTO);
            }
        }

        for(int i = 0, lvl = maxLevel; i < recordsNumber; lvl--) {
            if(lvl == 0) break;

            int countLeadersOnLevel = usersRepository.getCountLeadersOnLevel(lvl);
            if(countLeadersOnLevel == 0) continue;
            String nickname = usersRepository.getLeadersNameOnLevel(lvl);
            LeadersDTO leadersOnLevel = new LeadersDTO();
            leadersOnLevel.setRiddle(lvl);
            leadersOnLevel.setNickname(nickname);
            leadersOnLevel.setCountUsersOnThisRiddle(countLeadersOnLevel);
            leaders.add(leadersOnLevel);
            i++;
        }

        return new ResponseEntity<>(leaders,HttpStatus.OK);
    }

    public Integer getPlace(GetPlace getPlace) {
        // проверяем совпадают ли токены
        if(!getPlace.getToken().equals(usersRepository.getToken(getPlace.getNickname()))) {
            return null;
        }
        ArrayList<String> winnersNicknames = (ArrayList<String>) usersRepository.getUsersCompletedGame();
        int place = winnersNicknames.indexOf(getPlace.getNickname()) + 1;
        return place;
    }

    private boolean isValidName(String name) { // проверка ника на валидность
        if(name.length() < 4) return false;
        if(name.length() > 15) return false;

        String lowerCaseName = name.toLowerCase();
        /* здесь проверка на плохие слова, удалил с гитхаба */
        }
        if(name.indexOf(' ') != name.lastIndexOf(' ')) return false; // если больше одного пробела
        return name.matches("[A-Za-z0-9а-яА-Я\\s]+"); // проверка на соответсвие симолам
    }

    /* проверка на кол-во регистраций с одного ip адреса
    * если больше MAX_COUNT_LOGINS_PER_DAY, то возвращается false */
    private boolean isAllowedNumbersOfLogins(String userId) {
        try {
            PreparedStatement statement = DBConnection
                    .getConnection()
                    .prepareStatement("SELECT COUNT(user_id) FROM recent_logins WHERE user_id=? AND time >= NOW() - INTERVAL '1 DAY' LIMIT ?;");
            statement.setString(1,userId);
            statement.setInt(2,MAX_COUNT_LOGINS_PER_DAY);

            ResultSet resultSet = statement.executeQuery();
            int countLogins = 0;
            while (resultSet.next()) {
                countLogins = resultSet.getInt("count");
            }
            if(countLogins == MAX_COUNT_LOGINS_PER_DAY) return false;
            else return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }

    /* метод заносит в таблицу запись о новой регистрации */
    private void newLogin(String uniqueString) {
        PreparedStatement statement = null;
        try {
            statement = DBConnection
                    .getConnection()
                    .prepareStatement("INSERT INTO recent_logins(user_id,time) VALUES(?,NOW());");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            assert statement != null;
            statement.setString(1,uniqueString);
            statement.executeQuery();
        } catch (SQLException throwables) {
            System.out.println("Error while inserting new user ip");
            throwables.printStackTrace();
        }
    }

    private String encodeNick(String nick) {
        if(nick.contains(";wins4")) return nick.substring(0,nick.length()-6);
        else return null;
    }
}
