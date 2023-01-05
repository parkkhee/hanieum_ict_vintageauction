package hello.hellospring.dto.response;

import hello.hellospring.domain.ItemDealHistory;
import lombok.Data;

@Data
public class ItemDealHistoryDto {
    private Long vintageBoardId;
    private String seller;
    private String buyer;
    private String itemName;
    private int itemPrice;

    public ItemDealHistoryDto(ItemDealHistory itemDealHistory) {
        vintageBoardId = itemDealHistory.getVintageBoard().getVintageId();
        seller = itemDealHistory.getSeller().getMemberId();
        buyer = itemDealHistory.getBuyer().getMemberId();
        itemName = itemDealHistory.getVintageBoard().getVintageItem().getItemName();
        itemPrice = itemDealHistory.getVintageBoard().getVintageItem().getItemPrice();
    }
}
