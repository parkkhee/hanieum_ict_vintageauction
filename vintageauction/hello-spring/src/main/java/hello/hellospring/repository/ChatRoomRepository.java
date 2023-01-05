package hello.hellospring.repository;

import hello.hellospring.domain.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
@Component
public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {

    @Query("select c from ChatRoom c where c.item.itemId=:itemId and c.buyerNo.memberNo=:buyerNo")
    Optional<ChatRoom> findByItemAndBuyer(Long itemId, Long buyerNo);

    Optional<ChatRoom> findById(Long id);

    @Query("select c from ChatRoom c where c.sellerNo.memberNo=:sellerNo")
    List<ChatRoom> findBySellerNo(Long sellerNo);

    @Query("select c from ChatRoom c where c.buyerNo.memberNo=:buyerNo")
    List<ChatRoom> findByBuyerNo(Long buyerNo);

}
