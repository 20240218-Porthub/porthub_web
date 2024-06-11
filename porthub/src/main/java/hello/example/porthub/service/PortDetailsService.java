package hello.example.porthub.service;

import hello.example.porthub.config.PortDetails;
import hello.example.porthub.domain.MemberDto;
import hello.example.porthub.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PortDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MemberDto> optionalMemberDto = Optional.ofNullable(memberRepository.findByEmail(username));

        if (optionalMemberDto.isPresent()) {
            return new PortDetails(optionalMemberDto.get());
        } else {
            throw new UsernameNotFoundException("해당 유저를 찾을 수 없습니다.");
        }
    }

}
