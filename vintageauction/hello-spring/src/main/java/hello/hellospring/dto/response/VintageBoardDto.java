package hello.hellospring.dto.response;

import hello.hellospring.domain.VintageBoard;
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
public class VintageBoardDto {
    private Long vintageId;
    private String vintageTitle;
    private List<String> storeFileNames;

    public VintageBoardDto(VintageBoard vintageBoard){
        vintageId = vintageBoard.getVintageId();
        vintageTitle = vintageBoard.getVintageTitle();
        storeFileNames = vintageBoard.getVintageItem().getUploadFiles().stream()
                .map(uploadFile -> uploadFile.getStoreFileName())
                .collect(Collectors.toList());
    }

}
