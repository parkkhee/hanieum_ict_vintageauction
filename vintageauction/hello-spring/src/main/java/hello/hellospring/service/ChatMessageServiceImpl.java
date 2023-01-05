package hello.hellospring.service;

import hello.hellospring.domain.ChatMessage;
import hello.hellospring.domain.ChatRoom;
import hello.hellospring.domain.Member;
import hello.hellospring.domain.VintageBoard;
import hello.hellospring.dto.ChatRequestDto;
import hello.hellospring.exception.UnauthorizedException;
import hello.hellospring.repository.ChatMessageRepository;
import hello.hellospring.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService{
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    //수신한 채팅 저장
    public ChatMessage save(ChatRequestDto chatRequestDto){
        ChatMessage chatMessage = chatRequestDto.toEntity();
        chatMessage.setStatus(ChatMessage.MessageStatus.RECEIVED);
        //회원 조회 후, vintageBoard에 작성자 회원 세팅
        Optional<ChatRoom> chatroom= chatRoomRepository.findById(chatRequestDto.getChatroomId());
        /*
        * TODO: ChatRequestDto에 itemId 추가, itemId 함께 저장
        * */
        chatMessage.setChatroom(chatroom.get());
        chatMessageRepository.save(chatMessage);
        return chatMessage;
    }

    //안 읽은 채팅 개수 표시
    public long countNewMessages(Long senderNo, Long receiverNo) {
        return chatMessageRepository.countBySenderNoAndReceiverNoAndStatus(
                senderNo, receiverNo, ChatMessage.MessageStatus.RECEIVED);
    }




    @Query
    public void updateStatuses(Long senderNo, Long receiverNo, ChatMessage.MessageStatus status) {
        //UPDATE jpadb SET STATUS = status where senderId = senderId and receiverId = receiverId
//        Query query = new Query(
//                Criteria
//                        .where("senderId").is(senderId)
//                        .and("receiverId").is(receiverId));
//        Update update = Update.update("status", status);
    }

    @Override
    public ChatMessage addChatroomId(Long id, Long chatroomId) {
        return null;
    }

}
