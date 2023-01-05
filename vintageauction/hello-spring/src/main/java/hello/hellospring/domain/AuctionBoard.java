package hello.hellospring.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "auctionboard")
@Getter
@Setter
@NoArgsConstructor
public class AuctionBoard{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long auctionId;
    private String auctionTitle;
    private String auctionDetail;

    //경매 상태 -> 입찰 중, 입찰 실패, 경매 완료
    @Enumerated(EnumType.STRING)
    private AuctionStatus auctionStatus;

    //경매 시작 날짜, 마감 날짜
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime endTime;

    //경매 등록한 사람
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="memberId")
    private Member member;

    //경매 아이템에 대한 정보
    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "itemId")
    private Item auctionItem;

    //해당 경매에 대한 입찰 기록들 -> 경매가 끝나야지 기록된다.
    @JsonManagedReference
    @OneToMany(mappedBy = "auction", orphanRemoval = true)
    private List<BidRecord> bidRecordList = new ArrayList<>();

    //==연관관계 메서드==// -> 양방향 관계에서 사용한다.
    //경매 게시물 등록할 때 사용자 설정
    public void setMember(Member member) {
        this.member = member;
        member.getAuctionBoardList().add(this);
    }

    //연관관계 메서드 - 양방향 관계에서 사용된다.
    //중고경매 게시물 등록할 때 item 관련 연관관계 편의 메서드
    public void setAuctionItem(Item auctionItem) {
        this.auctionItem = auctionItem;
        auctionItem.setAuctionBoard(this);
    }

    @Builder
    public AuctionBoard(Long auctionId, String auctionTitle, String auctionDetail, Item auctionItem, Member member) {
        this.auctionId = auctionId;
        this.auctionTitle = auctionTitle;
        this.auctionDetail = auctionDetail;
        this.auctionItem = auctionItem;
        this.member = member;
    }


}
