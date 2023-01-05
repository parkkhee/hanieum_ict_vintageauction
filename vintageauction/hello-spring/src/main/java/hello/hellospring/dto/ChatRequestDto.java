package hello.hellospring.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;
import hello.hellospring.domain.ChatMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Component
public class ChatRequestDto {
    /**
     * 송신자 id
     */
    @NotNull
    private Long senderNo;

    /**
     * 수신자 id
     */
    @NotNull
    private Long receiverNo;

    /**
     * 채팅방 id
     */
    @NotNull
    private Long chatroomId;

    /**
     * 메시지 내용
     */
    @NotBlank
    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime sendDateTime;

    public ChatRequestDto(Long senderNo, Long receiverNo, Long chatroomId, String content, LocalDateTime sendDateTime) {
        this.senderNo = senderNo;
        this.receiverNo = receiverNo;
        this.chatroomId = chatroomId;
        this.content = content;
        this.sendDateTime = sendDateTime;
    }



    public ChatMessage toEntity() {
        return ChatMessage.builder()
                .senderNo(senderNo)
                .receiverNo(receiverNo)
                .content(content)
                .sendDateTime(sendDateTime)
                .build();
    }
}
