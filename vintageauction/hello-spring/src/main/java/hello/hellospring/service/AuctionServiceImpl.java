package hello.hellospring.service;

import hello.hellospring.domain.*;
import hello.hellospring.dto.request.AuctionBoardForm;
import hello.hellospring.exception.UnauthorizedException;
import hello.hellospring.file.FileStore;
import hello.hellospring.repository.AuctionBoardRepository;
import hello.hellospring.repository.ItemRepository;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.UploadFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class AuctionServiceImpl implements AuctionService {

    private final AuctionBoardRepository auctionBoardRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final UploadFileRepository uploadFileRepository;
    private final FileStore fileStore;

    @Override
    public AuctionBoard save(AuctionBoardForm auctionBoardForm, Long memberNo, List<MultipartFile> imageFiles) throws IOException {
        //DTO -> Entity
        Item item = auctionBoardForm.auctionItemFormtoEntity();
        AuctionBoard auctionBoard = auctionBoardForm.auctionFormtoEntity();


        //아이템 저장 후, vintageBoard에 item 세팅
        Item saveItem = itemRepository.save(item);
        auctionBoard.setAuctionItem(saveItem);

        //회원 조회 후, vintageBoard에 작성자 회원 세팅
        Optional<Member> member = memberRepository.findById(memberNo);
        auctionBoard.setMember(member.get());


        //이미지 파일 저장하기 - 스토리지에 저장
        //List<MultipartFile> imageFiles = vintageForm.getImageFiles();
        if (imageFiles != null) {
            List<UploadFile> uploadFiles = fileStore.storeFiles(imageFiles);

            //DB에 이미지 파일이름 저장(UploadFile 은  /올린 파일명/UUID파일명/저장된 fullPath 로 구성
            for (UploadFile uploadFile : uploadFiles) {
                uploadFile.setUploadItem(saveItem);
                uploadFileRepository.save(uploadFile);
            }
        }

        //경매 시작*마감 시간 설정
        auctionBoard.setStartTime(LocalDateTime.now());
        auctionBoard.setEndTime(LocalDateTime.now().plusSeconds(auctionBoardForm.getDuringTime()));
        auctionBoard.setAuctionStatus(AuctionStatus.PROGRESSING);


        //auctionBoard 저장
        AuctionBoard saveAuctionBoard = auctionBoardRepository.save(auctionBoard);

        return saveAuctionBoard;
    }

    @Override
    public AuctionBoard update(Long auctionBoardId, AuctionBoardForm auctionBoardForm,
                               Long memberNo, List<MultipartFile> imageFiles) throws IOException {
        //로그인 한 사용자 조회
        Optional<Member> findMember = memberRepository.findById(memberNo);
        Member member = findMember.get();

        //게시물 작성자 조회
        Optional<AuctionBoard> findAuctionBoard = auctionBoardRepository.findById(auctionBoardId);
        AuctionBoard auctionBoard = findAuctionBoard.get();

        //로그인 한 사용와 게시물 작성자가 다르면 예외 발생
        if(!member.getMemberId().equals(auctionBoard.getMember().getMemberId())){
            throw new UnauthorizedException("상품을 등록한 회원이 아닙니다.");
        }


        //AuctionBoard 내용 수정
        auctionBoard.setAuctionTitle(auctionBoardForm.getAuctionTitle());
        auctionBoard.setAuctionDetail(auctionBoardForm.getAuctionDetail());

        //Item 내용 수정
        Item findItem = auctionBoard.getAuctionItem();
        findItem.setItemBidPrice(auctionBoardForm.getItemBidPrice());
        findItem.setItemName(auctionBoardForm.getItemName());
        findItem.setItemCategory(auctionBoardForm.getItemCategory());


        //수정 할 이미지 넣기
        //List<MultipartFile> imageFiles = vintageForm.getImageFiles();
        if(imageFiles != null) {
            //기존의 이미지 삭제하기 -> 스토리지, db에서 모두 삭제
            List<UploadFile> existFiles = auctionBoard.getAuctionItem().getUploadFiles();
            for (UploadFile existFile : existFiles) {
                fileStore.deleteFile(existFile.getFullPath());
                uploadFileRepository.deleteById(existFile.getFileId());
            }

            //Item 에 연결된 이미지 리스트 초기화
            findItem.getUploadFiles().clear();

            List<UploadFile> uploadFiles = fileStore.storeFiles(imageFiles);

            //DB에 이미지 정보들 저장
            for (UploadFile updateFile : uploadFiles) {
                updateFile.setUploadItem(findItem);
                uploadFileRepository.save(updateFile);
            }
        }

        return auctionBoard;
    }

    @Override
    public void delete(Long auctionBoardId, Long memberNo) {
        //로그인 한 사용자 조회
        Optional<Member> findMember = memberRepository.findById(memberNo);
        Member member = findMember.get();

        //게시물 작성자 조회
        Optional<AuctionBoard> findAuctionBoard = auctionBoardRepository.findById(auctionBoardId);
        if (findAuctionBoard.isEmpty()) {
            throw new NoSuchElementException("존재 하지 않은 게시물 입니다.");
        }
        AuctionBoard auctionBoard = findAuctionBoard.get();

        //로그인 한 사용와 게시물 작성자가 다르면 예외 발생
        if (!member.getMemberId().equals(auctionBoard.getMember().getMemberId())) {
            throw new UnauthorizedException("상품을 등록한 회원이 아닙니다.");
        }

        //스토리지에서 사진 삭제
        List<UploadFile> existFiles = auctionBoard.getAuctionItem().getUploadFiles();
        for (UploadFile existFile : existFiles) {
            fileStore.deleteFile(existFile.getFullPath());
            uploadFileRepository.deleteById(existFile.getFileId());
        }

        //db 저장정보 삭제
        auctionBoardRepository.delete(auctionBoard);
    }

    @Override
    public Page<AuctionBoard> findAll(int page) {
        Page<AuctionBoard> auctionBoards = auctionBoardRepository.findAll(
                PageRequest.of(page, 2, Sort.by(Sort.Direction.DESC, "startTime"))
        );
        return auctionBoards;
    }

    @Override
    public Page<AuctionBoard> searchTitle(String vintageTitle, int page) {
        PageRequest pageRequest = PageRequest.of(page, 2, Sort.by("startTime").descending());
        Page<AuctionBoard> auctionBoards = auctionBoardRepository.findByAuctionTitleContaining(vintageTitle, pageRequest);
        return auctionBoards;
    }

    @Override
    public Page<AuctionBoard> searchItemCategory(String itemCategory, int page) {
        PageRequest pageRequest = PageRequest.of(page, 2, Sort.by("startTime").descending());
        Page<AuctionBoard> auctionBoards = auctionBoardRepository.findAuctionBoardsByItemCategory(itemCategory, pageRequest);
        return auctionBoards;
    }

    @Override
    public Optional<AuctionBoard> findById(Long auctionBoardId) {
        Optional<AuctionBoard> findAuctionBoard = auctionBoardRepository.findById(auctionBoardId);
        if(findAuctionBoard.isEmpty()){
            throw new NoSuchElementException("존재 하지 않은 게시물 입니다.");
        }
        return findAuctionBoard;
    }
}

