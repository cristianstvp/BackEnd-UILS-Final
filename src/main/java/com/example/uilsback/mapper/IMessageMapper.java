package com.example.uilsback.mapper;

import com.example.uilsback.dto.ChatDTO;
import com.example.uilsback.dto.MessageDTO;
import com.example.uilsback.model.Chat;
import com.example.uilsback.model.Message;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

@Mapper
public interface IMessageMapper {

    IMessageMapper INSTANCE = Mappers.getMapper(IMessageMapper.class);

    MessageDTO messageToMessageDTO(Message message);

    List<MessageDTO> messagesToMessageDTOs(List<Message> messages);

    @AfterMapping
    default void fillContent(Message message, @MappingTarget MessageDTO dto) {
        dto.setMessage(message.getMessageContent());
        dto.setSenderName(message.getUser().getFullName());
        dto.setSenderName(message.getUser().getFullName());
        dto.setTime(message.getTimestamp().toString());
        dto.setSenderId(message.getUserId());
    }

}
