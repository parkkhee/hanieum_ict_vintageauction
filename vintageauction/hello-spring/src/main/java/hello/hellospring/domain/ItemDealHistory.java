package hello.hellospring.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemDealHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemDealHistoryId;

    @JsonBackReference // 양방향 관계에서 json 순화참조 에러 해결하기 위해서 넣었다.
    @ManyToOne(fetch = FetchType.LAZY) //~~ToOne 에는 무조건 FetchType.LAZY로 설정
    @JoinColumn(referencedColumnName = "memberNo", name = "sellerMemberNo")
    private Member seller;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "memberNo", name = "buyderMemberNo")
    private Member buyer;

    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vintageId")
    private VintageBoard vintageBoard;


    //연관관계 편의 메서드 1:N 관계에서 N을 생성할 때 해준다.
    //연관관계 편의 메서드 1:1 관계에서 연관관계의 주인인 쪽을 생성할 때 해준다.
    public void setBuyer(Member buyer) {
        this.buyer = buyer;
        buyer.getBuyHistory().add(this);
    }
    public void setSeller(Member seller) {
        this.seller = seller;
        seller.getSellHistory().add(this);
    }
    public void setVintageBoard(VintageBoard vintageBoard) {
        this.vintageBoard = vintageBoard;
        vintageBoard.setVintageDealHistory(this);
    }

}
