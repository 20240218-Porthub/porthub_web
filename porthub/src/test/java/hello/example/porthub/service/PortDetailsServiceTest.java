package hello.example.porthub.service;

import hello.example.porthub.config.PortDetails;
import hello.example.porthub.domain.MemberDto;
import hello.example.porthub.repository.MemberRepository;
// import hello.example.porthub.service.PortDetailsService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
// import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PortDetailsServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private PortDetailsService portDetailsService;

    @Test
    public void loadUserByUsername_UserExists_ReturnsPortDetails() {
        // Given
        String email = "test@example.com";
        MemberDto memberDto = new MemberDto();
        memberDto.setEmail(email);
        when(memberRepository.findByEmail(email)).thenReturn(memberDto);

        // When
        PortDetails userDetails = (PortDetails) portDetailsService.loadUserByUsername(email);

        // Then
        assertEquals(email, userDetails.getUsername());
        verify(memberRepository, times(1)).findByEmail(email);
    }

    @Test
    public void loadUserByUsername_UserDoesNotExist_ThrowsUsernameNotFoundException() {
        // Given
        String email = "nonexistent@example.com";
        when(memberRepository.findByEmail(email)).thenReturn(null);

        // When, Then
        try {
            System.out.println(email);
            portDetailsService.loadUserByUsername(email);
        } catch (UsernameNotFoundException e) {
            assertEquals("해당 유저를 찾을 수 없습니다.", e.getMessage());
            verify(memberRepository, times(1)).findByEmail(email);
        }
    }

    @Test
    public void testGetAuthorities() {
        // 테스트할 MemberDto 생성
        MemberDto memberDto = new MemberDto();
        memberDto.setRole("USER"); // 예시로 "USER" 역할 설정

        // MemberDto를 가진 PortDetails 객체 생성
        PortDetails portDetails = new PortDetails(memberDto);

        // getAuthorities() 메서드 호출
        Collection<? extends GrantedAuthority> authorities = portDetails.getAuthorities();

        // 결과 확인
        assertEquals(1, authorities.size()); // 권한이 하나인지 확인
        assertEquals("USER", authorities.iterator().next().getAuthority()); // 권한이 "USER"인지 확인
    }
}
