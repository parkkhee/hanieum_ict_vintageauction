package hello.hellospring.service;

import hello.hellospring.domain.ChatRoom;

import java.util.Optional;

public interface ChatRoomService {

    //채팅방 가져오기, 없을 시 신규 생성
    public Optional<ChatRoom> findChatRoom(Long itemId, Long senderNo, boolean createIfNotExist);

}
