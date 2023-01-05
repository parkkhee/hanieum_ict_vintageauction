package hello.hellospring.service;

import hello.hellospring.domain.Item;
import hello.hellospring.domain.VintageBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemService {
    //1. 아이템 저장
    Item save(Item item);

    //2. 카테고리로 아이템 검색
    List<Long> findByCategory(String itemCategory);
}
