package com.example.uilsback.dto;

import lombok.Data;

@Data
public class MessageDTO {

    Long id;

    Long senderId;

    String senderName;

    String message;

    String time;

}
