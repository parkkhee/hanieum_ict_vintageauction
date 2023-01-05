package hello.hellospring.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "vintageboard")
@NoArgsConstructor
@Getter @Setter
public class VintageBoard extends BaseTimeEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vintageId;
    private String vintageTitle;
    private String vintageDetail;


    @JsonBackReference // 양방향 관계에서 json 순화참조 에러 해결하기 위해서 넣었다.
    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "itemId") //1:1 관계에서는 FK를 가지는 쪽이 연관관계의 주인이다. 그래서 @JoinColumn 여기다 넣었다.
    private Item vintageItem;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberNo")
    private Member member;

    @JsonManagedReference // 양방향 관계에서 json 순화참조 에러 해결하기 위해서 넣었다.
    @OneToOne(mappedBy = "vintageBoard", fetch = FetchType.LAZY, orphanRemoval = true)
    private ItemDealHistory VintageDealHistory;



    //==연관관계 메서드==// -> 양방향 관계에서 사용한다.
    //중고거래 게시물 등록할 때 사용자 설정
    public void setMember(Member member){
        this.member = member;
        member.getVintageBoardList().add(this);
    }

    //연관관계 메서드 - 양방향 관계에서 사용된다.
    //중고거래 게시물 등록할 때 item 관련 연관관계 편의 메서드
    public void setVintageItem(Item vintageItem) {
        this.vintageItem = vintageItem;
        vintageItem.setVintageBoard(this);
    }

    @Builder
    public VintageBoard(Long vintageId, String vintageTitle, String vintageDetail, Item vintageItem, Member member) {
        this.vintageId = vintageId;
        this.vintageTitle = vintageTitle;
        this.vintageDetail = vintageDetail;
        this.vintageItem = vintageItem;
        this.member = member;
    }
}
