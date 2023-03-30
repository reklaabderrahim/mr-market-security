package fr.mr_market.mr_market_security.service.impl;

import fr.mr_market.mr_market_security.exception.BadRequestException;
import fr.mr_market.mr_market_security.exception.UnauthorizedException;
import fr.mr_market.mr_market_security.model.auth.AuthenticationRequest;
import fr.mr_market.mr_market_security.model.auth.AuthenticationResponse;
import fr.mr_market.mr_market_security.model.auth.RegisterRequest;
import fr.mr_market.mr_market_security.model.token.Token;
import fr.mr_market.mr_market_security.model.token.TokenType;
import fr.mr_market.mr_market_security.model.user.AuthUser;
import fr.mr_market.mr_market_security.model.user.AuthUserRole;
import fr.mr_market.mr_market_security.model.user.Role;
import fr.mr_market.mr_market_security.service.AuthenticationService;
import fr.mr_market.mr_market_security.service.CustomUserDetailsService;
import fr.mr_market.mr_market_security.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthUser register(RegisterRequest request, List<Role> roles, Map<String, Object> attributes) {
        if(userDetailsService.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email address already in use.");
        }
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        return userDetailsService.createUser(request, roles, attributes);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userDetailsService.findByEmail(request.getEmail());
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        var jwtToken = jwtService.generateTokens(user);
        jwtService.revokeAllUserTokens(user);
        jwtService.saveUserToken(user, jwtToken.get(TokenType.REFRESH_TOKEN.getValue()));
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse refreshToken(HttpServletRequest request) {
        return generateToken(jwtService.verifyToken(request));
    }

    public AuthenticationResponse confirm(String token) {
        return generateToken(jwtService.verifyToken(token));
    }

    private AuthenticationResponse generateToken(Token jwtService) {
        Token storedToken = jwtService;

        if (storedToken.isRevoked()) {
            throw new UnauthorizedException("You can't request login temporarily");
        }
        var jwtToken = jwtService.generateTokens(storedToken.getAuthUser());
        jwtService.revokeAllUserTokens(storedToken.getAuthUser());
        jwtService.saveUserToken(storedToken.getAuthUser(), jwtToken.get(TokenType.REFRESH_TOKEN.getValue()));
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}
