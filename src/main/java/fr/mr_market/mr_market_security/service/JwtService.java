package fr.mr_market.mr_market_security.service;

import fr.mr_market.mr_market_security.model.token.Token;
import fr.mr_market.mr_market_security.model.user.AuthUser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface JwtService {
    Map<String, String> generateTokens(UserDetails userDetails);

    String generateAccessToken(UserDetails userDetails);

    String generateAccessToken(String username, Integer seconds, String authorities);

    String generateRefreshToken(UserDetails userDetails);

    Token verifyToken(HttpServletRequest request);
    Token verifyToken(String token);

    void saveUserToken(AuthUser user, String jwtToken);

    void revokeAllUserTokens(AuthUser user);
}

