package hello.hellospring.service;


import hello.hellospring.domain.Member;
import hello.hellospring.dto.MemberInfoDto;
import hello.hellospring.dto.request.MemberSignupDto;

import hello.hellospring.dto.request.MemberUpdateDto;
import hello.hellospring.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor //final 키워드가 붙은 것을 생성자 의존 관계 주입
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    @Override
    public Member save(MemberSignupDto memberSignupDto) throws Exception {
        Member member = memberSignupDto.toEntity();
        member.setMemberPoint(10000L);
        Member saveMember = memberRepository.save(member);
        return saveMember;
    }

    @Override
    public Optional<Member> findOne(Long memberNo) throws Exception {
        return Optional.empty();
    }

    @Override
    public List<Member> findAll() throws Exception {
        return null;
    }

    @Override
    public MemberInfoDto getMyInfo(String memberId) throws Exception {

        Optional<Member> byMemberId = memberRepository.findByMemberId(memberId);
        Member getMember = byMemberId.get();

        MemberInfoDto memberInfoDto = new MemberInfoDto(getMember);
        return memberInfoDto;

    }

    @Value("${memberImg.path}")
    private String uploadFolder;

    @Override
    @Transactional  //JPA 영속성을 위해 어노테이션 추가 _ 트랜젝션 종료후 JPARepository에 변화가 반영됨
    public void memberUpdate(String id, MemberUpdateDto memberUpdateDto, MultipartFile multipartFile){
        Member member = memberRepository.findByMemberId(id).orElseThrow(() -> new IllegalArgumentException("해당 아이디가 없습니다."));

        if(multipartFile != null ){
            String imageFileName = member.getMemberId() + "_" + multipartFile.getOriginalFilename();
            Path imageFIlePath = Paths.get(uploadFolder + imageFileName);

            if(multipartFile.getSize() != 0) {//파일이 업로드 되었는지 확인
                try {
                    if (member.getMemberImgUrl() != null) {//이미 프로필 사진이 있을경우
                        File file = new File(uploadFolder + member.getMemberImgUrl());
                        file.delete(); // 원래파일 삭제
                    }
                    Files.write(imageFIlePath, multipartFile.getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                member.memberImgUrlUpdate(imageFileName);
            }
        }


        if(memberUpdateDto.getPassword()!=null)
            member.memberUpdate(memberUpdateDto.getPassword());

    }

    @Override
    public void withdraw(Member deleteMember) throws Exception {

        memberRepository.delete(deleteMember);
    }

    @Override
    public boolean checkMemberIdDuplicate(String memberId){
        return memberRepository.existsByMemberId(memberId);
    }


}
