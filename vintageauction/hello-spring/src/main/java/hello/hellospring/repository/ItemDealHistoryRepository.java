package hello.hellospring.repository;

import hello.hellospring.domain.ItemDealHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemDealHistoryRepository extends JpaRepository<ItemDealHistory, Long> {
}
