package hello.hellospring.service;

import hello.hellospring.domain.*;
import hello.hellospring.exception.BadRequestException;
import hello.hellospring.exception.PointLackException;
import hello.hellospring.repository.ItemDealHistoryRepository;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.VintageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ItemDealHistoryImpl implements ItemDealHistoryService {

    private final ItemDealHistoryRepository itemDealHistoryRepository;
    private final VintageRepository vintageRepository;
    private final MemberRepository memberRepository;

    @Override
    public ItemDealHistory deal(Long vintageBoardId, Long buyerNo) {
        //판매자 가져오기
        Optional<VintageBoard> findVintageBoard = vintageRepository.findById(vintageBoardId);
        if (findVintageBoard.isEmpty()) {
            throw new NoSuchElementException("존재 하지 않은 중고 상품 입니다.");
        }
        VintageBoard vintageBoard = findVintageBoard.get();
        Member seller = vintageBoard.getMember();

        //구매자 가져오기
        Optional<Member> findBuyer = memberRepository.findById(buyerNo);
        Member buyer = findBuyer.get();

        //아이템 정보 가져오고 포인트 이동하기
        Item vintageItem = vintageBoard.getVintageItem();
        Integer itemPrice = vintageItem.getItemPrice();

        if (vintageItem.getItemStatus() == ItemStatus.COMPLETED) {
            throw new BadRequestException("이미 판매 된 중고 상품 입니다.");
        }

        if(buyer.getMemberId() == seller.getMemberId()){
            throw new BadRequestException("자신의 중고 물품을 구매할 수 없습니다.");
        }

        if (buyer.getMemberPoint() < itemPrice) {
            //구매자의 포인트가 itemPrice 보다 적으면 예외 처리
            throw new PointLackException("구매자의 포인트가 모자랍니다.");
        }

        //포인트 교환
        buyer.setMemberPoint(buyer.getMemberPoint() - itemPrice);
        seller.setMemberPoint(seller.getMemberPoint() + itemPrice);


        //거래내역 저장
        ItemDealHistory itemDealHistory = new ItemDealHistory();
        itemDealHistory.setBuyer(buyer);
        itemDealHistory.setSeller(seller);
        itemDealHistory.setVintageBoard(vintageBoard);

        ItemDealHistory saveItemDealHistory = itemDealHistoryRepository.save(itemDealHistory);

        //vintageItem을 판매완료 상태로 전환하고 ItemDealHistory 저장
        vintageItem.setItemStatus(ItemStatus.COMPLETED);

        return saveItemDealHistory;
    }
}
