package hello.hellospring.controller;

import hello.hellospring.dto.request.BidRequestDto;
import hello.hellospring.dto.response.BidResponseDto;
import hello.hellospring.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
public class BidController {

    private final BidService bidService;

    //입찰하기
    @PostMapping("/api/auction/{auctionId}/bid")
    public ResponseEntity<?> bid(@RequestBody BidRequestDto bidRequestDto,
                                 @PathVariable("auctionId") Long auctionId,
                                 HttpServletRequest request) {
        //입찰하는 MemberNo 가져오기(로그인 해서 입찰을 시도하는 memberNo)
        HttpSession session = request.getSession(false);
        Long memberNo = (Long)session.getAttribute("memberNo");

        bidRequestDto.setBidderId(memberNo);
        bidRequestDto.setAuctionId(auctionId);

        BidResponseDto bidResponseDto = bidService.bidSave(bidRequestDto);
        return new ResponseEntity<>(bidResponseDto, HttpStatus.OK);
    }
}
