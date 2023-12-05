package com.example.uilsback.mapper;

import com.example.uilsback.dto.ChatDTO;
import com.example.uilsback.dto.MessageDTO;
import com.example.uilsback.dto.TripResponseDTO;
import com.example.uilsback.model.Chat;
import com.example.uilsback.model.Message;
import com.example.uilsback.model.Trip;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mapper
public interface IChatMapper {

    IChatMapper INSTANCE = Mappers.getMapper(IChatMapper.class);

    @Mapping(source = "driver.fullName", target = "driverName")
    ChatDTO chatToChatDTO(Chat chat);

    /*@AfterMapping
    default void fillArrays(Chat chat, @MappingTarget ChatDTO dto) {
        List<MessageDTO> messages = IMessageMapper.INSTANCE.messagesToMessageDTOs(chat.getMessages());

        dto.setMessages(messages);
    }*/

}
