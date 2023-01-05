package hello.hellospring.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "bidrecord")
@Getter
@Setter
public class BidRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recordId;
    private Long bidPrice; //입찰가격

    @Enumerated(EnumType.STRING)
    private AuctionStatus status; // 경매상태(입찰 중, 입찰 실패, 완료)

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member bidMember; //입찰 한 사람

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "auctionId")
    private AuctionBoard auction; //경매 상품



    public void setBidMember(Member bidMember) {
        this.bidMember = bidMember;
        bidMember.getBidRecords().add(this);
    }

    public void setAuction(AuctionBoard auction) {
        this.auction = auction;
        auction.getBidRecordList().add(this);
    }
}
