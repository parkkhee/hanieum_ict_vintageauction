package hello.hellospring.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class UploadFile {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    private String uploadFileName; //업로드 되는 파일명
    private String storeFileName; //저장되는 파일명 -> UUID 이용하기
    private String fullPath; // 저장되는 경로 + 저장되는 파일명


    @JsonBackReference // 양방향 관계에서 json 순화참조 에러 해결하기 위해서 넣었다
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemId") //name 속성의 value는 매핑이 되는 Entity의 컬럼명을 적어준다.
    private Item uploadItem;

    public UploadFile(String uploadFileName, String storeFileName, String fullPath) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
        this.fullPath = fullPath;
    }

    //연관관계 편의 메서드
    public void setUploadItem(Item uploadItem) {
        this.uploadItem = uploadItem;
        uploadItem.getUploadFiles().add(this);
    }
}
