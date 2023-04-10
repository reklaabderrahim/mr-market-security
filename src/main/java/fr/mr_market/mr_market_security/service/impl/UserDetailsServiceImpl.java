package fr.mr_market.mr_market_security.service.impl;

import fr.mr_market.mr_market_security.model.auth.RegisterRequest;
import fr.mr_market.mr_market_security.model.user.AuthUser;
import fr.mr_market.mr_market_security.model.user.Role;
import fr.mr_market.mr_market_security.repository.UserRepository;
import fr.mr_market.mr_market_security.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements CustomUserDetailsService {
    private final UserRepository userRepository;

    @Override
    public AuthUser findByEmail(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email : " + email));
    }

    @Override
    public Optional<AuthUser> findByEmailOptional(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public AuthUser createUser(RegisterRequest registerRequest, List<Role> roles, Map<String, Object> attributes) {
        AuthUser authUser = AuthUser.create(registerRequest, roles, attributes);
        return userRepository.save(authUser);
    }

    @Override
    public AuthUser create(AuthUser authUser) {
        return userRepository.save(authUser);
    }

    @Override
    public void update(AuthUser authUser) {
        userRepository.saveAndFlush(authUser);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
