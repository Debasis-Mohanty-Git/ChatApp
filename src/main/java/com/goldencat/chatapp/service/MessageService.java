package com.goldencat.chatapp.service;

import com.goldencat.chatapp.model.Message;
import com.goldencat.chatapp.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {
    @Autowired
    private  MessageRepository messageRepository;
    @Autowired
    private  ChatRoomService chatRoomService;

    public Message save(Message message) {
        var chatId = chatRoomService
                .getChatRoomId(message.getSenderId(), message.getReceiverId(), true)
                .orElseThrow(() -> new RuntimeException("Chat room could not be created or found"));
        message.setChatId(chatId);
        return messageRepository.save(message);
    }

    public List<Message> getChatMessage(String senderId, String receiverId) {
        var chatId = chatRoomService.getChatRoomId(senderId, receiverId, false);
        return chatId.map(messageRepository::findByChatId).orElse(new ArrayList<>());
    }
}
