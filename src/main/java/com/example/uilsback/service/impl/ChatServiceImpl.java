package com.example.uilsback.service.impl;

import com.example.uilsback.dto.ChatDTO;
import com.example.uilsback.dto.MessageDTO;
import com.example.uilsback.exception.CustomException;
import com.example.uilsback.mapper.IChatMapper;
import com.example.uilsback.mapper.IMessageMapper;
import com.example.uilsback.model.Chat;
import com.example.uilsback.model.Message;
import com.example.uilsback.model.Trip;
import com.example.uilsback.model.UserChat;
import com.example.uilsback.repository.*;
import com.example.uilsback.service.interfaces.IChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatServiceImpl implements IChatService {

    private IUserRepository userRepository;

    private IChatRepository chatRepository;

    private IMessageRepository messageRepository;

    private IUserChatRepository userChatRepository;

    private ITripRepository tripRepository;

    public boolean sendMessage(Long tripId, Long userId, String message) {
        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new CustomException("No existe el viaje"));

        if(trip == null || trip.getChat() == null){
            return false;
        }

        Long chatId = trip.getChat().getId();

        if(userChatRepository.existsByChatIdAndUserId(chatId, userId)){
            Message newMessage = new Message();
            newMessage.setChatId(chatId);
            newMessage.setUserId(userId);
            newMessage.setMessageContent(message);
            newMessage.setTimestamp(LocalDateTime.now());
            Message send = messageRepository.save(newMessage);
            return send.getId() != null && send.getMessageContent().equals(message);
        }else{
            throw new CustomException("No puedes enviar mensajes a este chat");
        }
    }

    public ChatDTO getChat(Long tripId) {
        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new CustomException("No existe el viaje"));
        Chat chat = trip.getChat();
        ChatDTO chatDTO = IChatMapper.INSTANCE.chatToChatDTO(chat);
        List<Message> messages = chat.getMessages();
        List<MessageDTO> messageDTOS = IMessageMapper.INSTANCE.messagesToMessageDTOs(messages);
        chatDTO.setMessages(messageDTOS);

        return chatDTO;
    }

    public void addUserToChat(Long chatId, Long userId) {
        UserChat userChat = new UserChat();
        userChat.setChatId(chatId);
        userChat.setUserId(userId);
        userChatRepository.save(userChat);
    }

    public void removeUserFromChat(Long chatId, Long userId) {
        userChatRepository.deleteByChatIdAndUserId(chatId, userId);
    }

    public Chat createChat(Long driverId) {
        Chat chat = new Chat();
        chat.setDriverId(driverId);
        chat.setChatStatus("open");
        Chat savedChat = chatRepository.save(chat);
        UserChat userChat = new UserChat();
        userChat.setChatId(savedChat.getId());
        userChat.setUserId(driverId);
        userChatRepository.save(userChat);

        return savedChat;
    }


    @Autowired
    public void setUserRepository(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setChatRepository(IChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Autowired
    public void setMessageRepository(IMessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Autowired
    public void setUserChatRepository(IUserChatRepository userChatRepository) {
        this.userChatRepository = userChatRepository;
    }

    @Autowired
    public void setTripRepository(ITripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

}
