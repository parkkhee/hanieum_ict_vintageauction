package hello.hellospring.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Getter
@Setter
@AllArgsConstructor
@Builder
public class ChatNotification {
    private Long id;
    private Long senderNo;

}
