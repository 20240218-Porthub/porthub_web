package hello.example.porthub.config;

import hello.example.porthub.domain.MemberDto;
import hello.example.porthub.repository.MemberRepository;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class PortDetails implements UserDetails {

    private MemberDto memberDto;

    public PortDetails(MemberDto memberDto) {
        this.memberDto = memberDto;
    }

    //권한별 페이지 접근 설정
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        if (memberDto != null && memberDto.getRole() != null) {
            String role = memberDto.getRole();
            if (!role.startsWith("ROLE_")) {
                role = "ROLE_" + role;
            }
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return memberDto.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return memberDto.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        if (memberDto.getRole().equals("BAN")) {

            throw new LockedException("The account is locked");
        }

        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
