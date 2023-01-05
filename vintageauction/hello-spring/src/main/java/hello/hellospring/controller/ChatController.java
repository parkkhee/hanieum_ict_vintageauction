package hello.hellospring.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hello.hellospring.domain.ChatMessage;
import hello.hellospring.domain.ChatNotification;
import hello.hellospring.domain.ChatRoom;
import hello.hellospring.dto.ChatRequestDto;
import hello.hellospring.repository.ChatMessageRepository;
import hello.hellospring.repository.ChatRoomRepository;
import hello.hellospring.service.ChatMessageService;
import hello.hellospring.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ChatController {


    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    //내 채팅방 목록 불러오기
    @GetMapping("/api/chatroom")
    public ResponseEntity<?> listChatRoom(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        Long memberNo = (Long) session.getAttribute("memberNo");

        List<ChatRoom> sellChatRoom = chatRoomRepository.findBySellerNo(memberNo);
        List<ChatRoom> buyChatRoom = chatRoomRepository.findByBuyerNo(memberNo);

        List<ChatRoom> chatRoomList = new ArrayList<>();
        chatRoomList.addAll(sellChatRoom);
        chatRoomList.addAll(buyChatRoom);

        return new ResponseEntity<>(chatRoomList, HttpStatus.OK);
    }

    //판매자에게 채팅 처음 생성 시 채팅방 생성,저장
    @PostMapping("/api/chat/new")
    public ResponseEntity<?> createChatRoom(@RequestParam("receiverNo") int receiverNo,
                                            @RequestParam("itemId") int itemId,
                                            HttpServletRequest request){
        HttpSession session = request.getSession(false);
        Long senderNo = (Long) session.getAttribute("memberNo");

        Optional<ChatRoom> chatRoom = chatRoomService.findChatRoom(Long.valueOf(itemId),senderNo, true);

        return new ResponseEntity<>(chatRoom, HttpStatus.OK);
    }

    //생성된 채팅에 진입, 채팅 기록 불러오기
    @GetMapping("/api/chat/{chatroomId}")
    public ResponseEntity<?> enterChatRoom(@PathVariable String chatroomId,
                                  HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession(false);

        List<ChatMessage> chatMessageList =  chatMessageRepository.findByChatroomId(Long.valueOf(chatroomId));

        return new ResponseEntity<>(chatMessageList, HttpStatus.OK);
    }


    @MessageMapping("/chat")  //여기로 전송되면 메서드 호출
    public void processMessage(@Payload String msg) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        ChatRequestDto chatRequestDto = mapper.readValue(msg, ChatRequestDto.class);
        chatRequestDto.setSendDateTime(LocalDateTime.now());

        //메시지 저장하기
        ChatMessage saved = chatMessageService.save(chatRequestDto);

        //수신 유저에게 메시지 수신 알림 보내기
        simpMessagingTemplate.convertAndSendToUser(
                String.valueOf(chatRequestDto.getReceiverNo()),"/user/{receiverNo}/queue/messages",
                new ChatNotification(
                        saved.getId(),
                        saved.getSenderNo()));
    }


    @GetMapping("/messages/{senderNo}/{receiverNo}/count")
    public ResponseEntity<Long> countNewMessages(
            @PathVariable Long senderNo,
            @PathVariable Long receiverNo) {

        return ResponseEntity
                .ok(chatMessageService.countNewMessages(senderNo, receiverNo));
    }


}