package hello.example.porthub.service;

import hello.example.porthub.domain.MemberDto;
import hello.example.porthub.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor    //final이 붙은 필드를 가지고 생성자를 만듬
public class MemberService {

    //의존성 주입을 받음. 생성자 주입
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public int save(MemberDto memberDto) {
        memberDto.setPasswordHash(bCryptPasswordEncoder.encode(memberDto.getPasswordHash()));
        return memberRepository.save(memberDto);
    }

//
//    private MemberDto findByUserName(String UserName) {
//        return memberRepository.findByUserName(UserName);
//    }

    public String UserNameCheck(String UserName) {
        MemberDto memberDto = memberRepository.findByUserName(UserName);
        if (memberDto == null) {
            return "ok";
        } else {
            return "no";
        }
    }

}
