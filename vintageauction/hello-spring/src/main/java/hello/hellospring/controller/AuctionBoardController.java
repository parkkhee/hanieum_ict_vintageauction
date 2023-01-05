package hello.hellospring.controller;

import hello.hellospring.domain.AuctionBoard;
import hello.hellospring.dto.request.AuctionBoardForm;
import hello.hellospring.dto.response.AuctionBoardDetailDto;
import hello.hellospring.dto.response.AuctionBoardDto;
import hello.hellospring.dto.response.AuctionResult;
import hello.hellospring.dto.response.Result;
import hello.hellospring.service.AuctionService;
import hello.hellospring.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AuctionBoardController {

    private final AuctionService auctionService;
    private final RedisTemplate redisTemplate;
    private final ItemService itemService;


    // CREATE - 경매등록 로직 -> 등록이 성공적으로 완료되면 main 페이지로 이동한다.
    @PostMapping("/api/auction/new")
    public ResponseEntity<?> createVintage(@Valid @ModelAttribute AuctionBoardForm auctionForm,
                                           BindingResult bindingResult,
                                           List<MultipartFile> imageFiles,
                                           HttpServletRequest request) throws Exception {
        if(bindingResult.hasErrors()){
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            Map<String, Object> result = new HashMap<>();
            for (FieldError fieldError : fieldErrors) {
                result.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }


        //세션에 저장된 로그인 한 회원 정보 가져오기
        HttpSession session = request.getSession();
        Long memberNo = (Long)session.getAttribute("memberNo");

        AuctionBoard saveAuctionBoard = auctionService.save(auctionForm, memberNo, imageFiles);
        return new ResponseEntity<>("경매 상품 등록 완료",HttpStatus.OK);
    }

    //READ - 경매 상품목록 조회 -> 번호, 제목, 작성자Id 보여지기
    @GetMapping("/api/auctions")
    public ResponseEntity<?> vintageList( @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<AuctionBoard> auctionBoards = auctionService.findAll(page);

        if(page != 0 && page >= auctionBoards.getTotalPages()){
            return new ResponseEntity<>("해당 페이지는 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }

        //프록시 객체 초기화 + DTO 로 변환
        List<AuctionBoard> content = auctionBoards.getContent();
        List<AuctionBoardDto> result = content.stream()
                .map(v -> new AuctionBoardDto(v))
                .collect(Collectors.toList());

        return new ResponseEntity<>(new AuctionResult<>(page+1,auctionBoards.getTotalPages(),result), HttpStatus.OK);
    }

    //READ - 경매상품 상세 조회 -> 제목 / 아이템명, 아이템 가격, 아이템 이미지 / 설명
    @GetMapping("/api/auction/{auctionBoardId}")
    public ResponseEntity<?> vintageDetail(@PathVariable("auctionBoardId") Long auctionBoardId) throws IOException {
        Optional<AuctionBoard> findAuctionBoard = auctionService.findById(auctionBoardId);
        AuctionBoard auctionBoard = findAuctionBoard.get();

        AuctionBoardDetailDto result = new AuctionBoardDetailDto(auctionBoard);


        //누군가 입찰 했으면 입찰한 가격으로 보여준다.
        ZSetOperations zop = redisTemplate.opsForZSet();
        LinkedHashSet set = (LinkedHashSet)zop.reverseRangeWithScores("auction" + auctionBoardId, 0, 0);
        Object[] objects = set.toArray();
        if(!set.isEmpty()){
            for (Object object : objects) {
                DefaultTypedTuple tuple = (DefaultTypedTuple) object;
                String bidder = tuple.getValue().toString();
                int bidPrice = tuple.getScore().intValue();

                result.setItemBidPrice(bidPrice);
            }
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //UPDATE - 경매상품 업데이트
    @PostMapping("/api/auction/{auctionBoardId}/edit")
    public ResponseEntity<?> updateVintage(@PathVariable("auctionBoardId") Long auctionBoardId,
                                           @Valid @ModelAttribute AuctionBoardForm auctionForm,
                                           BindingResult bindingResult,
                                           List<MultipartFile> imageFiles,
                                           HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession(false);
        Long memberNo = (Long)session.getAttribute("memberNo");


        if(bindingResult.hasErrors()){
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            Map<String, Object> result = new HashMap<>();
            for (FieldError fieldError : fieldErrors) {
                result.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }

        AuctionBoard updateAuctionBoard = auctionService.update(auctionBoardId, auctionForm, memberNo, imageFiles);


        return new ResponseEntity<>("경매 상품 게시물 업데이트 완료", HttpStatus.OK);

    }

    //DELETE - 경매상품 삭제
    @PostMapping("/api/auction/{auctionBoardId}/delete")
    public ResponseEntity<?> deleteVintage(@PathVariable("auctionBoardId") Long auctionBoardId,
                                           HttpServletRequest request){
        HttpSession session = request.getSession(false);
        Long memberNo = (Long)session.getAttribute("memberNo");

        auctionService.delete(auctionBoardId, memberNo);

        return new ResponseEntity<>("경매상품 삭제 완료", HttpStatus.OK);
    }

    //READ - 중고상품 검색(auctionTitle 검색)
    @GetMapping("/api/auctions/search") //page:default 페이지, size:한 페이지 게시글 수, sort:정렬기준컬럼, direction:정렬순서
    public ResponseEntity<?> search(@RequestParam("auctionTitle") String auctionTitle,
                                    @RequestParam(value = "page", defaultValue = "0") int page){

        Page<AuctionBoard> auctionBoards = auctionService.searchTitle(auctionTitle, page);

        if(page != 0 && page >= auctionBoards.getTotalPages()){
            return new ResponseEntity<>("해당 페이지는 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }

        //프록시 객체 초기화 + DTO 로 변환
        List<AuctionBoard> content = auctionBoards.getContent();
        List<AuctionBoardDto> result = content.stream()
                .map(v -> new AuctionBoardDto(v))
                .collect(Collectors.toList());

        return new ResponseEntity<>(new Result<>(page+1,auctionBoards.getTotalPages(),result), HttpStatus.OK);

    }

    //READ - 중고상품 검색(ItemCategory 검색)
    @GetMapping("/api/auctions/category")
    public ResponseEntity<?> category(@RequestParam("itemCategory") String itemCategory,
                                      @RequestParam(value = "page", defaultValue = "0") int page){
        Page<AuctionBoard> auctionBoards = auctionService.searchItemCategory(itemCategory, page);

        if(page != 0 && page >= auctionBoards.getTotalPages()){
            return new ResponseEntity<>("해당 페이지는 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }

        //프록시 객체 초기화 + DTO 로 변환
        List<AuctionBoard> content = auctionBoards.getContent();
        List<AuctionBoardDto> result = content.stream()
                .map(v -> new AuctionBoardDto(v))
                .collect(Collectors.toList());

        return new ResponseEntity<>(new Result<>(page+1,auctionBoards.getTotalPages(),result), HttpStatus.OK);

    }
}
