package fr.mr_market.mr_market_security.service;

import fr.mr_market.mr_market_security.model.auth.RegisterRequest;
import fr.mr_market.mr_market_security.model.user.AuthUser;
import fr.mr_market.mr_market_security.model.user.Role;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CustomUserDetailsService {
    AuthUser findByEmail(String email) throws UsernameNotFoundException;

    Optional<AuthUser> findByEmailOptional(String email);

    AuthUser loadUserById(Integer id);

    AuthUser createUser(RegisterRequest registerRequest, List<Role> roles, Map<String, Object> attributes);

    AuthUser create(AuthUser authUser);

    Boolean existsByEmail(String email);
}
