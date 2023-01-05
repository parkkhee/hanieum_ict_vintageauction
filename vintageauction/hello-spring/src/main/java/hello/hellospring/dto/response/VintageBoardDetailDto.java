package hello.hellospring.dto.response;

import hello.hellospring.domain.VintageBoard;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class VintageBoardDetailDto {
    private String title;
    private String detail;
    private String itemName;
    private int itemPrice;
    private String itemCategory;
    private List<String> storeFileName;
    private String memberId;

    private Long memberNo;

    public VintageBoardDetailDto(VintageBoard vintageBoard) {
        title = vintageBoard.getVintageTitle();
        detail = vintageBoard.getVintageDetail();
        itemName = vintageBoard.getVintageItem().getItemName();
        itemPrice = vintageBoard.getVintageItem().getItemPrice();
        itemCategory = vintageBoard.getVintageItem().getItemCategory();
        storeFileName = vintageBoard.getVintageItem().getUploadFiles().stream()
                .map(storeFileName -> storeFileName.getStoreFileName())
                .collect(Collectors.toList());
        memberId = vintageBoard.getMember().getMemberName();
        memberNo = vintageBoard.getMember().getMemberNo();
    }
}
