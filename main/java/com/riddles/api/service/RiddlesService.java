package com.riddles.api.service;

import com.riddles.api.dto.CheckAnswerDTO;
import com.riddles.api.dto.GetRiddleDTO;
import com.riddles.api.dto.RiddleDTO;
import com.riddles.api.dao.Riddle;
import com.riddles.api.dao.User;
import com.riddles.api.repository.RiddlesRepository;
import com.riddles.api.repository.UsersRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class RiddlesService {

    private RiddlesRepository riddlesRepository;
    private UsersRepository usersRepository;
    private BCryptPasswordEncoder passwordEncoder; // для ответов на загадки

    public RiddlesService(RiddlesRepository riddlesRepository, UsersRepository usersRepository, BCryptPasswordEncoder passwordEncoder) {
        this.riddlesRepository = riddlesRepository;
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /* Возвращает null, если че то не так */
    public RiddleDTO getRiddle(GetRiddleDTO getRiddle) {
        RiddleDTO resultRiddle = new RiddleDTO();

        String nickname = getRiddle.getNickname();
        String usersToken = usersRepository.getToken(nickname);
        String locale = getRiddle.getLocale();
        if(usersToken == null) return null;

        if(!usersToken.equals(getRiddle.getToken())) return null;

        int currentRiddle = usersRepository.findByNickname(nickname).getCurrentRiddle().getId();
        if(getRiddle.isNext()) currentRiddle++;

        if(currentRiddle > 0 && currentRiddle < 22) {
            Optional<Riddle> riddle = riddlesRepository.findById(currentRiddle);
            if(riddle.isPresent()) {
                // выбор языка загадки
                if(locale == null) resultRiddle.setRiddle(riddle.get().getRiddle());
                else {
                    switch (locale) {
                        case "ru":
                            resultRiddle.setRiddle(riddle.get().getRiddleRu());
                            break;
                        default: // английский по умолчанию
                            resultRiddle.setRiddle(riddle.get().getRiddle());
                            break;
                    }
                }
            }
            else resultRiddle = null;
        } else resultRiddle = null;

        return resultRiddle;
    }

    public Integer checkAnswer(CheckAnswerDTO checkAnswer) {
        // если какое то значение не пришло, возвращаем null
        if(checkAnswer == null ||
            checkAnswer.getNickname() == null ||
            checkAnswer.getToken() == null ||
            checkAnswer.getAnswer() == null) {
            return null;
        }

        String receivedNickname = checkAnswer.getNickname();
        String receivedToken = checkAnswer.getToken();
        String receivedAnswer = checkAnswer.getAnswer().toLowerCase();

        // проверяем совпадают ли токены
        if(!receivedToken.equals(usersRepository.getToken(receivedNickname))) {
            return null;
        }

        int level = usersRepository.getLevelByNickname(receivedNickname);
        Optional<Riddle> riddle = riddlesRepository.findById(level);
        if(riddle.isEmpty()) return null;


        if(isAnswerRight(riddle.get().getId(),receivedAnswer)) {
            User user = usersRepository.findByNickname(receivedNickname);
            Riddle newRiddle = new Riddle();

            if(user.getCurrentRiddle().getId() == 21) {
                user.setCompleteGame(true);
            } else {
                newRiddle.setId((user.getCurrentRiddle().getId()+1));
                user.setCurrentRiddle(newRiddle);
            }
            usersRepository.save(user);
            return 1;
        }
        else return 0;
    }

    private boolean isAnswerRight(int riddle, String answer) { // сравнение хеша из бд и захешированного ответа игрока
        ArrayList<String> hashedAnswers = (ArrayList<String>) riddlesRepository.getAnswers(riddle);
        for (String hashedAnswer :
                hashedAnswers) {
            if(passwordEncoder.matches(answer,hashedAnswer)) return true;
        }
        return false;
    }
}
