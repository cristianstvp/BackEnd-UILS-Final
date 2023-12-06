package com.example.uilsback.repository;

import com.example.uilsback.model.UserChat;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserChatRepository extends JpaRepository<UserChat, Long> {

    @Transactional
    Integer deleteByChatIdAndUserId(Long chatId, Long userId);

    boolean existsByChatIdAndUserId(Long chatId, Long userId);

}
