package hello.hellospring.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @Column(nullable = false)
    private String itemName;

    private String itemCategory;

    private Integer itemPrice;

    private String itemImage;
    private Integer itemBidPrice; //입찰 시작 가격
    private Integer itemBidEndPrice; //최종 입찰 가격

    //판매중, 판매완료 -> 채팅창에서 한 기억이...
    @Enumerated(EnumType.STRING)
    private ItemStatus itemStatus;

    @JsonManagedReference
    @OneToOne(mappedBy = "vintageItem", fetch = FetchType.LAZY)
    private VintageBoard vintageBoard;

    @OneToOne(mappedBy = "auctionItem", fetch = FetchType.LAZY)
    private AuctionBoard auctionBoard;


    @JsonManagedReference // 양방향 관계에서 json 순화참조 에러 해결하기 위해서 넣었다.
    @OneToMany(mappedBy = "uploadItem", orphanRemoval = true) //mappedBy의 value는 연관관계의 주인 컬럼명을 적는다.
    private List<UploadFile> uploadFiles = new ArrayList<>();



    //vintageBoard, auctionBoard 등록 시, 사용하는 builder
    @Builder
    public Item(String itemName, Integer itemPrice, Integer itemBidPrice, String itemCategory) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemCategory = itemCategory;
        this.itemBidPrice = itemBidPrice;
        this.itemStatus = ItemStatus.PROGRESSING;
    }
}
