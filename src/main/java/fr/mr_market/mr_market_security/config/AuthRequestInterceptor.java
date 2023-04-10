package fr.mr_market.mr_market_security.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import fr.mr_market.mr_market_security.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthRequestInterceptor implements RequestInterceptor {
    private final JwtService jwtService;
    @Override
    public void apply(RequestTemplate requestTemplate) {
        String accessToken = jwtService.generateToken("internal", 2, "internal");
        requestTemplate.header("Authorisation", accessToken);
    }
}
