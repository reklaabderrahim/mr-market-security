package fr.mr_market.mr_market_security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "recaptcha")
public record RecaptchaProperties(String verifyUrl, String secretKey) {
}
