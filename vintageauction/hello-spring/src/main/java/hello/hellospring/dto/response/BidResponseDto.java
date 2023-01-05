package hello.hellospring.dto.response;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BidResponseDto {
    private Long auctionId;
    private Long bidderId;
    private Long bidPrice;
}
