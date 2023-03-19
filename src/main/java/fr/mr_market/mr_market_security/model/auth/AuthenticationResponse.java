package fr.mr_market.mr_market_security.model.auth;

import fr.mr_market.mr_market_security.model.token.TokenType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private Map<String, String> token;
}
