package com.goldencat.chatapp.service;

import com.goldencat.chatapp.model.ChatRoom;
import com.goldencat.chatapp.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    @Autowired
    private  ChatRoomRepository chatRoomRepository;

    public Optional<String> getChatRoomId(
        String senderId,
        String receiverId,
        boolean createRoomIfNotExist
    ) {
        return chatRoomRepository
                .findBySenderIdAndReceiverId(senderId, receiverId)
                .map(ChatRoom::getChatId)
                .or(() -> {
                    if (createRoomIfNotExist) {
                        var newChatRoomId = createChatId(senderId, receiverId);
                        return Optional.of(newChatRoomId);
                    }

                    return Optional.empty();
                });
    }

    private String createChatId(String senderId, String receiverId) {
        var chatId = String.format("%s_%s", senderId, receiverId);

        ChatRoom senderReceiver = new ChatRoom();
        senderReceiver.setChatId(chatId);
        senderReceiver.setSenderId(senderId);
        senderReceiver.setReceiverId(receiverId);

        ChatRoom receiverSender = new ChatRoom();
        receiverSender.setChatId(chatId);
        receiverSender.setSenderId(receiverId);
        receiverSender.setReceiverId(senderId);


        chatRoomRepository.save(senderReceiver);
        chatRoomRepository.save(receiverSender);

        return chatId;
    }
}
