package hello.hellospring.dto.response;

import hello.hellospring.domain.UploadFile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class UploadFileDto {
    private String fullPath;

    public UploadFileDto(UploadFile uploadFile) {
        fullPath = uploadFile.getFullPath();
    }
}
