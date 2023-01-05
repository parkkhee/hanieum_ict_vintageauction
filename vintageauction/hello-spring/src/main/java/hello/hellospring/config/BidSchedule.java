package hello.hellospring.config;


import hello.hellospring.domain.*;
import hello.hellospring.repository.AuctionBoardRepository;
import hello.hellospring.repository.BidRecordRepository;
import hello.hellospring.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BidSchedule {
    private final AuctionBoardRepository auctionRepository;
    private final BidRecordRepository bidRecordRepository;
    private final MemberRepository memberRepository;
    private final RedisTemplate redisTemplate;

    //Sorted Set 을 이용한  스케줄
    @Scheduled(cron = "* * * * * *")
    @Transactional
    public void auctionClose() {
        //경매가 현재 "Progessing" 상태 이면서 마감 시간이 지난 것을 가져온다.
        LocalDateTime curTime = LocalDateTime.now();
        List<AuctionBoard> byAuctionEndTime = auctionRepository.findByAuctionEndTime(curTime, AuctionStatus.PROGRESSING);

        if(byAuctionEndTime.isEmpty()) {
            System.out.println("마감된 경매가 없습니다.");
        }
        //마감된 경매라면
        else{
            ZSetOperations zop = redisTemplate.opsForZSet();
            for (AuctionBoard auction : byAuctionEndTime) {
                LinkedHashSet set = (LinkedHashSet)zop.reverseRangeWithScores("auction" + auction.getAuctionId(), 0, -1);
                Object[] objects = set.toArray();

                //입찰한 사람이 없으면 FAILED 상태로 변경
                if(set.isEmpty()){
                    auction.setAuctionStatus(AuctionStatus.FAILED);
                    auctionRepository.save(auction);

                    //경매 아이템 상태 변경
                    auction.getAuctionItem().setItemStatus(ItemStatus.FAILED);
                }
                //누군가가 입찰을 했으면 입찰 기록 세팅(성공한 사람, 실패한 사람)
                else{
                    int cnt = 0;
                    for (Object object : objects) {
                        DefaultTypedTuple tuple = (DefaultTypedTuple) object;
                        String bidder = tuple.getValue().toString();
                        long bidPrice = tuple.getScore().longValue();

                        Member findMember = memberRepository.findByMemberName(bidder);

                        /**
                         * 여기는 입찰 기록 남기기(낙찰자, 미낙찰자)
                         * 낙찰 시, 경매 등록한 사람한테 포인트 이동
                         */
                        if(cnt == 0){ //낙찰자 + 경매 등록한 사람한테 포인트 이동
                            BidRecord bidRecord = new BidRecord();
                            bidRecord.setStatus(AuctionStatus.COMPLETED);
                            bidRecord.setBidMember(findMember);
                            bidRecord.setBidPrice(bidPrice);
                            bidRecord.setAuction(auction);
                            bidRecordRepository.save(bidRecord);

                            auction.setAuctionStatus(AuctionStatus.COMPLETED);
                            auctionRepository.save(auction);

                            //경매 마감 가격 설정 + 경매 아이템 상태 변경
                            auction.getAuctionItem().setItemBidEndPrice(tuple.getScore().intValue());
                            auction.getAuctionItem().setItemStatus(ItemStatus.COMPLETED);

                            //경매 등록한 사람한테 포인트 이동
                            Member member = auction.getMember();
                            member.setMemberPoint(member.getMemberPoint() + bidPrice);
                        }else{ //미낙찰자
                            BidRecord bidRecord = new BidRecord();
                            bidRecord.setStatus(AuctionStatus.FAILED);
                            bidRecord.setBidMember(findMember);
                            bidRecord.setBidPrice(bidPrice);
                            bidRecord.setAuction(auction);
                            bidRecordRepository.save(bidRecord);

                            auction.setAuctionStatus(AuctionStatus.COMPLETED);
                            auctionRepository.save(auction);
                        }
                        cnt++;
                    }
                }
            }
        }
    }
}