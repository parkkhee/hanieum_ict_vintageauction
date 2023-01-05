package hello.hellospring.dto.response;

import hello.hellospring.domain.UploadFile;
import hello.hellospring.domain.VintageBoard;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

//중고상품 검색시 필요한 Dto
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VintageSearchDto {
    private Long vintageId;
    private String vintageTitle;
    private List<UploadFile> uploadFiles;
}
