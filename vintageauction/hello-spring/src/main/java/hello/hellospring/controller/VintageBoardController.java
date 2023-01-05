package hello.hellospring.controller;


import hello.hellospring.domain.VintageBoard;
import hello.hellospring.dto.request.VintageBordForm;
import hello.hellospring.dto.response.Result;
import hello.hellospring.dto.response.VintageBoardDetailDto;
import hello.hellospring.dto.response.VintageBoardDto;
import hello.hellospring.service.ItemService;
import hello.hellospring.service.VintageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
public class VintageBoardController {

    private final VintageService vintageService;
    private final ItemService itemService;


    // CREATE - 중고등록 로직 -> 등록이 성공적으로 완료되면 main 페이지로 이동한다.
    @PostMapping("/api/vintage/new")
    public ResponseEntity<?> createVintage(@Valid @ModelAttribute VintageBordForm vintageForm,
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

        VintageBoard saveVintageBoard = vintageService.save(vintageForm, memberNo, imageFiles);
        return new ResponseEntity<>("중고 상품 등록 완료",HttpStatus.OK);
    }

    //READ - 중고상품목록 조회 -> 번호, 제목, 작성자Id 보여지기
    @GetMapping("/api/vintages")
    public ResponseEntity<?> vintageList( @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<VintageBoard> vintageBoards = vintageService.findAll(page);

        if(page != 0 && page >= vintageBoards.getTotalPages()){
            return new ResponseEntity<>("해당 페이지는 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }

        //프록시 객체 초기화 + DTO 로 변환
        List<VintageBoard> content = vintageBoards.getContent();
        List<VintageBoardDto> result = content.stream()
                .map(v -> new VintageBoardDto(v))
                .collect(Collectors.toList());

        return new ResponseEntity<>(new Result<>(page+1,vintageBoards.getTotalPages(),result), HttpStatus.OK);
    }

    //READ - 중고상품 상세 조회 -> 제목 / 아이템명, 아이템 가격, 아이템 이미지 / 설명
    @GetMapping("/api/vintage/{vintageBoardId}")
    public ResponseEntity<?> vintageDetail(@PathVariable("vintageBoardId") Long vintageBoardId) throws IOException {
        Optional<VintageBoard> findVintageBoard = vintageService.findById(vintageBoardId);
        VintageBoard vintageBoard = findVintageBoard.get();

        VintageBoardDetailDto result = new VintageBoardDetailDto(vintageBoard);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //UPDATE - 중고상품 업데이트
    @PostMapping("/api/vintage/{vintageBoardId}/edit")
    public ResponseEntity<?> updateVintage(@PathVariable("vintageBoardId") Long vintageBoardId,
                                           @Valid @ModelAttribute VintageBordForm vintageForm,
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

        VintageBoard updateVintageBoard = vintageService.update(vintageBoardId, vintageForm, memberNo, imageFiles);


        return new ResponseEntity<>("중고 상품 게시물 업데이트 완료", HttpStatus.OK);

    }

    //DELETE - 중고상품 삭제
    @PostMapping("/api/vintage/{vintageBoardId}/delete")
    public ResponseEntity<?> deleteVintage(@PathVariable("vintageBoardId") Long vintageBoardId,
                                           HttpServletRequest request){
        HttpSession session = request.getSession(false);
        Long memberNo = (Long)session.getAttribute("memberNo");

        vintageService.delete(vintageBoardId, memberNo);

        return new ResponseEntity<>("게시물 삭제 완료", HttpStatus.OK);
    }

    //READ - 중고상품 검색(vintageTitle 검색)
    @GetMapping("/api/vintages/search") //page:default 페이지, size:한 페이지 게시글 수, sort:정렬기준컬럼, direction:정렬순서
    public ResponseEntity<?> search(@RequestParam("vintageTitle") String vintageTitle,
                                    @RequestParam(value = "page", defaultValue = "0") int page){

        Page<VintageBoard> vintageBoards = vintageService.searchTitle(vintageTitle, page);

        if(page != 0 && page >= vintageBoards.getTotalPages()){
            return new ResponseEntity<>("해당 페이지는 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }

        //프록시 객체 초기화 + DTO 로 변환
        List<VintageBoard> content = vintageBoards.getContent();
        List<VintageBoardDto> result = content.stream()
                .map(v -> new VintageBoardDto(v))
                .collect(Collectors.toList());

        return new ResponseEntity<>(new Result<>(page+1,vintageBoards.getTotalPages(),result), HttpStatus.OK);

    }

    //READ - 중고상품 검색(ItemCategory 검색)
    @GetMapping("/api/vintages/category")
    public ResponseEntity<?> category(@RequestParam("itemCategory") String itemCategory,
                                      @RequestParam(value = "page", defaultValue = "0") int page){
        Page<VintageBoard> vintageBoards = vintageService.searchItemCategory(itemCategory, page);

        if(page != 0 && page >= vintageBoards.getTotalPages()){
            return new ResponseEntity<>("해당 페이지는 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }

        //프록시 객체 초기화 + DTO 로 변환
        List<VintageBoard> content = vintageBoards.getContent();
        List<VintageBoardDto> result = content.stream()
                .map(v -> new VintageBoardDto(v))
                .collect(Collectors.toList());

        return new ResponseEntity<>(new Result<>(page+1,vintageBoards.getTotalPages(),result), HttpStatus.OK);

    }

}
