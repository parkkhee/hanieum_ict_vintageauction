package hello.hellospring.repository;


import hello.hellospring.domain.VintageBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VintageRepository extends JpaRepository<VintageBoard, Long> {
    Page<VintageBoard> findByVintageTitleContaining(@Param("vintageTitle") String vintageTitle, Pageable pageable); //Containing을 붙이면 like sql작동 //검색시

    @Query(value="select * from vintageboard where vintageboard.item_id in (:itemList)", nativeQuery = true)
    Page<VintageBoard> findByVintageItem_ItemId(List<Long> itemList, Pageable pageable);

    @Query("select v from VintageBoard v where v.vintageItem.itemId=:itemId")
    VintageBoard findByVintageItem(Long itemId);

    @Query("select v from VintageBoard v join v.vintageItem vi where vi.itemCategory =:itemCategory")
    Page<VintageBoard> findVintageBoardsByItemCategory(@Param("itemCategory") String itemCategory, Pageable pageable);
}
