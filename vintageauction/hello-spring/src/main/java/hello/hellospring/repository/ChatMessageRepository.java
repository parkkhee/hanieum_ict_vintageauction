package hello.hellospring.repository;

import hello.hellospring.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
@Component
public interface ChatMessageRepository extends JpaRepository<ChatMessage, String> {
    long countBySenderNoAndReceiverNoAndStatus(
            Long senderNo, Long receiverNo, ChatMessage.MessageStatus status);

    @Query("select c from ChatMessage c where " +
            "(c.senderNo=:senderNo or c.senderNo=:receiverNo) " +
            "and (c.receiverNo=:receiverNo or c.receiverNo=:senderNo)")
    List<ChatMessage> findBySenderNoAndReceiverNo(
            Long senderNo, Long receiverNo);

    List<ChatMessage> findByChatroomId(Long id);

    Optional<ChatMessage> findById(Long id);

}
