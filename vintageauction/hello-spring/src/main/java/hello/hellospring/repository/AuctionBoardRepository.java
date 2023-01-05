package hello.hellospring.repository;

import hello.hellospring.domain.AuctionBoard;
import hello.hellospring.domain.AuctionStatus;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface AuctionBoardRepository extends JpaRepository<AuctionBoard, Long> {
    //경매가 끝난 AuctionBoard 찾기
    @Query(value = "select a from AuctionBoard a where a.endTime < :time and a.auctionStatus = :status")
    List<AuctionBoard> findByAuctionEndTime(@Param("time") LocalDateTime time, @Param("status") AuctionStatus status);

    //제목으로 경매상품 검색
    Page<AuctionBoard> findByAuctionTitleContaining(@Param("auctionTitle") String auctionTitle, Pageable pageable); //Containing을 붙이면 like sql작동 //검색시

    //카테고리로 경매상품 검색
    @Query("select a from AuctionBoard a join a.auctionItem ai where ai.itemCategory =:itemCategory")
    Page<AuctionBoard> findAuctionBoardsByItemCategory(@Param("itemCategory") String itemCategory, Pageable pageable);

}
