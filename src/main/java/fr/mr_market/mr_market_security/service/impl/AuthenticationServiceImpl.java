package fr.mr_market.mr_market_security.service.impl;

import fr.mr_market.mr_market_security.exception.UnauthorizedException;
import fr.mr_market.mr_market_security.model.auth.AuthenticationRequest;
import fr.mr_market.mr_market_security.model.auth.AuthenticationResponse;
import fr.mr_market.mr_market_security.model.auth.RegisterRequest;
import fr.mr_market.mr_market_security.model.token.Token;
import fr.mr_market.mr_market_security.model.token.TokenType;
import fr.mr_market.mr_market_security.model.user.AuthUser;
import fr.mr_market.mr_market_security.model.user.AuthUserRole;
import fr.mr_market.mr_market_security.model.user.Role;
import fr.mr_market.mr_market_security.repository.TokenRepository;
import fr.mr_market.mr_market_security.repository.UserRepository;
import fr.mr_market.mr_market_security.service.AuthenticationService;
import fr.mr_market.mr_market_security.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = AuthUser.builder().firstName(request.getFirstName()).lastName(request.getLastName()).email(request.getEmail()).password(passwordEncoder.encode(request.getPassword())).authUserRoles(Collections.singletonList(AuthUserRole.builder().role(Role.USER).build())).build();
        var savedUser = repository.save(user);
        var jwtToken = jwtService.generateTokens(user);
        saveUserToken(savedUser, jwtToken.get(TokenType.REFRESH_TOKEN.getValue()));
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = repository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateTokens(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken.get(TokenType.REFRESH_TOKEN.getValue()));
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse refreshToken(HttpServletRequest request) {
        Token storedToken = jwtService.verifyToken(request);

        if (storedToken.isRevoked()) {
            throw new UnauthorizedException("You can't request login temporarily");
        }
        var jwtToken = jwtService.generateTokens(storedToken.getAuthUser());
        revokeAllUserTokens(storedToken.getAuthUser());
        saveUserToken(storedToken.getAuthUser(), jwtToken.get(TokenType.REFRESH_TOKEN.getValue()));
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    private void saveUserToken(AuthUser user, String jwtToken) {
        var token = Token.builder().authUser(user).token(jwtToken).tokenType(TokenType.REFRESH_TOKEN).isRevoked(false).build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(AuthUser user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) return;
        validUserTokens.forEach(token -> {
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
