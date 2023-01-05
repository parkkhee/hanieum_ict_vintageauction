package hello.hellospring.service;

import hello.hellospring.domain.VintageBoard;
import hello.hellospring.dto.request.VintageBordForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface VintageService {
    //1. 중고거래 글 등록(게시글 내용, 아이템 정보를 받아온다.)
    VintageBoard save(VintageBordForm vintageForm, Long memberId, List<MultipartFile> imageFiles) throws IOException;

    //2. 중고거래 글 수정
    VintageBoard update(Long vintageId, VintageBordForm vintageForm, Long memberNo, List<MultipartFile> imageFiles) throws IOException;

    //3. 중고거래 글 삭제
    void delete(Long vintageId, Long memberNo);

    //3. 중고거래 글 전부 다 조회
    Page<VintageBoard> findAll(int page);

    //4. 특정 중고거래 글 검색하기
    Optional<VintageBoard> findById(Long vintageBoardId);
    Page<VintageBoard> searchTitle(String vintageTitle, int page);
    Page<VintageBoard> searchItemCategory(String itemCategory, int page);

    //5. 중고거래 글 특정 단어로 검색
}
