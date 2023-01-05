package hello.hellospring.service;

import hello.hellospring.dto.request.BidRequestDto;
import hello.hellospring.dto.response.BidResponseDto;

public interface BidService {
    BidResponseDto bidSave(BidRequestDto bidRequestDto);
    BidResponseDto bidSelect(Long auctionId);
}
