package fr.mr_market.mr_market_security;

import fr.mr_market.mr_market_security.config.CertConfig;
import fr.mr_market.mr_market_security.config.RecaptchaProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({RecaptchaProperties.class, CertConfig.class})
public class MrMarketSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(MrMarketSecurityApplication.class, args);
    }

}
