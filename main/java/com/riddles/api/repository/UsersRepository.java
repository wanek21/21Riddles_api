package com.riddles.api.repository;

import com.riddles.api.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UsersRepository extends JpaRepository<User,Integer> {

    @Query(value = "SELECT * FROM users WHERE LOWER(nickname)=LOWER(?1)", nativeQuery = true)
    User findByNickname(String nickname);

    @Query(value = "SELECT nickname FROM users WHERE current_riddle=?1 AND complete_game='f' ORDER BY complete_riddle_date LIMIT 1", nativeQuery = true)
    String getLeadersNameOnLevel(int level); // получить ник лидера на уровне (только того, кто еще не прошел игру)

    @Query(value = "SELECT COUNT(nickname) FROM users WHERE current_riddle=?1 AND complete_game!='t'", nativeQuery = true)
    int getCountLeadersOnLevel(int level); // получить кол-во игроков на уровне (только тех, кто еще не прошел игру)

    @Query(value = "SELECT current_riddle FROM users ORDER BY current_riddle DESC LIMIT 1;", nativeQuery = true)
    Integer getMaxLevel(); // получить самый высокий уровень, который сейчас решается

    @Query(value = "SELECT token FROM users WHERE nickname=?1", nativeQuery = true)
    String getToken(String nickname);

    @Query(value = "SELECT current_riddle FROM users WHERE nickname=?1", nativeQuery = true)
    int getLevelByNickname(String nickname);

    @Query(value = "SELECT nickname FROM users WHERE complete_game='t' ORDER BY complete_riddle_date", nativeQuery = true)
    List<String> getUsersCompletedGame();

    @Query(value = "SELECT nickname FROM users WHERE complete_game='t' ORDER BY complete_riddle_date LIMIT 1", nativeQuery = true)
    String getWinnerNickname();
}
