package com.example.uilsback.controller;

import com.example.uilsback.dto.ChatDTO;
import com.example.uilsback.dto.MessageRequestDTO;
import com.example.uilsback.exception.CustomException;
import com.example.uilsback.service.interfaces.IChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private IChatService chatService;

    @PreAuthorize("hasAuthority('driver') || hasAuthority('passenger')")
    @GetMapping("/{tripId}")
    public ChatDTO getAllChats(
            @PathVariable("tripId") Long tripId
    ) {
        return chatService.getChat(tripId);
    }

    @PreAuthorize("hasAuthority('driver') || hasAuthority('passenger')")
    @PostMapping("/send/{tripId}")
    public ResponseEntity<?> sendMessage(
            @PathVariable("tripId") Long tripId,
            @RequestHeader("idUsuario") String userId,
            @RequestBody MessageRequestDTO message
    ) {
        if(chatService.sendMessage(tripId, Long.parseLong(userId), message.getMessage())){
            return ResponseEntity.ok("true");
        } else {
            throw new CustomException("No se pudo enviar el mensaje");
        }
    }

    @Autowired
    public void setChatService(@Qualifier("chatServiceImpl") IChatService chatService) {
        this.chatService = chatService;
    }

}
