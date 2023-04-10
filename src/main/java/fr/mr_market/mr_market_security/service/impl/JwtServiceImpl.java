package fr.mr_market.mr_market_security.service.impl;

import fr.mr_market.mr_market_security.exception.UnauthorizedException;
import fr.mr_market.mr_market_security.model.token.Token;
import fr.mr_market.mr_market_security.model.token.TokenType;
import fr.mr_market.mr_market_security.model.user.AuthUser;
import fr.mr_market.mr_market_security.repository.TokenRepository;
import fr.mr_market.mr_market_security.service.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class JwtServiceImpl implements JwtService {

    public static final int ONE_MONTH = 2629800;
    public static final int ONE_HOUR = 3600;
    private final JwtEncoder jwtEncoder;
    private final TokenRepository tokenRepository;
    private final JwtDecoder jwtDecoder;

    @Override
    public String generateToken(String username, Integer seconds, String authorities) {
        Instant now = Instant.now();
        var jwtClaimsSetBuilder = encodeToken(username, now, now.plus(seconds, ChronoUnit.SECONDS));
        if (authorities != null) {
            jwtClaimsSetBuilder.claim("scope", authorities);
        }
        var jwtClaimsSet = jwtClaimsSetBuilder.build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
    }

    @Override
    public Map<String, String> generateTokens(UserDetails userDetails) {
        Map<String, String> token = new HashMap<>();
        token.put(TokenType.ACCESS_TOKEN.getValue(), generateToken(userDetails.getUsername(), ONE_HOUR, extractAuthorities(userDetails)));
        token.put(TokenType.REFRESH_TOKEN.getValue(), generateToken(userDetails.getUsername(), ONE_MONTH, null));
        return token;
    }

    @Override
    public Token verifyToken(String token, String email, List<TokenType> tokenTypes) {
        final Jwt jwt = checkTokenValidity(token);
        return tokenRepository.findByTokenAndAuthUser_emailAndTokenTypeIn(jwt.getTokenValue(), email, tokenTypes)
                .orElseThrow(() -> new UnauthorizedException("Token not found"));
    }

    @Override
    public Token verifyToken(String token, List<TokenType> tokenTypes) {
        final Jwt jwt = checkTokenValidity(token);
        return tokenRepository.findByTokenAndTokenTypeIn(jwt.getTokenValue(), tokenTypes)
                .orElseThrow(() -> new UnauthorizedException("Token not found"));
    }

    private Jwt checkTokenValidity(String token) {
        final Jwt jwt;
        if (token == null) {
            throw new UnauthorizedException("No authorization found within the request");
        }
        try {
            jwt = jwtDecoder.decode(token);
        } catch (JwtException e) {
            throw new UnauthorizedException("An error occurred while attempting to decode the Jwt");
        }
        return jwt;
    }

    @Override
    public void saveUserToken(AuthUser user, String jwtToken, TokenType tokenType) {
        var token = Token.create(jwtToken, tokenType, false, user);
        tokenRepository.save(token);
    }

    @Override
    public void revokeAllUserTokens(AuthUser user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) return;
        validUserTokens.forEach(token -> token.setRevoked(true));
        tokenRepository.saveAll(validUserTokens);
    }

    private JwtClaimsSet.Builder encodeToken(String username, Instant startDate, Instant endDate) {
        return JwtClaimsSet.builder().subject(username).issuedAt(startDate).issuer("mr-market").expiresAt(endDate);
    }

    private String extractAuthorities(UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));
    }
}

