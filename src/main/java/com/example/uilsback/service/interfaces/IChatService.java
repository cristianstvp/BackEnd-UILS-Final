package com.example.uilsback.service.interfaces;

import com.example.uilsback.dto.ChatDTO;
import com.example.uilsback.model.Chat;
import org.springframework.stereotype.Service;

@Service
public interface IChatService {

    public boolean sendMessage(Long tripId, Long userId, String message);

    public ChatDTO getChat(Long tripId);

    public Chat createChat(Long driverId);

    public void addUserToChat(Long chatId, Long userId);

    public void removeUserFromChat(Long chatId, Long userId);

}
