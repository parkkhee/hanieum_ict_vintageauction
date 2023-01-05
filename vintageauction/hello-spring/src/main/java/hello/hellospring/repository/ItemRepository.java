package hello.hellospring.repository;

import hello.hellospring.domain.Item;
import hello.hellospring.domain.Member;
import hello.hellospring.domain.VintageBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Item findByItemId(Long itemId);

    @Query(value="select item_id from item where item.item_category= :itemCategory", nativeQuery = true)
    List<Long> findByItemCategory(@Param("itemCategory") String itemCategory);
}
