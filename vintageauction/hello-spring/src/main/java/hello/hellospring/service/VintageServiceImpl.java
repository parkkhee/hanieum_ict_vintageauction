package hello.hellospring.service;


import hello.hellospring.domain.Item;
import hello.hellospring.domain.Member;
import hello.hellospring.domain.UploadFile;
import hello.hellospring.domain.VintageBoard;
import hello.hellospring.dto.request.VintageBordForm;

import hello.hellospring.exception.UnauthorizedException;
import hello.hellospring.file.FileStore;

import hello.hellospring.repository.ItemRepository;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.UploadFileRepository;
import hello.hellospring.repository.VintageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class VintageServiceImpl implements VintageService{

    private final VintageRepository vintageRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final UploadFileRepository uploadFileRepository;
    private final FileStore fileStore;

    @Override
    public VintageBoard save(VintageBordForm vintageForm, Long memberId, List<MultipartFile> imageFiles) throws IOException {
        //DTO -> Entity
        Item item = vintageForm.itemFormtoEntity();
        VintageBoard vintageBoard = vintageForm.vintageFormtoEntity();


        //아이템 저장 후, vintageBoard에 item 세팅
        Item saveItem = itemRepository.save(item);
        vintageBoard.setVintageItem(saveItem);

        //회원 조회 후, vintageBoard에 작성자 회원 세팅
        Optional<Member> member = memberRepository.findById(memberId);
        vintageBoard.setMember(member.get());


        //이미지 파일 저장하기 - 스토리지에 저장
        //List<MultipartFile> imageFiles = vintageForm.getImageFiles();
        if(imageFiles != null){
            List<UploadFile> uploadFiles = fileStore.storeFiles(imageFiles);

            //DB에 이미지 파일이름 저장(UploadFile 은  /올린 파일명/UUID파일명/저장된 fullPath 로 구성
            for (UploadFile uploadFile : uploadFiles) {
                uploadFile.setUploadItem(saveItem);
                uploadFileRepository.save(uploadFile);
            }
        }


        //vintageBoard 저장
        VintageBoard saveVintageBoard = vintageRepository.save(vintageBoard);

        return saveVintageBoard;
    }


    @Override
    public Page<VintageBoard> findAll(int page) {
        Page<VintageBoard> vintageBoards = vintageRepository.findAll(
                PageRequest.of(page, 2, Sort.by(Sort.Direction.DESC, "createdTime"))
        );
        return vintageBoards;
    }

    @Override
    public VintageBoard update(Long vintageId,VintageBordForm vintageForm,
                               Long memberNo, List<MultipartFile> imageFiles) throws IOException {

        //로그인 한 사용자 조회
        Optional<Member> findMember = memberRepository.findById(memberNo);
        Member member = findMember.get();

        //게시물 작성자 조회
        Optional<VintageBoard> findVintageBoard = vintageRepository.findById(vintageId);
        VintageBoard vintageBoard = findVintageBoard.get();

        //로그인 한 사용와 게시물 작성자가 다르면 예외 발생
        if(!member.getMemberId().equals(vintageBoard.getMember().getMemberId())){
            throw new UnauthorizedException("상품을 등록한 회원이 아닙니다.");
        }


        //VintageBoard 내용 수정
        vintageBoard.setVintageTitle(vintageForm.getVintageTitle());
        vintageBoard.setVintageDetail(vintageForm.getVintageDetail());

        //Item 내용 수정
        Item findItem = vintageBoard.getVintageItem();
        findItem.setItemPrice(vintageForm.getItemPrice());
        findItem.setItemName(vintageForm.getItemName());
        findItem.setItemCategory(vintageForm.getItemCategory());


        //수정 할 이미지 넣기
        //List<MultipartFile> imageFiles = vintageForm.getImageFiles();
        if(imageFiles != null) {
            //기존의 이미지 삭제하기 -> 스토리지, db에서 모두 삭제
            List<UploadFile> existFiles = vintageBoard.getVintageItem().getUploadFiles();
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

        return vintageBoard;
    }


    @Override
    public Optional<VintageBoard> findById(Long vintageBoardId) {
        Optional<VintageBoard> findVintageBoard = vintageRepository.findById(vintageBoardId);
        if(findVintageBoard.isEmpty()){
            throw new NoSuchElementException("존재 하지 않은 게시물 입니다.");
        }
        return findVintageBoard;
    }


    @Override
    public void delete(Long vintageId, Long memberNo) {

        //로그인 한 사용자 조회
        Optional<Member> findMember = memberRepository.findById(memberNo);
        Member member = findMember.get();

        //게시물 작성자 조회
        Optional<VintageBoard> findVintageBoard = vintageRepository.findById(vintageId);
        if (findVintageBoard.isEmpty()) {
            throw new NoSuchElementException("존재 하지 않은 게시물 입니다.");
        }
        VintageBoard vintageBoard = findVintageBoard.get();

        //로그인 한 사용와 게시물 작성자가 다르면 예외 발생
        if (!member.getMemberId().equals(vintageBoard.getMember().getMemberId())) {
            throw new UnauthorizedException("상품을 등록한 회원이 아닙니다.");
        }

        //스토리지에서 사진 삭제
        List<UploadFile> existFiles = vintageBoard.getVintageItem().getUploadFiles();
        for (UploadFile existFile : existFiles) {
            fileStore.deleteFile(existFile.getFullPath());
            uploadFileRepository.deleteById(existFile.getFileId());
        }

        //db 저장정보 삭제
        vintageRepository.delete(vintageBoard);
    }

    //중고게시글 검색 Page로 repository에서 받아와서 return
    @Transactional
    @Override
    public Page<VintageBoard> searchTitle(String vintageTitle, int page){
        PageRequest pageRequest = PageRequest.of(page, 2, Sort.by("createdTime").descending());
        Page<VintageBoard> vintageBoards = vintageRepository.findByVintageTitleContaining(vintageTitle, pageRequest);
        return vintageBoards;
    }

    @Override
    public Page<VintageBoard> searchItemCategory(String itemCategory, int page) {
        PageRequest pageRequest = PageRequest.of(page, 2, Sort.by("createdTime").descending());
        Page<VintageBoard> vintageBoards = vintageRepository.findVintageBoardsByItemCategory(itemCategory, pageRequest);
        return vintageBoards;
    }

}

