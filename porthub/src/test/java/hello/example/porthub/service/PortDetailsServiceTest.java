package hello.example.porthub.service;

import hello.example.porthub.config.PortDetails;
import hello.example.porthub.domain.MemberDto;
import hello.example.porthub.repository.MemberRepository;
import hello.example.porthub.service.PortDetailsService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

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
}
