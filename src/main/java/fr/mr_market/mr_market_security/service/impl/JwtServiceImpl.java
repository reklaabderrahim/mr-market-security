package fr.mr_market.mr_market_security.service.impl;

import fr.mr_market.mr_market_security.exception.UnauthorizedException;
import fr.mr_market.mr_market_security.model.token.Token;
import fr.mr_market.mr_market_security.model.token.TokenType;
import fr.mr_market.mr_market_security.repository.TokenRepository;
import fr.mr_market.mr_market_security.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class JwtServiceImpl implements JwtService {

    public static final int ONE_MONTH = 2629800;
    private final JwtEncoder jwtEncoder;
    private final TokenRepository tokenRepository;
    private final JwtDecoder jwtDecoder;

    public Map<String, String> generateTokens(UserDetails userDetails) {
        Map<String, String> token = new HashMap<>();
        String accessToken = generateAccessToken(userDetails);
        String refreshToken = generateRefreshToken(userDetails);
        token.put(TokenType.ACCESS_TOKEN.getValue(), accessToken);
        token.put(TokenType.REFRESH_TOKEN.getValue(), refreshToken);
        return token;
    }

    public String generateAccessToken(UserDetails userDetails) {
        String authorities = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));
        Instant now = Instant.now();
        var jwtClaimsSet = encodeToken(userDetails.getUsername(), now, now.plus(30, ChronoUnit.SECONDS)).claim("role", authorities).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
    }

    public String generateRefreshToken(UserDetails userDetails) {
        Instant now = Instant.now();
        var jwtClaimsSet = encodeToken(userDetails.getUsername(), now, now.plus(ONE_MONTH, ChronoUnit.SECONDS)).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
    }

    public Token verifyToken(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        final Jwt jwt;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("No authorization found within the request");
        }
        try {
            jwt = jwtDecoder.decode(authHeader);
        } catch (JwtException e) {
            throw new UnauthorizedException("An error occurred while attempting to decode the Jwt");
        }
        return tokenRepository.findByToken(jwt.getTokenValue()).orElseThrow(() -> new UnauthorizedException("Token not found"));
    }

    private JwtClaimsSet.Builder encodeToken(String username, Instant startDate, Instant endDate) {
        return JwtClaimsSet.builder().subject(username).issuedAt(startDate).issuer("mr-market").expiresAt(endDate);
    }
}

