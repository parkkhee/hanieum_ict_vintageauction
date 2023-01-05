package hello.hellospring.service;

import hello.hellospring.domain.UploadFile;

import java.util.List;

public interface UploadFileService {
    //1. 이미지 경로 저장
    List<UploadFile> save(List<UploadFile> uploadFileList);

}
