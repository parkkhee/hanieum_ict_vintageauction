package hello.hellospring.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Member implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberNo;

    @Column(nullable = false, unique = true)
    private String memberId;

    @Column(nullable = false)
    private String memberName;

    @Column(nullable = false)
    private String memberPassword;

    private String memberImgUrl;

    private Long memberPoint;

    @JsonManagedReference // 양방향 관계에서 json 순화참조 에러 해결하기 위해서 넣었다.
    @OneToMany(mappedBy = "member", orphanRemoval = true) //1:N 관계에서 one 쪽이 삭제되면 자식도 같이 삭제
    private List<VintageBoard> vintageBoardList = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "auctionId", orphanRemoval = true)
    private List<AuctionBoard> auctionBoardList = new ArrayList<>();


    //입찰 기록
    @OneToMany(mappedBy = "bidMember")
    List<BidRecord> bidRecords = new ArrayList<>();


    //구매한 기록
    @JsonManagedReference()
    @OneToMany(mappedBy = "seller", orphanRemoval = true)
    private List<ItemDealHistory> sellHistory = new ArrayList<>();

    //판매한 기록
    @JsonManagedReference
    @OneToMany(mappedBy = "buyer", orphanRemoval = true)
    private List<ItemDealHistory> buyHistory = new ArrayList<>();

    //구매자로서 채팅방 기록
    @JsonManagedReference
    @OneToMany(mappedBy = "buyerNo", orphanRemoval = true)
    private List<ChatRoom> buyChatRoomList = new ArrayList<>();

    //판매자로서 채팅방 기록
    @JsonManagedReference
    @OneToMany(mappedBy = "sellerNo", orphanRemoval = true)
    private List<ChatRoom> sellChatRoomList = new ArrayList<>();




    //DTO 클래스의 toEntity() 에서 사용하기 위해서 선언
    @Builder
    public Member(String memberId, String memberName, String memberPassword, String memberImgUrl){
        this.memberId = memberId;
        this.memberName = memberName;
        this.memberPassword = memberPassword;
        this.memberImgUrl = memberImgUrl;
    }

    //회원 정보수정을 위한
    public void memberUpdate(String memberPassword){
        this.memberPassword = memberPassword;
    }

    public void memberImgUrlUpdate(String memberImgUrl) {
        this.memberImgUrl = memberImgUrl;
    }

}
