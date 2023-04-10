package fr.mr_market.mr_market_security.service;

import fr.mr_market.mr_market_security.model.token.Token;
import fr.mr_market.mr_market_security.model.token.TokenType;
import fr.mr_market.mr_market_security.model.user.AuthUser;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Map;

public interface JwtService {
    String generateToken(String username, Integer seconds, String authorities);

    Map<String, String> generateTokens(UserDetails userDetails);

    Token verifyToken(String token, String email, List<TokenType> tokenTypes);

    Token verifyToken(String token, List<TokenType> tokenTypes);

    void saveUserToken(AuthUser user, String jwtToken, TokenType tokenType);

    void revokeAllUserTokens(AuthUser user);
}

