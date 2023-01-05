package hello.hellospring.dto.request;

import hello.hellospring.domain.AuctionBoard;
import hello.hellospring.domain.Item;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Getter
public class AuctionBoardForm {

    //VintageBoard 영역
    @NotEmpty
    @NotBlank
    private String auctionTitle;
    @NotEmpty
    @NotBlank
    private String auctionDetail;

    //Item 영역
    @NotEmpty
    @NotBlank
    private String itemName;

    @NotNull //NotEmpty 는 String 타입에만 가능하다.
    private Integer itemBidPrice; //입찰 시작 가격

    @NotEmpty
    @NotBlank
    private String itemCategory;

    @NotNull
    private Integer duringTime; //경매 지속 시간(시간단위)


    //private List<MultipartFile> imageFiles; //이미지 파일들


    public AuctionBoard auctionFormtoEntity(){
        return AuctionBoard.builder()
                .auctionTitle(auctionTitle)
                .auctionDetail(auctionDetail)
                .build();
    }

    public Item auctionItemFormtoEntity(){
        return Item.builder()
                .itemName(itemName)
                .itemBidPrice(itemBidPrice)
                .itemCategory(itemCategory)
                .build();
    }
}