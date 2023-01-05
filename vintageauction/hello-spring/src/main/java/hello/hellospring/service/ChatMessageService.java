package hello.hellospring.service;

import hello.hellospring.domain.ChatMessage;
import hello.hellospring.dto.ChatRequestDto;

import java.util.List;

public interface ChatMessageService {
    //수신한 채팅 저장
    public ChatMessage save(ChatRequestDto chatRequestDto);

    //안 읽은 채팅 개수 표시
    public long countNewMessages(Long senderNo, Long receiverNo);

    //채팅 status update
    public void updateStatuses(Long senderNo, Long receiverNo, ChatMessage.MessageStatus status);

    ChatMessage addChatroomId(Long id, Long chatroomId);
}
