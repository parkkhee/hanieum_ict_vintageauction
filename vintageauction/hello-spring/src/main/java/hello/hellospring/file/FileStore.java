package hello.hellospring.file;

import hello.hellospring.domain.UploadFile;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Component
public class FileStore {

    @Value("${file.dir}")
    private String fileDir;

    //fullPath 반환
    public String getFullPath(String filename){
        return fileDir + filename;
    }

    //파일 저장
    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        //1.클라이언트가 올린 파일명 뽑아오기
        //2.서버에 저장할 파일명 가져오기
        //3.파일 저장하기
        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        multipartFile.transferTo(new File(getFullPath(storeFileName)));

        return new UploadFile(originalFilename, storeFileName, getFullPath(storeFileName));
    }


    //여러 개의 파일을 업로드 할 경우
    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<UploadFile> storeFileResult = new ArrayList<>();

        //multipart를 돌면서 sotreFile을 호출해서 여러 개를 저장한다.
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                UploadFile uploadFile = storeFile(multipartFile);
                storeFileResult.add(uploadFile);
            }
        }

        return storeFileResult;
    }


    //파일 삭제
    public void deleteFile(String fullPath) {
        new File(fullPath).delete();
    }


    //UUID + 확장자 = 서버에 저장 파일명 추출
    private String createStoreFileName(String originalFilename) {
        //업로드 한 확장자 뽑아오기
        String ext = extractExt(originalFilename);
        //서버에 저장하는 파일명 - UUID 이용
        String uuid = UUID.randomUUID().toString();
        //UUID + 확장자
        String storeFileName = uuid + "." + ext;
        return storeFileName;
    }

    //확장자 추출하기
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        String ext = originalFilename.substring(pos + 1);
        return ext;
    }

    //저장된 파일명 불러오기
    private String getStoreFileName(String abcd){
        return null;
    }



}
