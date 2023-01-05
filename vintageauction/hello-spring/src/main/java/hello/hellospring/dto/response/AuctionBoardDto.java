package hello.hellospring.dto.response;


import hello.hellospring.domain.AuctionBoard;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuctionBoardDto {
    private Long auctionId;
    private String auctionTitle;
    private List<String> storeFileNames;

    public AuctionBoardDto(AuctionBoard auctionBoard){
        auctionId = auctionBoard.getAuctionId();
        auctionTitle = auctionBoard.getAuctionTitle();
        storeFileNames = auctionBoard.getAuctionItem().getUploadFiles().stream()
                .map(uploadFile -> uploadFile.getStoreFileName())
                .collect(Collectors.toList());
    }
}