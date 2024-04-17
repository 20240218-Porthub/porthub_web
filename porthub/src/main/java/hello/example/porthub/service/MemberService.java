package hello.example.porthub.service;

import hello.example.porthub.domain.MemberDto;
import hello.example.porthub.domain.ProfileDto;
import hello.example.porthub.repository.MemberRepository;
import hello.example.porthub.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor    //final이 붙은 필드를 가지고 생성자를 만듬
public class MemberService {

    //의존성 주입을 받음. 생성자 주입
    private final MemberRepository memberRepository;
    private final ProfileRepository profileRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public int save(MemberDto memberDto) {
        memberDto.setRole("USER");
        memberDto.setPasswordHash(bCryptPasswordEncoder.encode(memberDto.getPasswordHash()));
        if(memberRepository.save(memberDto)>0){
            memberDto=memberRepository.findByUserName(memberDto.getUserName());
            profileRepository.newmeta(memberDto.getUserID());
            return 1;
        }
        else{
            return 0;
        }
    }

    public String UserNameCheck(String UserName) {
        MemberDto memberDto = memberRepository.findByUserName(UserName);
        if (memberDto == null) {
            return "ok";
        } else {
            return "no";
        }
    }

    public boolean EmailDuplicateCheck(String Email) {
        MemberDto memberDto = memberRepository.findByEmail(Email);

        if (memberDto == null) {
            return true;
        } else {
            return false;
        }
    }

    public int ImageSave(MemberDto memberDto){
        if(memberRepository.imagesave(memberDto)>0){
            return 1;
        }
        else{
            return 0;
        }
    }
  
    public String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername(); // 이메일을 반환하는 것으로 가정
        }
        return null; // 인증된 사용자가 없는 경우 또는 사용자 정보를 가져올 수 없는 경우
    }

  
}
