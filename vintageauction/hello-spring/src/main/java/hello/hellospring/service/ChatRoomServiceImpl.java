package hello.hellospring.service;

import hello.hellospring.domain.ChatRoom;
import hello.hellospring.domain.Item;
import hello.hellospring.domain.Member;
import hello.hellospring.domain.VintageBoard;
import hello.hellospring.repository.ChatRoomRepository;
import hello.hellospring.repository.ItemRepository;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.VintageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final VintageRepository vintageRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;


    public Optional<ChatRoom> findChatRoom(Long itemId, Long senderNo, boolean createIfNotExist) {
        /*
        *채팅 시작은 구매자(sender)->판매자(receiver)만 가능
        *채팅메시지가 들어오면 채팅방을 찾아서 리턴/ 없을 시
        */

        return chatRoomRepository
                .findByItemAndBuyer(itemId, senderNo)
                .or(() -> { //없을 시 생성
                    if(!createIfNotExist) {
                        return  Optional.empty();
                    }
                    VintageBoard vintageBoard = vintageRepository.findByVintageItem(itemId);
                    Item item = itemRepository.findByItemId(itemId);
                    Member buyer = memberRepository.findByMemberNo(senderNo).orElseThrow(() -> new IllegalArgumentException("보내는 이가 등록되지 않은 사용자입니다."));
                    Member seller = vintageBoard.getMember();
                    System.out.println(" ");
                    System.out.println("seller : " + seller);
                    System.out.println(" ");

                    ChatRoom newChatRoom = ChatRoom
                            .builder()
                            .item(item)
                            .buyerNo(buyer)
                            .sellerNo(seller)
                            .build();

                    chatRoomRepository.save(newChatRoom);

                    return chatRoomRepository.findByItemAndBuyer(itemId, senderNo);
                });
    }
}
