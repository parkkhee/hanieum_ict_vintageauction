package hello.hellospring.service;

import hello.hellospring.domain.AuctionBoard;
import hello.hellospring.domain.AuctionStatus;
import hello.hellospring.domain.Member;
import hello.hellospring.dto.request.BidRequestDto;
import hello.hellospring.dto.response.BidResponseDto;
import hello.hellospring.exception.BadRequestException;
import hello.hellospring.repository.AuctionBoardRepository;
import hello.hellospring.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {

    private final AuctionBoardRepository auctionBoardRepository;
    private final MemberRepository memberRepository;
    private final RedisTemplate redisTemplate;


    @Override
    public BidResponseDto bidSave(BidRequestDto bidRequestDto) {


        //어떤 경매 인지
        AuctionBoard auctionBoard = auctionBoardRepository.findById(bidRequestDto.getAuctionId()).get();

        /**
         * 끝난 경매이면 입찰을 못하게 막는다.
         */
        if(auctionBoard.getAuctionStatus().equals(AuctionStatus.COMPLETED) || auctionBoard.getAuctionStatus().equals(AuctionStatus.FAILED)){
            throw new BadRequestException("이미 종료된 경매 입니다.");
        }

        //어떤 사용자가 입찰을 하는지
        Optional<Member> bymember = memberRepository.findById(bidRequestDto.getBidderId());
        Member bidMember = bymember.get();

        /**
         * 포인트가 모자란 경우
         */
        if (bidRequestDto.getBidPrice() > bidMember.getMemberPoint()){
            throw new BadRequestException("포인트가 모자랍니다.");
        }
        /**
         * 내가 등록한 경매는 입찰을 못한다.
         */
        if(auctionBoard.getMember() == bidMember){
            throw new BadRequestException("자신의 경매는 입찰하지 못합니다.");
        }


        ZSetOperations zop = redisTemplate.opsForZSet();
        Set set = zop.reverseRange("auction" + auctionBoard.getAuctionId(), 0, 0);

        //아직 입찰을 안 한 경매인 경우
        if (set.isEmpty()) {
            if (auctionBoard.getAuctionItem().getItemBidPrice() >= bidRequestDto.getBidPrice()) {
                throw new RuntimeException("입찰 시작 가격보다 높은 가격으로 입찰해 주세요.");
            } else {
                /**
                 * 내 포인트 차감하기
                 */
                bidMember.setMemberPoint(bidMember.getMemberPoint() - bidRequestDto.getBidPrice());
                memberRepository.save(bidMember);

                zop.add("auction" + auctionBoard.getAuctionId(), bidMember.getMemberName(), bidRequestDto.getBidPrice());
            }
        }
        //누군가 입찰을 한 경매인 경우
        else {
            String highestBidder = set.toString();
            highestBidder = highestBidder.substring(1, highestBidder.length() - 1);
            Long highestPrice = zop.score("auction" + auctionBoard.getAuctionId(), highestBidder).longValue();


            //상품의 현재 입찰 가격보다 높아야지 입찰이 가능하다.
            if (bidRequestDto.getBidPrice() <= highestPrice) {
                throw new BadRequestException("현재 입찰 가격보다 높은 가격으로 입찰 해 주세요.");
            }
            //입찰하기
            else {
                /**
                 * 이전에 입찰했던 사람 포인트 돌려주고 내 포인트 차감하기
                 */
                LinkedHashSet topBidder = (LinkedHashSet)zop.reverseRangeWithScores("auction" + auctionBoard.getAuctionId(), 0, 0);
                Object[] objects = topBidder.toArray();
                for (Object object : objects) {
                    DefaultTypedTuple tuple = (DefaultTypedTuple) object;
                    String bidder = tuple.getValue().toString();
                    long bidPrice = tuple.getScore().longValue();

                    Member byMemberName = memberRepository.findByMemberName(bidder);
                    byMemberName.setMemberPoint(byMemberName.getMemberPoint() + bidPrice);

                }
                bidMember.setMemberPoint(bidMember.getMemberPoint() - bidRequestDto.getBidPrice());
                memberRepository.save(bidMember);

                zop.add("auction" + auctionBoard.getAuctionId(), bidMember.getMemberName(), bidRequestDto.getBidPrice());
            }
        }
        BidResponseDto bidResponseDto = new BidResponseDto(auctionBoard.getAuctionId(), bidMember.getMemberNo(), bidRequestDto.getBidPrice());

        return bidResponseDto;
    }

    @Override
    public BidResponseDto bidSelect(Long auctionId) {
        return null;
    }
}
