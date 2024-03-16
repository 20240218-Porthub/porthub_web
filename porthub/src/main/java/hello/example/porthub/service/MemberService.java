package hello.example.porthub.service;

import hello.example.porthub.domain.MemberDto;
import hello.example.porthub.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor    //final이 붙은 필드를 가지고 생성자를 만듬
public class MemberService {

    //의존성 주입을 받음. 생성자 주입
    private final MemberRepository memberRepository;

    public int save(MemberDto memberDto) {
        return memberRepository.save(memberDto);
    }
}
