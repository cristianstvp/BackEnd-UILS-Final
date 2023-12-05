package com.example.uilsback.dto;

import com.example.uilsback.model.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatDTO {

    Long id;

    String chatStatus;

    String driverName;

    List<MessageDTO> messages;

}
