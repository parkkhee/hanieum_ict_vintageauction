package hello.hellospring.repository;

import hello.hellospring.domain.BidRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BidRecordRepository extends JpaRepository<BidRecord, Long> {
}