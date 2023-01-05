package hello.hellospring.service;

import hello.hellospring.domain.AuctionBoard;
import hello.hellospring.dto.request.AuctionBoardForm;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface AuctionService {
    //경매상품 등록 - Creat
    AuctionBoard save(AuctionBoardForm auctionBoardForm, Long memberNo, List<MultipartFile> imageFiles) throws IOException;

    //경매상품 수정 - Update
    AuctionBoard update(Long auctionBoardId, AuctionBoardForm auctionBoardForm, Long memberNo, List<MultipartFile> imageFiles) throws IOException;

    //경매상품 삭제 - Delete
    void delete(Long auctionBoardId, Long memberNo);

    //경매상품 Page로 단위로 조회 -Read
    Page<AuctionBoard> findAll(int page);
    //경개상품 제목으로 조회 - Read
    Page<AuctionBoard> searchTitle(String auctionTitle, int page);
    //경매상품 카테고리로 조회 - Read
    Page<AuctionBoard> searchItemCategory(String itemCategory, int page);

    //경매상품 상세 조회 - Read
    Optional<AuctionBoard> findById(Long auctionBoardId);


}
