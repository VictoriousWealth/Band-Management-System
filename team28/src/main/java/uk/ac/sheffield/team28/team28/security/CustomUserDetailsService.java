
package uk.ac.sheffield.team28.team28.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import uk.ac.sheffield.team28.team28.model.Member;
import uk.ac.sheffield.team28.team28.repository.MemberRepository;


@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch the user by email (username)
        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        // Convert Member to Spring Security's UserDetails
        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles("USER") // Assign a default role
                .build();
    }
}
