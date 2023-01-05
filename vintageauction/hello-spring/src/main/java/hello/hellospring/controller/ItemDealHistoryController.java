package hello.hellospring.controller;

import hello.hellospring.domain.ItemDealHistory;
import hello.hellospring.dto.response.ItemDealHistoryDto;
import hello.hellospring.service.ItemDealHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
@Transactional
public class ItemDealHistoryController {

    private final ItemDealHistoryService itemDealHistoryService;


    @PostMapping("/api/vintage/deal")
    public ResponseEntity<?> deal(@RequestParam("vintageBoardId") Long vintageBoardId, HttpServletRequest request){

        //로그인을 해서 버튼을 누른 사용자 PK 인 memberNo 가져오기
        HttpSession session = request.getSession(false);
        Long buyerNo = (Long)session.getAttribute("memberNo");

        ItemDealHistory itemDealHistory = itemDealHistoryService.deal(vintageBoardId, buyerNo);
        ItemDealHistoryDto result = new ItemDealHistoryDto(itemDealHistory);

//            Map<String, Object> result = new HashMap<>();
//            result.put("seller", itemDealHistory.getSeller().getMemberId());
//            result.put("buyer", itemDealHistory.getBuyer().getMemberId());
//            result.put("vintageItem", itemDealHistory.getVintageBoard().getVintageItem());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
