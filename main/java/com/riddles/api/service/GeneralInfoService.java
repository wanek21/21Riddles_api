package com.riddles.api.service;

import com.riddles.api.dao.GeneralInfo;
import com.riddles.api.dto.GetEmail;
import com.riddles.api.dto.PrizeDTO;
import com.riddles.api.repository.GeneralInfoRepository;
import com.riddles.api.repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GeneralInfoService {

    private GeneralInfoRepository infoRepository;
    private UsersRepository usersRepository;

    public GeneralInfoService(GeneralInfoRepository infoRepository, UsersRepository usersRepository) {
        this.infoRepository = infoRepository;
        this.usersRepository = usersRepository;
    }

    public PrizeDTO getPrize(String locale) {
        PrizeDTO prize = new PrizeDTO();
        Optional<GeneralInfo> generalInfo = infoRepository.findByLocale(locale);
        generalInfo.ifPresent(info -> prize.setPrize(info.getPrize()));
        return prize;
    }
    public String getEmail(GetEmail getEmail) {

        // проверяем совпадают ли токены
        if(!getEmail.getToken().equals(usersRepository.getToken(getEmail.getNickname()))) {
            return null;
        }
        // проверяем совпадает ли полученный никнем с никнеймом победителя
        String winnerNickname = usersRepository.getWinnerNickname();
        if(winnerNickname != null && !getEmail.getNickname().equals(usersRepository.getWinnerNickname())) {
            return null;
        }
        String email = null;
        Optional<GeneralInfo> generalInfo = infoRepository.findByLocale(getEmail.getLocale());
        if(generalInfo.isPresent()) {
            email = generalInfo.get().getEmail();
        }
        return email;
    }
}
