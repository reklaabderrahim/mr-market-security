package fr.mr_market.mr_market_security.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.List;

@Getter
@ConfigurationProperties(prefix = "oauth2")
public class AppProperties {
    private final Cors cors = new Cors();
    private final OAuth2 oauth2 = new OAuth2();

    private final Rsa rsa = new Rsa();
    private final Recaptcha recaptcha = new Recaptcha();

    @Data
    public static class Rsa {
        private RSAPublicKey publicKey;
        private RSAPrivateKey privateKey;
    }

    @Data
    public static class Recaptcha {
        private String verifyUrl;
        private String secretKey;

        private Boolean disabled;
    }

    @Data
    public static class Cors {
        private List<String> allowedMethods;
        private List<String> allowedHeaders;
        private List<String> allowedOrigins;
        private Long maxAge;
        private Boolean allowCredentials;

    }

    @Data
    public static final class OAuth2 {
        private List<String> authorizedRedirectUris;
    }
}
